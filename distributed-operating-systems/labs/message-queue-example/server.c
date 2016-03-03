/*
 * A client and a server.
 * The client sends a number.
 * The server returns if the number is even or odd.
 * 0 if even or 1 if odd.
 * Multiple instances of the client and server. Every one has a different priority.
 * Client sends with priority 1 and in the message we put our process id.
 * The server sets the type to the client process id.
 */
#include "../os.h"
#include "../math.h"

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
