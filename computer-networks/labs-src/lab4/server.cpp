#include "../os.hpp"
#include "../udp.hpp"
#include <fstream>

using namespace std;

static const uint16_t PORT = 47000;
static SafeSocketUDP *server_socket = nullptr;
static int32_t file_size = 0, write_bytes = 0;
static string *file_name = nullptr;

void int_handle(int signal)
{
    cout << "CTRL + C pressed." << endl;
    server_socket->close();
    DELETE(server_socket);
    exit(0);
}

void handle_client()
{

}

int main(int argc, char *argv[])
{
    // parse arguments
    if (argc != 2)
    {
        printf("Usage: %s <nr_bytes>\n", argv[0]);
        return 1;
    }
    write_bytes = stol(string(argv[1]));
    cout << "write_bytes = " << write_bytes << endl;
    assert(write_bytes > 0);

    signal(SIGINT, int_handle);
    server_socket = new SafeSocketUDP();
    server_socket->open()
            ->setCloseSocket(false)
            ->setAddress(PORT) // bind to this
            ->setOptionReuseAddress()
            ->bind();

    while (true)
    {
        // get filename to transfer back to client
        file_name = server_socket->receiveString(server_socket->receiveScalar<int32_t>());
        file_size = File::size(file_name->c_str());

        cout << "Filename = " << *file_name << endl
             << "From = " << server_socket->getFromIPString() << ":" << server_socket->getFromPort()
             << endl;

//        if (safe_fork() == 0)
//        {
//            handle_client();
//        }
        // send file size
        server_socket->setAddressAsFromAddress();
        server_socket->sendScalar<int32_t>(file_size);

        // file does not exist
        if (file_size == -1)
        {
            cout << "Error: file does not exist" << endl;
        }
        else
        {
            // send file
            cout << "Sending file " << *file_name << endl;

            ifstream input(*file_name, ios::binary | ios::in);
            assert(input.is_open());

            char *buffer = new char[write_bytes + 1];
            int32_t write_file_size = 0;
            while (input.good())
            {
                input.read(buffer, write_bytes);
                size_t read_size = input.gcount();

                cout << "Sending: " << read_size << endl;
                try
                {
                    write_file_size += server_socket->reliableSend(buffer, read_size);
                }
                catch (const runtime_error &e)
                {
                    cout << "runtime_error " << e.what() << endl;
                    break;
                }
            }
            delete [] buffer;
            cout << "write_file_size = " << write_file_size << endl;
            break;
        }

        cout << endl;
        DELETE(file_name);
        DELETE(server_socket);
    }

    server_socket->close();
    DELETE(file_name);
    DELETE(server_socket);
    return 0;
}
