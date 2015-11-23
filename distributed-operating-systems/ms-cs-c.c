// instante de client si de server
// clientul trimite un numari
// serverul intoarce daca numarul par sau impar
// 0 daca e par si 1 dace impar
// mai multe instante de client si server
// different priority
// send with priority 1
// in message we put our processid
// server: send back with the processid

#include "os.h"
#include "math.h"

#define PATH "/home/dan/dev/os/"

typedef struct msg
{
    long   type;
    int    nr;
    pid_t  pid;
} msg_t;

int main()
{
    int id;
    size_t length = sizeof(int) + sizeof(pid_t);
    msg_t m;
    key_t key = safe_ftok(PATH, 1);

    if ((id = msgget(key, 0600)) == -1)
    {
        perror("msgget");
        exit(1);
    }

    m.type = 1;
    m.pid = getpid();
    printf("Number = "); scanf("%d", &(m.nr));

    if (msgsnd(id, &m, length, 0) == -1)
    {
        perror("msgsnd");
        exit(1);
    }
    printf("Client: sent number\n");

    if (msgrcv(id, &m, length, getpid(), 0) == -1)
    {
        perror("msgrcv");
        exit(1);
    }

    printf("We got with type = %lu and number = %d\n", m.type, m.nr);

    return 0;
}
