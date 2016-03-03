#include "../os.h"

typedef struct msg
{
    long   type; // type 1 is number, type 2 is string
    int    nr;
    char   string[256];
} msg_t;

int id;

void cleanup(int s)
{
    if (msgctl(id, IPC_RMID, NULL) == -1)
    {
        perror("error: remove queue:");
        exit(1);
    }
}

int main()
{
    srand(time(NULL));

    signal(SIGINT, cleanup);


    int count = 10;
    key_t key = safe_ftok(".", 1);
    size_t length = sizeof(msg_t) - sizeof(long);
    msg_t m;

    if ((id = msgget(key, 0600 | IPC_CREAT)) == -1)
    {
        perror("msgget");
        exit(1);
    }

    while (count)
    {
        int nr = rand() % 10000;
        char string[256];
        printf("Give string: ");
        scanf("%s", string ); // string without spaces

        // send number
        m.type = 1;
        m.nr = nr;

        if (msgsnd(id, &m, length, 0) == -1)
        {
            perror("msgsnd");
            exit(1);
        }
        printf("Sent nr\n");

        // send string
        m.type = 2;
        m.nr = -1;
        strcpy(m.string, string);

        if (msgsnd(id, &m, length, 0) == -1)
        {
            perror("msgsnd");
            exit(1);
        }
        printf("Sent string\n");


        printf("Sleeping \n\n");
        sleep(10);
        count--;
    }

    cleanup(0);

    return 0;
}
