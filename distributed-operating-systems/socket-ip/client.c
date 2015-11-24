#include "../os.h"

#define PORT 35000

int main()
{
    int sd = safe_socket_tcp();
    struct sockaddr_in adress;
    adress.sin_family = AF_INET;
    adress.sin_port = htons(PORT);
    adress.sin_addr.s_addr = INADDR_ANY; // bind to all ips

    safe_connect(sd, &adress);

    printf("Sent connection\n");
    write_int(sd, 25);

    safe_close(sd);

    return 0;
}
