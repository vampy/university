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

    while(true)
    {
        if ((id = msgget(key, 0600 | IPC_CREAT)) == -1)
        {
            perror("msgget");
            exit(1);
        }

        if (msgrcv(id, &m, length, 1, 0) == -1)
        {
            perror("msgrcv");
            exit(1);
        }
        printf("Server: received number %d\n", m.nr);
        m.type = m.pid; // send to the specific client
        m.nr = m.nr % 2;

        if (msgsnd(id, &m, length, 0) == -1)
        {
            perror("msgsnd");
            exit(1);
        }
    }

    return 0;
}
