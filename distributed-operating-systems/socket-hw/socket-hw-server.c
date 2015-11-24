// Tema: un client si un server the udp
// clientul o sa ii dam un nume de fisier
// si o sa il trimita linie cu linie la server
// serverul o sa ia linia si o sa faca append la un fisier

// il testam cu 2 clients
// si fiecare client are 500ms delay
#include "../os.h"

#define PORT 47000
#define MAX_BUFFER 4096
#define FILENAME "socket-hw-append.txt"

int main()
{
    int server_socket = safe_socket_udp();
    struct sockaddr_in server_address;
    server_address.sin_family = AF_INET;
    server_address.sin_port = htons(PORT);
    server_address.sin_addr.s_addr = INADDR_ANY; // bind to all ips

    safe_bind(server_socket, &server_address);
    FILE *fp = safe_fopen(FILENAME, "a");

    // receive clientq
    while (true)
    {
        char buffer[MAX_BUFFER];
        struct sockaddr_in client_address;

        ssize_t received_size = safe_recvfrom(server_socket, buffer, MAX_BUFFER - 1, &client_address);
        buffer[received_size] = 0;

        printf("Received = %zu bytes\n", received_size);
        fprintf(fp, "%s", buffer);
        fflush(fp);
    }

    safe_close(server_socket);
    safe_fclose(fp);

    return 0;
}
