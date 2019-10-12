#pragma once
#include "socket.hpp"
#include "os.hpp"
#include <vector>
#include <algorithm>
#include <queue>

#define MAX_PACKET_BODY 4096
#define GENERATE_ERROR false
using namespace std;

unsigned char generateError(int err_ratio, unsigned char israndom)
{
   if (!GENERATE_ERROR) return 0;
   static int count = 0;

   // using pure random 0 generation here. err_ratio doesn't count
   if (israndom)
   {
        if (count == 0)
            srandom( time(NULL) );

        count++;

        return random() % 2;
   }
   int result;

   // constant 1 only if err_ratio=0 and israndom=false

   if (err_ratio == 0)
        result = 1;
   else
        // generate 0 with a err_ratio percentage
        result = count % err_ratio ? 1 : 0;

   count++;

   return result;

}

class UDPReliablePeer // peer to simulate UDP with TCP
{
protected:
    queue<uint8_t> receive_queue;

public:
    // used only for receive, but send may also fill this, for multiway communication
    uint32_t expected_sequence_nr = 0; // received from the remote party;

     // used only for send
    uint8_t retries = 0;

    // hold the addreess
    uint16_t port = 0;
    uint32_t ip = 0;

    // normal packet
    uint32_t sequence_nr = 0; // only increment for packets containig data
    uint16_t payload_length = 0; // size of payload data, 0 if this is an ACK packet
    uint16_t ack_flag = 0;  // value can be 0 or 1
    uint32_t ack_value = 0; // index of the next expected packet (sequence_nr)
    uint8_t *payload_data = nullptr; // the data content

    void addToQueue(uint8_t *src, size_t src_length)
    {
        assert((receive_queue.size() + src_length) < MAX_PACKET_BODY * 5);
        for (size_t i = 0; i < src_length; i++)
        {
            receive_queue.push(src[i]);
        }
    }

    void removeFromQueue(uint8_t *dst, size_t dst_length)
    {
        assert((receive_queue.size() - dst_length) >= 0);
        for (size_t i = 0; i < dst_length; i++)
        {
            dst[i] = receive_queue.front();
            receive_queue.pop();
        }
    }

    bool isQueueFull()
    {
        return receive_queue.size() == MAX_PACKET_BODY * 5;
    }

    size_t queueSize()
    {
        return receive_queue.size();
    }

    ~UDPReliablePeer()
    {
        DELETE_ARRAY(payload_data);
    }
};

class UDPReliablePacket // packet to simulate UDP with TCP
{
public:
    static uint32_t getHeaderSize()
    {
        return sizeof(uint32_t) * 2 + sizeof(uint16_t) * 2;
    }
    static uint32_t getMaxBodySize()
    {
        //return 1024 * 16 + 1;
        return MAX_PACKET_BODY;
    }

    static uint32_t getMaxPacketSize()
    {
        return getHeaderSize() + getMaxBodySize();
    }

    // convert the packet to a packet buffer that can be sent
    static uint8_t* toBuffer(uint32_t sequence_nr, uint16_t payload_length,  uint16_t ack_flag, uint32_t ack_value, char *payload_data)
    {
        assert(payload_length <= getMaxBodySize());
        uint32_t packet_size = getHeaderSize() + payload_length;
        uint8_t *buffer = new uint8_t[packet_size];

        // add header
        uint32_t size = 0;
        packi32(buffer + size, sequence_nr);    size += 4;
        packi16(buffer + size, payload_length); size += 2;
        packi16(buffer + size, ack_flag);       size += 2;
        packi32(buffer + size, ack_value);      size += 4;

        // add payload
        memcpy(buffer + size, payload_data, payload_length);

        return buffer;
    }

    static UDPReliablePeer* fromBuffer(uint8_t *buffer, size_t buffer_length)
    {
        UDPReliablePeer *peer = new UDPReliablePeer();
        uint32_t size = 0;

        // buffer must at least be ass big as header
        assert(buffer_length >= getHeaderSize());

        // read header
        peer->sequence_nr = unpacku32(buffer + size); size += 4;
        peer->payload_length = unpacku16(buffer + size); size += 2;
        peer->ack_flag = unpacku16(buffer + size); size += 2;
        peer->ack_value = unpacku32(buffer + size); size += 4;

        // buffer must be large enough for body
        assert(buffer_length >= (peer->payload_length + size));
        assert(peer->payload_length <= getMaxBodySize());

        // read body
        peer->payload_data = new uint8_t[peer->payload_length + 1];
        memcpy(peer->payload_data, buffer + size, peer->payload_length);

        return peer;
    }
};

