/* Create N threads */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>
#include <unistd.h>

#define N 10

void *func(void *arg)
{
    int i = *((int *)arg);

    usleep(i * 1000);
    printf("Thread %d finished\n", );

    free(arg);
    return NULL;
}

int main(int argc, char *argv[])
{
    int i;
    pthread_t thr[N];

    for (i = 0; i < N; i++)
    {
        int *temp_i = malloc(sizeof(int));
        *temp_i = i;
        pthread_create(&thr[i], NULL, func, temp_i);
    }

    // clean
    for (i = 0; i < N; i++)
    {
        pthread_join(thr[i] , NULL) ;
    }

    return 0;
}
