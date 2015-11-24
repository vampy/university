/*
Avem producator si consumator
consumatorul pune un numar
producator ii intoarce radical din numar
*/

#include "../os.h"
#include "../math.h"

#define PATH "/home/dan/dev/os/"
#define TYPE_CLIENT 1 // the type of message sent by client

typedef struct msg
{
    long   type;
    long   type_receive; // send to a specific client with tha type
    float  nr;
    float  result;
} msg_t;

int main()
{
    int id;
    size_t length = sizeof(msg_t) - sizeof(long);
    msg_t message;
    key_t key = safe_ftok(PATH, 1);

    while(true)
    {
        if ((id = msgget(key, 0600 | IPC_CREAT)) == -1)
        {
            perror("msgget");
            exit(1);
        }

        if (msgrcv(id, &message, length, TYPE_CLIENT, 0) == -1)
        {
            perror("msgrcv");
            exit(1);
        }
        printf("Server: received number %f\n", message.nr);

        message.type = message.type_receive; // send to the specific client
        message.result = sqrtf(message.nr);
        if (msgsnd(id, &message, length, 0) == -1)
        {
            perror("msgsnd");
            exit(1);
        }
    }

    return 0;
}
