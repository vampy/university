// SEE socket-hw-server.c
#include "os.h"

#define PORT 47000
#define MAX_BUFFER 4096
#define FILENAME "socket-hw-send.txt"

int main(int argc, char *argv[])
{
    // get file from user
    if (argc < 2)
    {
        printf("Program usage: %s <filename>", argv[0]);
        exit(EXIT_FAILURE);
    }
    char *file_to_send = argv[1];
    if (!file_exists(file_to_send))
    {
        printf("File: '%s' does not exist", file_to_send);
        exit(EXIT_FAILURE);
    }

    int server_socket = safe_socket_udp();
    struct sockaddr_in server_address;
    server_address.sin_family = AF_INET;
    server_address.sin_port = htons(PORT);
    server_address.sin_addr.s_addr = INADDR_ANY; // bind to all ips

    char buffer[MAX_BUFFER];
    FILE *fp = safe_fopen(file_to_send, "r");

    while (fgets(buffer, sizeof(buffer), fp) != NULL)
    {
        ssize_t sent_size = safe_sendto(server_socket, buffer, strlen(buffer), &server_address);
        printf("Sent = %zu bytes\n", sent_size);
        usleep(1000 * 500); // sleep 500 ms
    }

    // check if we close the file correctly
    if (!feof(fp) || ferror(fp))
    {
        perror("fgets()");
    }

    safe_close(server_socket);
    safe_fclose(fp);

    return 0;
}
