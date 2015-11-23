#include "os.h"
#include <pthread.h>

#define PORT 45000
#define MAX_CLIENTS 2

int semid;

void set_value(int new_value)
{
    struct sembuf op;
    op.sem_num = 0; // a single semaphore
    op.sem_op = new_value; // increment by this value
    op.sem_flg = 0;

    if (semop(semid, &op, 1 /* the number of elements in the buffer in op*/) < 0)
    {
        perror("semop");
        exit(1);
    }
}

void lock_sem()
{
    set_value(-1);
}

void unlock_sem()
{
    set_value(1);
}

void* thread_main(void *arg)
{
    int cd = *((int *)arg), read_n;

    char *buffer = malloc(100);
    if ((read_n = read(cd, buffer, 100)) < 0)
    {
        perror("could not read");
    }
    else
    {
        buffer[read_n] = 0;
        printf("Received: %s\n", buffer);
    }

    free(arg);
    free(buffer);
    return NULL;
}

int main()
{
    int sd = socket(AF_INET, SOCK_STREAM, 0);
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

    if (listen(sd, MAX_CLIENTS) < 0)
    {
        perror("listen");
        exit(1);
    }

    // create semaphore
    key_t key = safe_ftok("/home/dan/dev/os", 1);
    semid = semget(key, 1, IPC_CREAT | 0600);

    while (1)
    {
        struct sockaddr_in client_addr;
        socklen_t client_size = sizeof(client_addr);
        int cd = accept(sd, (struct sockaddr *)&client_addr, &client_size);
        if (cd < 0)
        {
            perror("accept");
            exit(1);
        }

        // thread
        pthread_t id_thread;
        int *temp_arg = malloc(sizeof(int));
        *temp_arg = cd;
        pthread_create(&id_thread, NULL, thread_main, temp_arg);

    }

    close(sd);

    return 0;
}
