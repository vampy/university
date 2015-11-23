// avem 2 procese a si b
// a citim numere de la tastatura
// daca numarul e par il trimitem la procesul b
// b afla radical trimite la a
// a la b pipe
// b la a fifo

#include "os.h"
#include "math.h"
#include "math.h"
#define FIFO_A "b2a"

int main(int argc, char *argv[])
{
    int a2b[2];

    file_delete_check(FIFO_A);
    safe_mkfifo(FIFO_A, 0666);
    safe_pipe(a2b);

    // A
    pid_t pid_a = safe_fork();
    if (pid_a == 0)
    {
        int fh_write, fh_read, n;
        fh_read = safe_open(FIFO_A, O_RDONLY); // a to b
        fh_write = a2b[W]; // b 2 a

        do
        {
            n = 0;

            printf("Nr = "); scanf("%d", &n);
            //printf("Send %d\n", n);

            if (n % 2 == 0 && n > 0)
            {
                write_int(fh_write, n);

                float fn;
                read(fh_read, &fn, sizeof(float));

                printf("A: The number sqrt %f\n", fn);
            }
        } while (n > 0);

        safe_close(fh_read);
        safe_close(fh_write);
    }

    // B
    int fh_write, fh_read, n;
    fh_write = safe_open(FIFO_A, O_WRONLY); // b to a
    fh_read = a2b[R];

    do
    {
        n = 0;

        read_int(fh_read, &n);
        //printf("Receive %d\n", n);

        float fn = sqrtf(n);

        printf("B: The number sqrt %f\n", fn);
        write(fh_write, &fn, sizeof(float));

    } while (n > 0);

    wait(0);
    close_pipe(a2b);
    safe_close(fh_read);
    safe_close(fh_write);
    file_delete_check(FIFO_A);

    return EXIT_SUCCESS;
}