// UDP socket
class SafeSocketUDP : public SafeSocket
{
private:
    sockaddr_in *m_from_address; // used to retain info when we do a receiveFrom
    ip_mreq *m_multicast_address = nullptr; // used for multicast
    // m_address is used for when sending

    vector<UDPReliablePeer*> m_reliable_peers;
    uint32_t m_max_retries = 12;
public:
    SafeSocketUDP() : SafeSocket()
    {
        m_domain = AF_INET;
        m_type = SOCK_DGRAM;
        m_from_address = new sockaddr_in;
        memset(m_from_address, 0, m_address_size);
    }

    ~SafeSocketUDP()
    {
        DELETE(m_from_address);
        DELETE(m_multicast_address);

        // clean reliable peers
        size_t size = m_reliable_peers.size();
        for (size_t i = 0; i < size; i++)
        {
            delete m_reliable_peers.at(i);
        }
        m_reliable_peers.clear();
    }

    void setMaximumRetries(uint32_t retries) { m_max_retries = retries; }

    virtual SafeSocketUDP *open()
    {
        SafeSocket::open(m_domain, m_type, 0);
        return this;
    }

    virtual SafeSocketUDP *bind()
    {
        SafeSocket::bind();
        return this;
    }

    virtual SafeSocketUDP *setOptionMulticastTTL(uint8_t ttl)
    {
        checkIfOpen();

        if (setsockopt(m_socket, IPPROTO_IP, IP_MULTICAST_TTL, &ttl, sizeof(ttl)) == -1)
        {
            throwException("Error setsockopt IP_MULTICAST_TTL for socket: ");
        }

        return this;
    }

    virtual SafeSocketUDP *setOptionMulticastLoop(bool enable)
    {
        assert(m_multicast_address != nullptr);
        checkIfOpen();

        uint8_t value = enable ? 1 : 0;
        if (setsockopt(m_socket, IPPROTO_IP, IP_MULTICAST_LOOP, &value, sizeof(value)) == -1)
        {
            throwException("Error setsockopt IP_MULTICAST_LOOP for socket: ");
        }

        return this;
    }

    virtual SafeSocketUDP *setOptionMulticastAdd()
    {
        assert(m_multicast_address != nullptr);
        checkIfOpen();

        if (setsockopt(m_socket, IPPROTO_IP, IP_ADD_MEMBERSHIP, m_multicast_address, sizeof(*m_multicast_address)) == -1)
        {
            throwException("Error setsockopt IP_ADD_MEMBERSHIP for socket: ");
        }

        return this;
    }

    virtual SafeSocketUDP *setOptionMulticastDrop()
    {
        assert(m_multicast_address != nullptr);
        checkIfOpen();

        if (setsockopt(m_socket, IPPROTO_IP, IP_DROP_MEMBERSHIP, m_multicast_address, sizeof(*m_multicast_address)) == -1)
        {
            throwException("Error setsockopt IP_DROP_MEMBERSHIP for socket: ");
        }

        return this;
    }

    virtual SafeSocketUDP *setOptionReuseAddress()
    {
        SafeSocket::setOptionReuseAddress();
        return this;
    }

    virtual SafeSocketUDP *setOptionReceiveTimeout(const timeval &timeout)
    {
        SafeSocket::setOptionReceiveTimeout(timeout);
        return this;
    }

    virtual SafeSocketUDP *setOptionSendTimeout(const timeval &timeout)
    {
        SafeSocket::setOptionSendTimeout(timeout);
        return this;
    }

    virtual SafeSocketUDP *setOptionBroadcast()
    {
        setOption(SO_BROADCAST, "SO_BROADCAST");
        return this;
    }


    virtual SafeSocketUDP *setAddressMulticast(const char *ip)
    {
        DELETE(m_multicast_address);
        m_multicast_address = new ip_mreq;

        inet_pton(m_domain, ip, &(m_multicast_address->imr_multiaddr));
        m_multicast_address->imr_interface.s_addr = INADDR_ANY;  // interface is any

        return this;
    }

    virtual SafeSocketUDP *setAddress(const char *ip, uint16_t port)
    {
        SafeSocket::setAddress(ip, port);
        return this;
    }

    virtual SafeSocketUDP *setAddress(uint16_t port)
    {
        SafeSocket::setAddress(port);
        return this;
    }

    virtual SafeSocketUDP *setCloseSocket(bool value)
    {
        SafeSocket::setCloseSocket(value);
        return this;
    }

