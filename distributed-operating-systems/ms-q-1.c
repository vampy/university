#include "os.h"
#include "math.h"

#define PATH "/home/dan/dev/os/"

typedef struct msg
{
    long   type;
    int    n;
} msg_t;
int id;
msg_t m;

void routine(int s)
{
    printf("SIGINT was called\n");

    //exit(0);
    if (msgctl(id, IPC_RMID, 0) == -1)
    {
        perror("msgctl");
    }
    printf("Queue was removed\n");
    exit(0);
}

int main()
{
    key_t key = safe_ftok(PATH, 1);
    m.type = rand_interval(1, 10);
    m.n = 42;

    signal(SIGINT, routine);

    if ((id = msgget(key, 0600 | IPC_CREAT)) == -1)
    {
        perror("msgget");
        exit(1);
    }

    if (msgsnd(id, &m, sizeof(int), 0) == -1)
    {
        perror("msgsnd");
        exit(1);
    }

    int r = 1;
    while (r != -1)
    {
        if ((r = msgrcv(id, &m, sizeof(int), 0, 0)) == -1)
        {
            perror("msgrcv");
            exit(1);
        }
        printf("Got a message with type = %lu, and int = %d\n", m.type, m.n);
    }

    return 0;
}
