/*
 * 2 programs
 * Program A:
 * - creates a segment of 100 bytes
 * - every second it sets a random byte
 *
 * Program B:
 * - every 3 seconds verifies 2 bytes, at least
 * - displays all the bytes that repeat themselves
 */

#include "../os.h"
#include "../math.h"

int main()
{
    int id, i = 0, isize = 100;
    char *data;
    key_t key = safe_ftok(".", 1);


    if ((id = shmget(key, isize, 0600 | IPC_CREAT)) == -1)
    {
        perror("shmget");
        exit(1);
    }
    printf("Created: id = %d\n", id);


    data = (char *)shmat(id, NULL, 0);
    if (*((int *)data) == -1)
    {
        perror("shmat: ");
        exit(1);
    }
    data[isize] = 0;
    while (1)
    {
        if (i == isize)
        {
            i = 0;
        }

        data[i] = rand_interval(97, 122);

        i++;
        sleep(1);
    }

    /* detach from the segment: */
    if (shmdt(data) == -1)
    {
        perror("shmdt");
        exit(1);
    }

    return 0;
}