    virtual SafeSocketUDP *setAddressAsFromAddress()
    {
        memcpy(m_address, m_from_address, m_address_size);
        return this;
    }

    virtual sockaddr_in *getFromAddressIPv4() const { return m_from_address; }
    virtual sockaddr    *getFromAddress()     const { return (sockaddr *)m_from_address; }

    // get the from received port and IP
    virtual uint16_t getFromPort()   const { return getPort(m_from_address); }
    virtual uint32_t getFromIP()     const { return getIP(m_from_address); }
    virtual string getFromIPString() const { return getIPString(m_from_address); }

    template<typename Type>  // use m_address as send address
    int32_t send(Type ptr, size_t length)
    {
        // validate
        if (!is_pointer<Type>::value)
        {
            throw invalid_argument("Error send socket: template Type is not a pointer");
        }

        ssize_t rvalue = ::sendto(m_socket, (void *)ptr, length, 0, (sockaddr *)m_address, m_address_size);
        if (rvalue == -1)
        {
            throwException("Error send socket: ");
        }

        return rvalue;
    }

    template<typename Type> // Type must be a pointer
    int32_t receive(Type ptr, size_t length)
    {
        // validate
        if (!is_pointer<Type>::value)
        {
            throw invalid_argument("Error receive socket: template Type is not a pointer");
        }

        memset(m_from_address, 0, m_address_size);
        socklen_t len = m_address_size;
        ssize_t rvalue = ::recvfrom(m_socket, (void *)ptr, length, 0, (sockaddr *)m_from_address, &len);
        if (rvalue == 0)
        {
            throw SafeSocketException("Error receive socket: No messages are available to be received and the peer has performed an orderly shutdown");
        }
        else if (rvalue == -1)
        {
            throwException("Error receive socket: ");
        }

        return rvalue;
    }

    template<typename Type> // only values on the stack
    void sendScalar(const Type value)
    {
        // validate
        if (!is_enum<Type>::value && !is_arithmetic<Type>::value)
        {
            throw invalid_argument("Error sendScalar socket: template Type is not scalar");
        }

        ssize_t rvalue = ::sendto(m_socket, (void *)&value, sizeof(Type), 0, (sockaddr *)m_address, m_address_size);
        if (rvalue == -1)
        {
            throwException("Error sendScalar socket: ");
        }
    }
    template<typename Type> // only values on the stack
    Type receiveScalar()
    {
        // validate
        if (!is_enum<Type>::value && !is_arithmetic<Type>::value)
        {
            throw invalid_argument("Error receiveScalar socket: template Type is not scalar");
        }

        Type value;
        socklen_t len = m_address_size;
        ssize_t rvalue = ::recvfrom(m_socket, (void *)&value, sizeof(Type), 0, (sockaddr *)m_from_address, &len);
        if (rvalue == 0)
        {
            throw SafeSocketException("Error receiveScalar socket: no message or client closed the socket");
        }
        else if (rvalue == -1)
        {
            throwException("Error receiveScalar socket: ");
        }

        return (Type)value;
    }

    virtual int32_t receiveCharString(char *str, size_t length)
    {
        return receive<char*>(str, length);
    }
    virtual int32_t sendCharString(char *str, size_t length)
    {
        return send<char*>(str, length);
    }

    UDPReliablePeer* findPeer(uint32_t ip, uint16_t port)
    {
        if (ip && port) // ip and port are not 0
        {
            size_t size = m_reliable_peers.size();
            for (size_t i = 0; i < size; i++)
            {
                UDPReliablePeer *peer = m_reliable_peers[i];
                if (peer->ip == ip && peer->port == port)
                {
                    return peer;
                }
            }
        }

        // peer not found, create it
        UDPReliablePeer *peer = new UDPReliablePeer();
        peer->ip = ip;
        peer->port = port;
        m_reliable_peers.push_back(peer);

        return peer;
    }

