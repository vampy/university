/*
N threads
fiecare thread vrea sa gaseasca al 100 lea numar fibonacii
primul care a terminat (trebuie sa stim care e)
cand un thread isi da seama acesta zice ca a pierdut
*/
#include "os.h"
#include <pthread.h>

pthread_mutex_t finished_mutex = PTHREAD_MUTEX_INITIALIZER;

typedef struct
{
    int thread;
    int nr;
} msg_t;
msg_t finished = {-1, -1};

void *func(void *arg)
{
    int thread_number = *((int *)arg),
        limit = 20,
        current = 3,
        a = 1,
        b = 1,
        c = a + b;

    while (current < limit)
    {
        if (finished.thread != -1)
        {
            printf("Thread %d lost\n", thread_number);
            free(arg);
            return NULL;
        }

        a = b;
        b = c;
        c = a + b;
        current++;
    }

    pthread_mutex_lock(&finished_mutex);
    finished.thread = thread_number;
    finished.nr = c;
    printf("Thread %d finished and the 100'th fibonacii nr is %d\n", thread_number, c);
    pthread_mutex_unlock(&finished_mutex);

    free(arg);
    return NULL;
}

int main(int argc, char *argv[])
{
    int i, n;

    printf("N = "); scanf("%d", &n);

    pthread_t *array_threads = malloc(sizeof(pthread_t) * n);

    for (i = 0 ; i < n ; i++)
    {
        int *temp_i = malloc(sizeof(int));
        *temp_i = i;
        pthread_create (&array_threads[i], NULL, func, temp_i);
    }

    // clean
    for (i = 0 ; i < n ; i++)
    {
        pthread_join(array_threads[i] , NULL) ;
    }

    free(array_threads);

    return 0;
}
