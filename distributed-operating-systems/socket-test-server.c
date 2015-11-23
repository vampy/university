#include "os.h"

#define PORT 27000
#define MAX_BUFFER 512

int main()
{
    int sd = safe_socket_tcp();
    struct sockaddr_in adress;
    adress.sin_family = AF_INET;
    adress.sin_port = htons(PORT);
    adress.sin_addr.s_addr = INADDR_ANY; // bind to all ips

    safe_bind(sd, &adress);
    safe_listen(sd, 2);

    struct sockaddr_in cdddr;
    int cd = safe_accept(sd, &cdddr);

    printf("Received connection\n");
    char buffer[MAX_BUFFER] = "socket-test-server STRING";
    write_string(cd, buffer);

    safe_close(sd);
    safe_close(cd);

    return 0;
}