    // send/receive packet simulating TCP using UDP
    int32_t reliableSend(char *buffer, size_t buffer_length)
    {
        // init variables, send to this ip and port
        uint32_t ip = getIP();
        uint16_t port = getPort();
        bool need_split = (buffer_length > UDPReliablePacket::getMaxBodySize());
        timeval ack_timeout = {.tv_sec = 2, .tv_usec = 0};
        uint32_t buffer_location = 0; // location of the buffer sent
        UDPReliablePeer *receive_peer = nullptr,
                *reliable_peer = findPeer(ip, port);

        if (need_split)
        {
            reliable_peer->payload_length = UDPReliablePacket::getMaxBodySize();
        }
        else
        {
            reliable_peer->payload_length = buffer_length;
        }

        while (true)
        {
            assert(reliable_peer->payload_length > 0); DELETE(receive_peer);
            if (reliable_peer->retries >= m_max_retries)
            {
                cout << "Max retry reached, exiting" << endl;
                return -1;
            }
            if (buffer_location >= buffer_length)
            {
                cout << "Finished sending, buffer_location = "
                          << buffer_location << ", buffer_length=" << buffer_length << endl;

                return buffer_location;
            }

            // send data packet
            cout << "Sending data packet: sequence_nr = "
                 << reliable_peer->sequence_nr
                 << ", payload_size = " << reliable_peer->payload_length << endl;
            uint8_t *packed_buffer = UDPReliablePacket::toBuffer(
                        reliable_peer->sequence_nr,
                        reliable_peer->payload_length,
                        reliable_peer->ack_flag,
                        reliable_peer->ack_value,
                        buffer + buffer_location);
            int32_t should_have_sent = UDPReliablePacket::getHeaderSize() + reliable_peer->payload_length;
            if (!generateError(2, 0))
            {
                int32_t sent_bytes = send<uint8_t*>(packed_buffer, should_have_sent);
                assert(sent_bytes == should_have_sent);
            }
            else
            {
                cout << endl << "Dropping DATA packet" << endl << endl;
            }
            DELETE_ARRAY(packed_buffer);

            // receive ACK packet
            setOptionReceiveTimeout(ack_timeout);
            hell:
            uint8_t *packed_ack = new uint8_t[UDPReliablePacket::getMaxPacketSize()];
            uint16_t packed_ack_length = 0;
            try
            {
                // check, drop packet, not for us
                while (packed_ack_length < UDPReliablePacket::getHeaderSize())
                {
                    if (packed_ack_length)
                    {
                        cout << "Dropping packet with packed_ack_length = " << packed_ack_length << endl;
                    }
                    packed_ack_length = receive<uint8_t*>(packed_ack, UDPReliablePacket::getMaxPacketSize());
                    // check if correct ip and port??
                }
            }
            catch (const SafeSocketException &e)
            {
                if (e.isErrorTryAgain()) // timeout receive ACK
                {
                    reliable_peer->retries++;
                    cout << "TIMEOUT: Retrying" << endl;

                    DELETE_ARRAY(packed_ack); continue; // retry
                }
            }
            resetOptionReceiveTimeout();

            // handle type of packet
            receive_peer = UDPReliablePacket::fromBuffer(packed_ack, packed_ack_length); DELETE_ARRAY(packed_ack);
            if (receive_peer->ack_flag == 1) // ack packet
            {
                // correct ACK
                if (receive_peer->ack_value == (reliable_peer->sequence_nr + 1))
                {
                    cout << "Received correct ACK" << endl;

                    // move to next location
                    buffer_location += reliable_peer->payload_length;
                    reliable_peer->retries = 0;
                    reliable_peer->sequence_nr++;

                    // split packet
                    if (need_split && buffer_location < buffer_length)
                    {
                        uint32_t diff = buffer_length - buffer_location;
                        if (diff >= UDPReliablePacket::getMaxBodySize())
                        {
                            // TODO we do not need this
                            reliable_peer->payload_length = UDPReliablePacket::getMaxBodySize();
                        }
                        else // final packet is smaller
                        {
                            reliable_peer->payload_length = diff;
                        }

                        continue;
                    }

                    // all data sent, return
                    DELETE(receive_peer);
                    return buffer_location;
                }
                else // wrong ACK
                {
                    cout << "Received wrong ACK" << endl;

                    if (receive_peer->ack_value == reliable_peer->sequence_nr) // queue is full
                    {
                         usleep(50000);
                    }
                    else if (receive_peer->ack_value > (reliable_peer->sequence_nr + 1)) // resend packet
                    {
                        // error remote party out of sync
                        usleep(50000);
                        reliable_peer->retries++;
                    }
                }

            }
            else // data packet, not used in our app, TODO test
            {
                cout << "Received data packet --------------------" << endl;

                if (!reliable_peer->isQueueFull() && receive_peer->sequence_nr == reliable_peer->expected_sequence_nr)
                {
                    reliable_peer->addToQueue(receive_peer->payload_data, receive_peer->payload_length);
                    reliable_peer->expected_sequence_nr++;
                    cout << "Sending ACK packet: ack_value = " << reliable_peer->expected_sequence_nr << endl;
                    uint8_t *packed_buffer = UDPReliablePacket::toBuffer(
                                reliable_peer->sequence_nr,
                                0, // packet length
                                1, // ACK flag
                                reliable_peer->expected_sequence_nr,
                                buffer);
                    send<uint8_t*>(packed_buffer, UDPReliablePacket::getHeaderSize());
                    DELETE_ARRAY(packed_buffer);
                }
                else if(reliable_peer->isQueueFull())
                {
                    cout << "Queue is full, expected_sequence_nr = " << reliable_peer->expected_sequence_nr << endl;

                    uint8_t *packed_buffer = UDPReliablePacket::toBuffer(
                                reliable_peer->sequence_nr,
                                0, // packet length
                                1, // ACK flag
                                reliable_peer->expected_sequence_nr,
                                buffer);
                    send<uint8_t*>(packed_buffer, UDPReliablePacket::getHeaderSize());
                    DELETE_ARRAY(packed_buffer);
                }
                else
                {
                    goto hell;
                }
            }
        }

        return 0;
    }

