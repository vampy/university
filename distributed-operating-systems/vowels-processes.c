#include "os.h"

/*
 P1 citeste il trimite la P2
 P2 determina numearul de vocale, trimite la P1
*/

#define MAX_STRING 256

#define FIFO_A "a2b"
#define FIFO_B "b2a"

int main(int argc, char *argv[])
{
    // B is main
    safe_mkfifo(FIFO_A, 0666);
    safe_mkfifo(FIFO_B, 0666);

    pid_t pid_b = safe_fork();
    if (pid_b == 0)
    {
        // P2 aka b process
        char string[MAX_STRING];
        int fh_write, fh_read, length, i, nr_vowels;

        fh_read = safe_open(FIFO_A, O_RDONLY);
        read_string(fh_read, string);

        //printf("P2: string = %s\n", string);

        // calculate the number of vowels
        for (length = strlen(string), i = 0, nr_vowels = 0; i < length; i++)
        {
            if (strchr("aeiou", string[i]) != NULL)
            {
                nr_vowels++;
            }
        }

        // send back to P1
        fh_write = safe_open(FIFO_B, O_WRONLY);
        write_int(fh_write, nr_vowels);

        close(fh_read);
        close(fh_write);
        exit(EXIT_SUCCESS);
    }

    // P1 process, main
    char string[MAX_STRING];
    int fh_write, fh_read, nr_vowels;

    // read string and send
    fh_write = safe_open(FIFO_A, O_WRONLY);
    printf("Give string: "); scanf("%s", string);
    write_string(fh_write, string);

    // receive, string
    fh_read = safe_open(FIFO_B, O_RDONLY);
    read_int(fh_read, &nr_vowels);

    printf("P1: nr of vowels = %d\n", nr_vowels);

    wait(0); // wait for B

    // clean
    close(fh_read);
    close(fh_write);
    unlink(FIFO_A);
    unlink(FIFO_B);

    return EXIT_SUCCESS;
}
