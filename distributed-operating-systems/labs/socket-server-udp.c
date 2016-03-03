#include "os.h"

#define PORT 44000
#define MAX_CLIENTS 2
#define BUFF_SIZE  512

int main()
{
    int sd = socket(AF_INET, SOCK_DGRAM, 0);
    if (sd < 0)
    {
        perror("socket");
        exit(1);
    }
    struct sockaddr_in server_addr;
    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(PORT);
    server_addr.sin_addr.s_addr = INADDR_ANY; // bind to all ips

    if (bind(sd, (struct sockaddr *)&server_addr, sizeof(server_addr)) < 0)
    {
        perror("bind");
        exit(1);
    }

    while (true)
    {
        char buffer[BUFF_SIZE];
        struct sockaddr_in client_addr;
        socklen_t client_addr_size = sizeof(client_addr);

        ssize_t client_size = recvfrom(sd, buffer, BUFF_SIZE - 1, 0, (struct sockaddr *)&client_addr, &client_addr_size);
        buffer[client_size] = 0;

        printf("Received: %s\n", buffer);
    }

    close(sd);
    return 0;
}
