#include "../os.h"

#define PORT 27000
#define MAX_BUFFER 512

int main()
{
    int sd = safe_socket_tcp();
    struct sockaddr_in adress;
    adress.sin_family = AF_INET;
    adress.sin_port = htons(PORT);
    adress.sin_addr.s_addr = INADDR_ANY; // bind to all ips

    safe_connect(sd, &adress);

    printf("Sent connection\n");
    char buffer[MAX_BUFFER];
    read_string(sd, buffer);
    printf("got string from server: %s\n", buffer);


    safe_close(sd);

    return 0;
}
