#include "../os.hpp"
#include "../udp.hpp"
#include <fstream>

using namespace std;

static const uint16_t PORT = 47060;
static SafeSocketUDP *server_socket = nullptr;
static char *buffer = nullptr;
static ofstream output;

void clean()
{
    if (output)
    {
        output.close();
    }
    DELETE(server_socket);
    DELETE_ARRAY(buffer);
}

void int_handle(int signal)
{
    cout << "CTRL + C pressed." << endl;
    clean();
    exit(0);
}

int main(int argc, char *argv[])
{
    // parse arguments
    if (argc != 3)
    {
        printf("Usage: %s <filename to transfer> <nr_bytes>\n", argv[0]);
        return 1;
    }
    string file_name(argv[1]);
    int32_t read_bytes = stol(string(argv[2]));
    cout << "file_name = " << file_name << ", read_bytes = " << read_bytes << endl;
    assert(read_bytes > 0);

    signal(SIGINT, int_handle);
    server_socket = new SafeSocketUDP();
    server_socket->open()
            ->setAddress(PORT); // send to this address and receive at this address

    // send filename
    server_socket->sendScalar<int32_t>(file_name.length());
    server_socket->sendString(file_name);

    // get file size
    int32_t file_size = server_socket->receiveScalar<int32_t>();
    cout << "file_size = " << file_size << endl
         << "From = " << server_socket->getFromIPString() << ":" << server_socket->getFromPort()
         << endl;
    if (file_size == -1)
    {
        cout << "Error: filename=" << file_name << " does not exist on the server" << endl;
        clean();
        return 1;
    }

    // receive file
    cout << "Receiving file " << file_name << endl;
    string file_name_output = "client_" + file_name;
    int32_t read_file_size = 0;

    output.open(file_name_output, ios::binary | ios::out);
    assert(output.is_open());

    buffer = new char[read_bytes + 1];
    while (output.is_open() && read_file_size < file_size)
    {
        int32_t read_size = server_socket->receiveCharString(buffer, read_bytes);
        cout << "Receiving: " << read_size << endl;

        output.write(buffer, read_size);
        read_file_size += read_size; // increase number of sent bytes
    }
    cout << "read_file_size = " << read_file_size << endl;

    clean();
    return 0;
}
