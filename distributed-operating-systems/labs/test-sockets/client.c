#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <unistd.h>
#include <errno.h>
#include <time.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

#define PORT 19002
#define MAX_BUFFER 1024

int main(int argc, char *argv[])
{
    if (argc < 2)
    {
        printf("Usage: %s <string>\n", argv[0]);
        exit(1);
    }
    char *buffer = argv[1];
    int buffer_len = strlen(buffer);
    if (buffer_len > MAX_BUFFER)
    {
        perror("String is too long");
        exit(1);
    }

    int sd = socket(AF_INET, SOCK_STREAM, 0);
    if (sd < 0)
    {
        perror("failed to create socket");
        exit(1);
    }
    struct sockaddr_in adress;
    adress.sin_family = AF_INET;
    adress.sin_port = htons(PORT);
    adress.sin_addr.s_addr = INADDR_ANY;

    if (connect(sd, (struct sockaddr *)&adress, sizeof(adress)) < 0)
    {
        perror("Failed to connect");
        exit(1);
    }

    // send string
    if (write(sd, buffer, buffer_len) < 0)
    {
        perror("failed to write");
        close(sd);
        exit(1);
    }

    // read
    int read_n = read(sd, buffer, buffer_len);
    if (read_n == -1)
    {
        perror("failed to read");
        close(sd);
        exit(1);
    }
    if (read_n != buffer_len)
    {
        perror("Read a string of different size");
    }

    printf("got = %s\n", buffer);

    close(sd);
    return 0;
}
