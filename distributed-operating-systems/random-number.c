#include "os.h"
#include "math.h"

/*
 * Genereaza un numar aleator intre 100 si 1000
 * il trimite le celelalal
 * daca numarul este mai mare decat 0 scade o valodare intre 10 si 20 random
 * il trimite inapoi
 * la fel si celalalt
*/
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
