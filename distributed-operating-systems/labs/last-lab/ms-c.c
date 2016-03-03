/*
 * With message queues.
 *
 * A produces puts 2 type of messages in the queue:
 * - a random number
 * - a string read from the keyboard
 * It does this every 10 seconds.
 *
 * The consumer:
 * - if the number in the first message is even, it display the string
 * - if the number is odd it displays the string reversed
 */

#include "../os.h"

typedef struct msg
{
    long   type;
    int    nr;
    char   string[256];
} msg_t;


void string_reverse(char *string, int len)
{
    int i;

    int last_pos = len-1;
    for(i = 0; i < len/2; i++)
    {
        char tmp = string[i];
        string[i] = string[last_pos - i];
        string[last_pos - i] = tmp;
    }
}

int main()
{
    int id;
    key_t key = safe_ftok(".", 1);
    size_t length = sizeof(msg_t) - sizeof(long);
    msg_t m;

    while (1)
    {
        int nr;
        char string[256];

        if ((id = msgget(key, 0600)) == -1)
        {
            perror("msgget");
            exit(1);
        }
        printf("Id = %d\n", id);

        // get number
        if (msgrcv(id, &m, length, 1, 0) == -1)
        {
            perror("msgrcv");
            exit(1);
        }
        nr = m.nr;
        printf("Got nr = %d\n", nr);

        // get string
        if (msgrcv(id, &m, length, 2, 0) == -1)
        {
            perror("msgrcv");
            exit(1);
        }
        strcpy(string, m.string);

        if (nr % 2 == 0)
        {
            printf("Got string = %s\n", string);
        }
        else // reverse
        {
            string_reverse(string, strlen(string));
            printf("Got string = %s\n", string);
        }

        printf("\n");
    }


    return 0;
}
