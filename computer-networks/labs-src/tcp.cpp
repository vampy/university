#include "tcp.hpp"

SafeSocketTCP::SafeSocketTCP() : SafeSocket()
{
    // set values here, because socket may not be created by us, see accept()
    m_domain = AF_INET;
    m_type = SOCK_STREAM;
    m_temp_client = nullptr;
    m_listen_connections = 0;
}

SafeSocketTCP::~SafeSocketTCP()
{
    DELETE(m_temp_client);  // clean client if intterupt happened
}
