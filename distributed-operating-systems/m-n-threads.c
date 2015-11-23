#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>
#include <unistd.h>
#include <semaphore.h>

sem_t sem;

void *thread_main(void *arg)
{
    int i = *((int *)arg), j;

    sem_wait(&sem);
    for (j = 0; j < 10; j++)
    {
        usleep(i * 100000);
        printf("%d\n", i);
    }
    sem_post(&sem);

    free(arg);
    return NULL;
}

int main(int argc, char *argv[])
{
    int n, m, i;
    scanf("%d %d", &n, &m);

    pthread_t thr[n];
    sem_init(&sem, 0, m);

    for (i = 0 ; i < n ; i++)
    {
        int *temp_i = malloc(sizeof(int));
        *temp_i = i;
        pthread_create (&thr[i], NULL, thread_main, temp_i);
    }

    for (i = 0 ; i < n ; i++)
    {
        pthread_join(thr[i] , NULL) ;
    }

    return EXIT_SUCCESS;
}
