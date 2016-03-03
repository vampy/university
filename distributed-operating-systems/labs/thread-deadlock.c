/*
 * Create a deadlock
 * sudo gdb -p <id>
 *         thread apply all bt
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
    pthread_mutex_lock(&mutex);
    usleep(100);
    pthread_mutex_lock(&mutex2);
    printf("Thread 1 finished\n");
    return NULL;
}

void *func2(void *arg)
{
    pthread_mutex_lock(&mutex2);
    usleep(100);
    pthread_mutex_lock(&mutex);

    printf("Thread 2 finished\n");
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

    // clean
    for (i = 0; i < N; i++)
    {
        pthread_join(thr[i] , NULL) ;
    }
    pthread_mutex_destroy(&mutex);
    pthread_mutex_destroy(&mutex2);

    return 0;
}
