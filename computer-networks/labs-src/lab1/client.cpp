#include "../os.hpp"
#include "../tcp.hpp"

enum {ERROR_RECEIVE = -1, ERROR_OVERFLOW = -2};
static const uint16_t PORT = 23233;
static const uint16_t TIMEOUT = 10;
SafeSocketTCP *server_socket = nullptr;
using namespace std;

void alarm_handle(int semnal)
{
    cout << "Timed out with the server." << endl;
    DELETE(server_socket);
    exit(1);
}

int main()
{
    signal(SIGALRM, alarm_handle);
    server_socket = new SafeSocketTCP();
    server_socket->open()
            ->setAddress(PORT)
            ->connect();

    // read info
    string client_read;
    uint32_t i = 0, l = 0;
    cout << "Read string: ";
    if (!getline(std::cin, client_read))
    {
        perror("failed to read line");
        exit(EXIT_FAILURE);
    }
    cout << "i = "; cin >> i;
    cout << "l = "; cin >> l;

    // send info
    server_socket->sendScalar<uint32_t>(client_read.length());
    server_socket->sendString(client_read);
    server_socket->sendScalar<uint32_t>(i);
    server_socket->sendScalar<uint32_t>(l);

    // get response
    ALARM_ATOMIC(int32_t status = server_socket->receiveScalar<int32_t>(), TIMEOUT);
    if (status == 0)
    {
        cout << "String is empty" << endl;
    }
    else if (status > 0)
    {
        ALARM_ATOMIC(string *client_receive = server_socket->receiveString(status), TIMEOUT)

        cout << "Received: " << client_receive->c_str() << endl;
        delete client_receive;
    }
    else if (status == ERROR_RECEIVE)
    {
        cout << "Error: on sending data to server" << endl;
    }
    else if (status == ERROR_OVERFLOW)
    {
        cout << "Error: string overflow" << endl;
    }
    else
    {
        cout << "Unknown error code: " << status << endl;
    }

    DELETE(server_socket);
    return 0;
}
