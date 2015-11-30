// Client uses type 1
#include "../os.h"
#include "../math.h"

#define PATH "/home/dan/dev/os/"
#define TYPE_CLIENT 1 // the type of message sent by client

typedef struct msg
{
    long   type;
    long   type_receive; // send to a specific client, set the type with it
    float  nr;
    float  result;
} msg_t;

int main()
{
    int id;
    size_t length = sizeof(msg_t) - sizeof(long);
    key_t key = safe_ftok(PATH, 1);
    pid_t receive_type = getpid();

    if ((id = msgget(key, 0600)) == -1)
    {
        perror("msgget");
        exit(1);
    }

    msg_t message;
    message.type = TYPE_CLIENT;
    message.type_receive = receive_type;
    printf("Number = "); scanf("%f", &(message.nr));
    if (msgsnd(id, &message, length, 0) == -1)
    {
        perror("msgsnd");
        exit(1);
    }
    printf("Sent number\n");

    if (msgrcv(id, &message, length, receive_type, 0) == -1)
    {
        perror("msgrcv");
        exit(1);
    }

    printf("We got result = %f\n", message.result);

    return 0;
}
