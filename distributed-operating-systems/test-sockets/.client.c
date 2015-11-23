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

#define PORT 19001
#define MAX_BUFFER 1024

int main()
{
    int sd = socket(AF_INET, SOCK_STREAM, 0);
    if (sd == -1)
    {
        perror("Failed to create socket");
        exit(1);
    }
    struct sockaddr_in adress;
    adress.sin_family = AF_INET;
    adress.sin_port = htons(PORT);
    adress.sin_addr.s_addr = INADDR_ANY; // bind to all ips

    if (connect(sd, (struct sockaddr *)&adress, sizeof(adress)) < 0)
    {
        perror("failed to connect");
        exit(1);
    }

    char buffer[MAX_BUFFER] = "Test";

    write(sd, buffer, 4);
    //read(sd, buffer, )

    close(sd);

    return 0;
}
