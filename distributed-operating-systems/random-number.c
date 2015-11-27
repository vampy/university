/*
 * A generate a random number between 100 and 1000.
 * The process A sends the number to process B.
 * B verifies if the number is >= 0 then it subtracts the value between 10 and 20
 * B sends the number back.
 * B does the same thing as A, only opposite to each other.
 */

#include "os.h"
#include "math.h"

int main(int argc, char *argv[])
{
    rand_seed();

    int a2b[2], b2a[2];
    pid_t id_a, id_b;

    safe_pipe(a2b);
    safe_pipe(b2a);

    // process a
    id_a = safe_fork();
    if (id_a == 0)
    {
        // init, send first number
        int nr = rand_interval(100, 1000);

        write_int(a2b[W], nr);
        //printf("From a, random number is %d\n", nr);

        while (1)
        {
            int nr;
            read_int(b2a[R], &nr);

            if (nr <= 0)
            {
                printf("From a: %d\n", nr);
                nr = 0;  write_int(a2b[W], nr);
                exit(EXIT_SUCCESS);
            }

            nr -= rand_interval(10, 20);
            // send back to b
            write_int(a2b[W], nr);

            printf("From a: %d\n", nr);
        }

        exit(EXIT_SUCCESS);
    }

    // process b
    id_b = safe_fork();
    if (id_b == 0)
    {
        while (1)
        {
            int nr;
            read_int(a2b[R], &nr);

            if (nr <= 0)
            {
                printf("From b: %d\n", nr);
                nr = 0;  write_int(b2a[W], nr);
                exit(EXIT_SUCCESS);
            }

            nr -= rand_interval(10, 20);
            // send back to b
            write_int(b2a[W], nr);

            printf("From b: %d\n", nr);
        }
    }

    wait(0);
    wait(0);

    // clean
    close_pipe(a2b);
    close_pipe(b2a);

    return 0;
}
