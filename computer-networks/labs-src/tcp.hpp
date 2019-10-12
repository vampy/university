#pragma once
#include "socket.hpp"

// TCP socket
class SafeSocketTCP : public SafeSocket
{
private:
    uint32_t m_listen_connections; // indicate the number of listen connections
    SafeSocketTCP *m_temp_client;      // useful when init signals are transmited, clear memory

public:
    SafeSocketTCP();

    virtual ~SafeSocketTCP();

    virtual SafeSocketTCP *open()
    {
        SafeSocket::open(m_domain, m_type, 0);
        return this;
    }

    virtual SafeSocketTCP *bind()
    {
        SafeSocket::bind();
        return this;
    }

    virtual SafeSocketTCP *setOptionReuseAddress()
    {
        SafeSocket::setOptionReuseAddress();
        return this;
    }

    virtual SafeSocketTCP *setOptionReceiveTimeout(const timeval &timeout)
    {
        SafeSocket::setOptionReceiveTimeout(timeout);
        return this;
    }

    virtual SafeSocketTCP *setOptionSendTimeout(const timeval &timeout)
    {
        SafeSocket::setOptionSendTimeout(timeout);
        return this;
    }

    virtual SafeSocketTCP *setAddress(const char *ip, uint16_t port)
    {
        SafeSocket::setAddress(ip, port);
        return this;
    }

    virtual SafeSocketTCP *setAddress(uint16_t port)
    {
        SafeSocket::setAddress(port);
        return this;
    }

    virtual SafeSocketTCP *setCloseSocket(bool value)
    {
        SafeSocket::setCloseSocket(value);
        return this;
    }

    virtual SafeSocketTCP *listen()
    {
        if (!m_listen_connections)
        {
            throw SafeSocketException("Listen connections can not be 0");
        }

        if (::listen(m_socket, m_listen_connections) == -1)
        {
            throwException("Error listen socket: ");
        }

        return this;
    }

    virtual uint32_t getListenConnections() const { return m_listen_connections; }
    virtual SafeSocketTCP *setListenConnections(uint32_t value)
    {
        m_listen_connections = value;
        return this;
    }

    virtual SafeSocketTCP *accept()
    {
        DELETE(m_temp_client); // clean client if interrupt happened
        m_temp_client = new SafeSocketTCP();

        // get client socket
        socklen_t len = m_address_size;
        int32_t rvalue = ::accept(m_socket, m_temp_client->getAddress(), &len);
        if (rvalue == -1)
        {
            throwException("Error accept socket connection: ");
        }
        m_temp_client->setSocket(rvalue);

        // uninit temp pointer
        SafeSocketTCP *ret = m_temp_client;
        m_temp_client = nullptr;
        return ret;
    }

    virtual SafeSocketTCP *connect()
    {
        if (::connect(m_socket, (sockaddr *)m_address, m_address_size) == -1)
        {
            throwException("Error connect socket: ");
        }

        return this;
    }

    template<typename Type> // Type must be a pointer
    int32_t receive(Type ptr, size_t length)
    {
        // validate
        if (!std::is_pointer<Type>::value)
        {
            throw std::invalid_argument("Error receive socket: template Type is not a pointer");
        }

        ssize_t rvalue = ::recv(m_socket, (void *)ptr, length, 0);
        if (rvalue == 0)
        {
            throw SafeSocketException("Error receive socket: no message or client closed the socket");
        }
        else if (rvalue == -1)
        {
            throwException("Error receive socket: ");
        }

        return rvalue;
    }

    template<typename Type>  // Type must be a pointer
    int32_t send(Type ptr, size_t length)
    {
        // validate
        if (!std::is_pointer<Type>::value)
        {
            throw std::invalid_argument("Error send socket: template Type is not a pointer");
        }

        ssize_t rvalue = ::send(m_socket, (void *)ptr, length, 0);
        if (rvalue == -1)
        {
            throwException("Error send socket: ");
        }

        return rvalue;
    }

    template<typename Type> // only values on the stack
    void sendScalar(const Type value)
    {
        // validate
        if (!std::is_enum<Type>::value && !std::is_arithmetic<Type>::value)
        {
            throw std::invalid_argument("Error sendScalar socket: template Type is not scalar");
        }

        ssize_t rvalue = ::send(m_socket, (void *)&value, sizeof(Type), 0);
        if (rvalue == -1)
        {
            throwException("Error sendScalar socket: ");
        }
    }
    template<typename Type> // only values on the stack
    Type receiveScalar()
    {
        // validate
        if (!std::is_enum<Type>::value && !std::is_arithmetic<Type>::value)
        {
            throw std::invalid_argument("Error receiveScalar socket: template Type is not scalar");
        }

        Type value;
        ssize_t rvalue = ::recv(m_socket, (void *)&value, sizeof(Type), 0);
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
};
