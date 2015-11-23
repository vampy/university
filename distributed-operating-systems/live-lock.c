#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>
#include <unistd.h>
#include <errno.h>

#define N 2
pthread_mutex_t mutex;
pthread_mutex_t mutex2;

void *func(void *arg)
{
    while (1)
    {
        pthread_mutex_lock(&mutex);
        usleep(100);
        if (pthread_mutex_trylock(&mutex2) == 0)
        {
            printf("Thread 1 surpassed\n");
            pthread_mutex_unlock(&mutex);
            pthread_mutex_unlock(&mutex2);
            break;
        }

        printf("Thread 1 waiting for thread 2\n");
        pthread_mutex_unlock(&mutex);
    }

    return NULL;
}

void *func2(void *arg)
{
    while (1)
    {
        pthread_mutex_lock(&mutex2);
        //usleep(100);
        if (pthread_mutex_trylock(&mutex) == 0)
        {
            printf("Thread 2 surpassed\n");
            pthread_mutex_unlock(&mutex);
            pthread_mutex_unlock(&mutex2);
            break;
        }

        printf("Thread 2 waiting for thread 1\n");
        pthread_mutex_unlock(&mutex2);
    }

    return NULL;
}

int main(int argc, char *argv[])
{
    int i;
    pthread_t thr[N];

    pthread_create(&thr[0], NULL, func, NULL);
    pthread_create(&thr[1], NULL, func2, NULL);

    pthread_mutex_init(&mutex, NULL);
    pthread_mutex_init(&mutex2, NULL);

    // clean
    for (i = 0 ; i < N ; i++)
    {
        pthread_join(thr[i] , NULL) ;
    }

    return 0;
}
