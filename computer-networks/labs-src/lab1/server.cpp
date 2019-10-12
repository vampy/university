#include "../os.hpp"
#include "../tcp.hpp"

enum {ERROR_RECEIVE = -1, ERROR_OVERFLOW = -2};
static const uint16_t PORT = 23233;
static const uint16_t TIMEOUT = 10;
static const int MAX_CLIENTS = 5;
SafeSocketTCP *server_socket = nullptr, *client_socket = nullptr;
using namespace std;

void alarm_handle(int signal)
{
    cout << "Timed out with the client." << endl;
    if (client_socket != nullptr)
    {
        client_socket->sendScalar<int32_t>(ERROR_RECEIVE);
        client_socket->close();
    }
    DELETE(client_socket);
    DELETE(server_socket);
    exit(0);
}
void int_handle(int signal)
{
    cout << "CTRL + C pressed." << endl;
    if (client_socket != nullptr)
    {
        client_socket->sendScalar<int32_t>(ERROR_RECEIVE);
        client_socket->close();
    }
    DELETE(client_socket);
    server_socket->close(); DELETE(server_socket);
    exit(0);
}

void handle_client()
{
    uint32_t i = 0, l = 0, client_read_len;

    // get info
    ALARM_ATOMIC(client_read_len = client_socket->receiveScalar<uint32_t>(), TIMEOUT)
    ALARM_ATOMIC(
        string *client_read = client_socket->receiveString(client_read_len),
        TIMEOUT
    );
    ALARM_ATOMIC(i = client_socket->receiveScalar<uint32_t>(), TIMEOUT)
    ALARM_ATOMIC(l =  client_socket->receiveScalar<uint32_t>(), TIMEOUT)
    printf("String: %s\ni = %u\nl = %u\n", client_read->c_str(), i, l);

    // invalid protocol
    if (client_read_len != client_read->length())
    {
        cout << "ERROR_RECEIVE" << endl;
        client_socket->sendScalar<int32_t>(ERROR_RECEIVE);
    }

    // string overflow happened
    if ((i + l) > client_read_len)
    {
        cout << "ERROR_OVERFLOW" << endl;
        client_socket->sendScalar<int32_t>(ERROR_OVERFLOW);
    }
    else // success
    {
        cout << "Sending data" << endl;
        string client_send = client_read->substr(i, l);
        client_socket->sendScalar<int32_t>(client_send.length());
        client_socket->sendString(client_send);
    }

    client_socket->close(); DELETE(client_socket);
    DELETE(server_socket);
    DELETE(client_read);
    exit(0);
}

int main()
{
    signal(SIGINT, int_handle);
    signal(SIGALRM, alarm_handle);
    server_socket = new SafeSocketTCP();
    server_socket->open()
            ->setAddress(PORT)
            ->setListenConnections(MAX_CLIENTS)
            ->setCloseSocket(false)
            ->setOptionReuseAddress()
            ->bind()
            ->listen();

    cout << "Server: started to listen" << endl;
    while (true)
    {
        client_socket = server_socket->accept();
        client_socket->setCloseSocket(false);
        printf("Client %s:%d\n", client_socket->getIPString().c_str(), client_socket->getPort());

        // parent closes the server, child closes the clients
        if (safe_fork() == 0) // child
        {
            handle_client();
        }
        DELETE(client_socket); // fork copies memory, delete local instance
        // parent goes on
    }

    server_socket->close();
    DELETE(server_socket);
    return 0;
}

