/*
XO with the same client
*/

#include "server.h"
#include <pthread.h>

#define PORT 37000
#define MAX_BUFFER 512
#define MAX_CLIENTS 16

void *thread_play_xo(void *arg)
{
    int client_socket = *((int *)arg);

    // start table
    table_t client_table;
    int value = rand() % 2;
    write_int(client_socket, value);

    // start game
    while (true)
    {
        int modified_pos = table_put_rand_pos(&table, value), row, col;
        write_int(client_socket, modified_pos);

        // from client
        read_int(client_socket, &modified_pos);
    }

    safe_close(client_socket);
    free(arg);
    return NULL;
}

int main()
{
    srand(time(NULL));
    int sd = safe_socket_tcp();

    struct sockaddr_in adress;
    adress.sin_family = AF_INET;
    adress.sin_port = htons(PORT);
    adress.sin_addr.s_addr = INADDR_ANY; // bind to all ips

    safe_bind(sd, &adress);
    safe_listen(sd, MAX_CLIENTS);

    while (true)
    {
        struct sockaddr_in client_address;
        char *ip;
        int cd = safe_accept(sd, &client_address);

        printf("Received connection\n");
        pthread_t id_thread;
        int *temp_arg = malloc(sizeof(int));
        *temp_arg = cd;
        pthread_create(&id_thread, NULL, thread_play_xo, temp_arg);
    }

    safe_close(sd);

    return 0;
}
