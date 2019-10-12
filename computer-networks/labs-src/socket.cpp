#include "socket.hpp"

SafeSocket::SafeSocket()
{
    m_socket = -1;
    m_close_socket = true;
    m_address = new sockaddr_in;
    memset(m_address, 0, m_address_size);
}

SafeSocket::~SafeSocket()
{
    if (m_socket != -1 && m_close_socket) // close if not already closed
    {
        close();
    }

    DELETE(m_address);
}

void SafeSocket::checkIfOpen() const
{
    if (m_socket == -1)
    {
        throw SafeSocketException("Error socket is closed");
    }
}

SafeSocket* SafeSocket::setOption(int option_name, std::string option_name_str)
{
    checkIfOpen();

    int32_t yes = 1;
    if (setsockopt(m_socket, SOL_SOCKET, option_name, &yes, sizeof(yes)) == -1)
    {
        throwException("Error setsockopt option_name = " + option_name_str +  " for socket: ");
    }

    return this;
}

SafeSocket* SafeSocket::setOptionTimeout(const timeval &timeout, int option_name, std::string option_name_str)
{
    checkIfOpen();

    if (setsockopt(m_socket, SOL_SOCKET, option_name, (char *)&timeout, sizeof(timeout)) == -1)
    {
        throwException("Error setsockopt option_name = " + option_name_str +  " for socket: ");
    }

    return this;
}

SafeSocket* SafeSocket::open(int32_t domain, int32_t type, int32_t protocol)
{
    m_domain  = domain;
    m_type    = type;
    m_socket  = create(domain, type, protocol);

    return this;
}

void SafeSocket::close()
{
    checkIfOpen();

    if (::close(m_socket) == -1)
    {
        throwException("Error close socket: ");
    }
    m_socket = -1;
}

SafeSocket* SafeSocket::bind()
{
    checkIfOpen();

    if (::bind(m_socket, (sockaddr *)m_address, m_address_size) == -1)
    {
        throwException("Error bind socket: ");
    }

    return this;
}

SafeSocket* SafeSocket::setAddress(const char *ip, uint16_t port)
{
    // validate
    size_t ip_len = strlen(ip);
    if (ip_len > INET_ADDRSTRLEN)
    {
        throwException("Ip address is too big: " + std::to_string(ip_len), false);
    }

    memset(m_address, 0, m_address_size);
    m_address->sin_family = m_domain;
    m_address->sin_port = htons(port);
    inet_pton(m_domain, ip, &(m_address->sin_addr));

    return this;
}

SafeSocket* SafeSocket::setAddress(uint16_t port)
{
    memset(m_address, 0, m_address_size);
    m_address->sin_family = m_domain;
    m_address->sin_port = htons(port);
    m_address->sin_addr.s_addr = INADDR_ANY;

    return this;
}

std::string* SafeSocket::receiveString(size_t length)
{
    char *read_string = new char[length + 1];
    receiveCharString(read_string, length);
    read_string[length] = 0;

    std::string *str = new std::string(read_string);

    delete [] read_string;
    return str;
}
