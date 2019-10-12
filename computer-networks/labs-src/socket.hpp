#pragma once
#include <cstdlib>
#include <cstring>
#include <string>
#include <cstdint>
#include <stdexcept>
#include <iostream>
#include <sstream> // for ostringstream
#include <cstddef>
#include <type_traits>

// socket libs
#include <unistd.h>
#include <netinet/in.h>
#include <netinet/udp.h>
#include <netinet/tcp.h>
#include <arpa/inet.h>

#define DELETE(var) if (var != nullptr) { delete var; var = nullptr; }
#define DELETE_ARRAY(array) if (array != nullptr) { delete [] array; array = nullptr; }


class SafeSocketException : public std::runtime_error
{
protected:
    int m_error; // hold the errno value

public:
    SafeSocketException(const std::string argument) : std::runtime_error(argument)
    {
        m_error = errno;
    }

    bool isErrorTryAgain() const // se if the error is a try again flag
    {
        return m_error == EAGAIN;
    }

    bool isErrorWouldBlock() const
    {
        return m_error == EWOULDBLOCK;
    }
};

// TODO add ipv6
class SafeSocket
{
protected:
    int32_t m_domain; // ipv4 or ipv6
    int32_t m_type;   // TCP or UDP
    int32_t m_socket; // actual socket descriptor
    bool   m_close_socket; // indicate to close socket in the destructor;
    sockaddr_in *m_address;
    const size_t m_address_size = sizeof(struct sockaddr); // ipv4 and ipv6 compatible

    virtual void checkIfOpen() const;

    // only set boolean options
    virtual SafeSocket* setOption(int option_name, std::string option_name_str);
    virtual SafeSocket* setOptionTimeout(const timeval &timeout, int option_name, std::string option_name_str);

    // throw a runtime error with a predefined messsage and with an error number if desired
    static void throwException(std::string prefix, bool use_error_suffix = true)
    {
        std::ostringstream out;
        out << prefix;
        if (use_error_suffix) out << strerror(errno) << " || errno = " << errno;
        throw SafeSocketException(out.str());
    }

public:
    SafeSocket();
    virtual ~SafeSocket();

    virtual SafeSocket* open(int32_t domain, int32_t type, int32_t protocol);
    virtual void close();
    virtual SafeSocket* bind();

    virtual SafeSocket *setOptionReuseAddress() // reuse address
    {
        return setOption(SO_REUSEADDR, "SO_REUSEADDR");
    }

    virtual SafeSocket *setOptionReceiveTimeout(const timeval &timeout)
    {
        return setOptionTimeout(timeout, SO_RCVTIMEO, "SO_RCVTIMEO");
    }

    virtual void resetOptionReceiveTimeout()
    {
        timeval timeout = {.tv_sec = 0, .tv_usec = 0};
        setOptionReceiveTimeout(timeout);
    }

    virtual SafeSocket *setOptionSendTimeout(const timeval &timeout)
    {
        return setOptionTimeout(timeout, SO_SNDTIMEO, "SO_SNDTIMEO");
    }

    virtual void resetOptionSendTimeout()
    {
        timeval timeout = {.tv_sec = 0, .tv_usec = 0};
        setOptionSendTimeout(timeout);
    }

    // set server address with ip
    virtual SafeSocket* setAddress(const char *ip, uint16_t port);

    // set server address on all interfaces, localhost
    virtual SafeSocket *setAddress(uint16_t port);

    virtual void setSocket(int32_t value) { m_socket = value; }

    // if called this method, the socket will not close automatically in the destructor
    virtual int32_t  getSocket() const { return m_socket; }

    // close socket in destructor flag
    virtual SafeSocket *setCloseSocket(bool value) { m_close_socket = value; return this; }

    // the port in little edian
    virtual uint16_t getPort()         const { return getPort(m_address); }
    virtual uint32_t getIP()           const { return getIP(m_address); }
    virtual std::string getIPString()  const { return getIPString(m_address); }
    virtual bool     isOpen()          const { return m_socket != -1; }

    virtual sockaddr    *getAddress()     const { return (sockaddr *)m_address; }
    virtual sockaddr_in *getAddressIPv4() const { return m_address; }

    // must implement this in children classes
    virtual int32_t receiveCharString(char *str, size_t length) = 0;
    virtual int32_t sendCharString(char *str, size_t length) = 0;

    // send/receive null terminated strings
    std::string* receiveString(size_t length);
    int32_t sendString(const std::string &str, size_t length)
    {
        return sendCharString((char*)str.c_str(), length);
    }
    int32_t sendString(const std::string &str)
    {
        return sendString(str, str.length());
    }

    static uint16_t getPort(sockaddr_in *address)
    {
        return ntohs(address->sin_port);
    }
    static uint32_t getIP(sockaddr_in *address)
    {
        return ntohl(address->sin_addr.s_addr);
    }

    static std::string getIPString(sockaddr_in *address)
    {
        char address_str[INET_ADDRSTRLEN];
        const char *rvalue = inet_ntop(AF_INET, &(address->sin_addr), address_str, INET_ADDRSTRLEN);

        if (rvalue == NULL)
        {
            throwException("Error inet_ntop: ");
        }

        return std::string(address_str);
    }

    static void inet_pton(int domain, const char *src, void *dst)
    {
        int32_t rvalue = ::inet_pton(domain, src, dst);
        if (rvalue == 0)
        {
            throw SafeSocketException("inet_pton: IP is not valid for this family address");
        }
        else if (rvalue == -1)
        {
            throwException("Error inet_pton Address family is not valid: ");
        }
    }

    // crate socket safely
    static int32_t create(int32_t domain, int32_t type, int32_t protocol)
    {
        int32_t rvalue = socket(domain, type, protocol);
        if (rvalue == -1)
        {
            throwException("Error create socket: ");
        }

        return rvalue;
    }
};
