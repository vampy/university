/*
 * Create a thread starvation
 * Thread 1 print 10 times
 * Thread 2 prints once
 * Intercalate functions, every thread function locks itself and unlocks the other one at the end
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>
#include <unistd.h>

#define N 2

pthread_mutex_t mutex;
pthread_mutex_t mutex2;

void *func(void *arg)
{
    while (1)
    {
        pthread_mutex_lock(&mutex);
        int i;

        for (i = 1; i <= 5; i++)
        {
            printf("Thread 1 - %d\n", i);
        }
        sleep(1);
        pthread_mutex_unlock(&mutex2);
    }

    return NULL;
}

void *func2(void *arg)
{
    while (1)
    {
        pthread_mutex_lock(&mutex2);

        printf("Thread 2 finished\n");
        sleep(1);
        pthread_mutex_unlock(&mutex);
    }

    return NULL;
}

int main(int argc, char *argv[])
{
    int i;
    pthread_t thr[N];
    pthread_mutex_init(&mutex, NULL);
    pthread_mutex_init(&mutex2, NULL);

    pthread_create(&thr[0], NULL, func, NULL);
    pthread_create(&thr[1], NULL, func2, NULL);

    // start the first thread first
    pthread_mutex_lock(&mutex2);

    // clean
    for (i = 0 ; i < N ; i++)
    {
        pthread_join(thr[i] , NULL) ;
    }
    pthread_mutex_destroy(&mutex);
    pthread_mutex_destroy(&mutex2);

    return 0;
}
