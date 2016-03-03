#include "../os.h"

int main(int argc, char *argv[])
{
    srand(time(NULL));
    if (argc != 3)
    {
        printf("Usage: %s <ip> <port>", argv[0]);
        return 1;
    }

    int sd = safe_socket_tcp(), port = atoi(argv[2]);
    struct sockaddr_in adress;
    adress.sin_family = AF_INET;
    adress.sin_port = htons(port);
    adress.sin_addr.s_addr = inet_addr(argv[1]); // bind to all ips

    safe_connect(sd, &adress);
    printf("Connection\n");
    int value;
    read_int(sd, &value);
    value = value == 0 ? 1 : 0;

    // start game
    while (true)
    {
        int modified_pos, row, col;
        read_int(sd, &modified_pos);
        row = modified_pos / 3;
        col = modified_pos % 3;
    }

    safe_close(sd);

    return 0;
}
