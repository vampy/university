/*
 * Create a vector of 10 elements and 10 threads. Every threads gets the argument v[i] and displays the value at position
 * i in a file (it will wait for the other threads to finish), the content of the file will be 0123456789.
 */

#include <stdio.h>
#include <time.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>

#define N 10
#define SIG_LOCK 1
#define SIG_UNLOCK 0

char *filename = "duca.txt";

// create custom signals
unsigned short signal[N]; // 0 unlocked, 1 locked

void *write(void *arg)
{
    int nr = *((int *)arg), i;
    //printf("Got: %d\n", nr);

    // wait for the others until now
    for(i = 0; i < nr; i++)
    {
        // wait for indivdual signal
        while(signal[i] == SIG_LOCK) {}
    }

    // write to file
    FILE *handle = fopen(filename, "a");
    fprintf(handle, "%d", nr);
    fclose(handle);

    // unlock the current signal, for the others to use
    signal[i] = SIG_UNLOCK;

    return NULL;
}

int main()
{
    int v[N] = {4, 1, 5, 7, 9, 2, 8, 3, 6, 0}, i;
    pthread_t thr[N];
    srand(time(NULL));

    // create file
    FILE *handle = fopen(filename, "w");
    fclose(handle);

    for(i = 0; i < N; i++)
    {
        //printf("Sent: %d\n", v[i]);
        // create custom signals
        signal[i] = SIG_LOCK; // lock

        pthread_create(&thr[i], NULL, write, &v[i]);
    }

    // clean up
    for(i = 0; i < N; i++)
    {
        pthread_join(thr[i], NULL);
    }

    return 0;
}
