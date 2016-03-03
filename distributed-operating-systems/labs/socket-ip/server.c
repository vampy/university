/*
 * We have a client and server that use UDP
 * The server should not allow connection from clients with the same IP address.
 * The client sends a number and the server adds it to the global sum.
 */

#include "../os.h"

#define PORT 35000
#define MAX_BUFFER 512

int main()
{
    int sd = safe_socket_tcp();
    int sum;
    char *all_ips[MAX_BUFFER];
    char all_ips_len = 0;

    struct sockaddr_in adress;
    adress.sin_family = AF_INET;
    adress.sin_port = htons(PORT);
    adress.sin_addr.s_addr = INADDR_ANY; // bind to all ips

    safe_bind(sd, &adress);
    safe_listen(sd, 2);

    while (true)
    {
        struct sockaddr_in client_address;
        char *ip;
        int cd = safe_accept(sd, &client_address), i, cont = 0;

        printf("Received connection\n");

        // found ip
        ip = inet_ntoa(client_address.sin_addr);
        for (i = 0; i < all_ips_len; i++)
        {
            if (strcmp(all_ips[i], ip) == 0)
            {
                cont = 1;
                break;
            }
        }
        if (cont == 1)
        {
            printf("Rejected connection\n");
            safe_close(cd);
            continue;
        }

        // add to sum
        int client_number;
        read_int(cd, &client_number);
        sum += client_number;

        // add to ips pool
        printf("Got connection from %s\n", ip);
        char *put_ip = strdup(ip);
        all_ips[all_ips_len++] = put_ip;

        safe_close(cd);
    }

    safe_close(sd);

    return 0;
}