    int32_t reliableReceive(char *buffer, size_t buffer_length)
    {
        // use from_address on first call should pe new, and on consecutive calls it should be the last address
        uint32_t ip = m_from_address->sin_addr.s_addr;
        uint16_t port = getPort();
        UDPReliablePeer *reliable_peer = findPeer(ip, port);

        // we have data in the queue
        if (reliable_peer->queueSize())
        {
            cout << "Use data from the queue for the receive" << endl;
            size_t copy_length = min(reliable_peer->queueSize(), buffer_length);
            reliable_peer->removeFromQueue((uint8_t*)buffer, copy_length);

            return copy_length;
        }

        // wait for data
        uint8_t *packed_receive = new uint8_t[UDPReliablePacket::getMaxPacketSize()];
        uint16_t packed_receive_length = 0;
        while (true)
        {
            // receive data packet
            packed_receive_length = receive<uint8_t*>(packed_receive, UDPReliablePacket::getMaxPacketSize());
            if (!reliable_peer->ip || !reliable_peer->port) // set ip:port on first request
            {
                reliable_peer->ip = m_from_address->sin_addr.s_addr;
                reliable_peer->port = m_from_address->sin_port;
            }
            else
            {
                assert(reliable_peer->ip == m_from_address->sin_addr.s_addr && reliable_peer->port == m_from_address->sin_port);
            }

            UDPReliablePeer *receive_peer = UDPReliablePacket::fromBuffer(packed_receive, packed_receive_length);
            DELETE_ARRAY(packed_receive);
            cout << "Received data packet: sequence_nr = " << receive_peer->sequence_nr << endl;

            setAddressAsFromAddress();
            if (receive_peer->sequence_nr == reliable_peer->expected_sequence_nr) // return buffer to user
            {
                if (generateError(2, 0))
                {
                    cout << endl << "Dropping ACK packet" << endl << endl;
                    packed_receive = new uint8_t[UDPReliablePacket::getMaxPacketSize()];
                    DELETE(receive_peer);
                    continue;
                }

                // add to queue
                reliable_peer->addToQueue(receive_peer->payload_data, receive_peer->payload_length);

                // expect next packet
                reliable_peer->expected_sequence_nr++;

                // send back ACK
                cout << "Sending ACK packet: ack_value = " << reliable_peer->expected_sequence_nr << endl;
                uint8_t *packed_buffer = UDPReliablePacket::toBuffer(
                            reliable_peer->sequence_nr,
                            0, // packet length
                            1, // ACK flag
                            reliable_peer->expected_sequence_nr,
                            buffer);
                send<uint8_t*>(packed_buffer, UDPReliablePacket::getHeaderSize());

                // fill user buffer
                size_t copy_length = min(reliable_peer->queueSize(), buffer_length);
                reliable_peer->removeFromQueue((uint8_t*)buffer, copy_length);

                DELETE(receive_peer);
                DELETE_ARRAY(packed_buffer);
                return copy_length;
            }
            else // wrong packet
            {
                cout << "Received wrong packet sequence_nr = " << receive_peer->sequence_nr << endl;

                uint8_t *packed_buffer = UDPReliablePacket::toBuffer(
                            reliable_peer->sequence_nr,
                            0, // packet length
                            1, // ACK flag
                            reliable_peer->expected_sequence_nr,
                            buffer);
                send<uint8_t*>(packed_buffer, UDPReliablePacket::getHeaderSize());
                DELETE_ARRAY(packed_buffer);
                // drop packet
            }

            DELETE(receive_peer);
        }

        return -1;
    }
};
