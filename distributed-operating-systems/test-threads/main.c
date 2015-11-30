/*
 * We have 2 threads that play ping pong using a heap variable.
 * The first thread generates a number n between 100 and 1000 and it puts it in the heap variable.
 * The next thread generates a value between -10 and 10 and it adds it to the heap variable, until the variable is 0
 */
#include <stdlib.h>
#include <stdio.h>
#include <pthread.h>
#include <time.h>

pthread_mutex_t mutex_a = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t mutex_b = PTHREAD_MUTEX_INITIALIZER;

void *thread_a(void *arg)
{
    long *n = (long *)arg;
    *n = rand() % 900 + 100;
    printf("Number is %d\n", *n);
    while (1)
    {
        pthread_mutex_lock(&mutex_a);

        printf("A\n");
        *n += rand() % 20 + (-10);
        //printf("Number is %d\n", *n);
        if (*n == 0)
        {
            printf("FOUND from A\n");
            break;
        }

        pthread_mutex_unlock(&mutex_b);
    }

    return NULL;
}

void *thread_b(void *arg)
{
    long *n = (long *)arg;
    while(1)
    {
        pthread_mutex_lock(&mutex_b);

        printf("B\n");
        *n += rand() % 20 + (-10);
        //printf("Number is %d\n", *n);
        if (*n == 0)
        {
            printf("FOUND from B\n");
            break;
        }

        pthread_mutex_unlock(&mutex_a);
    }
    return NULL;
}

int main()
{
    srand(time(NULL));
    pthread_t a, b;
    long *n = (long *)malloc(sizeof(long));

    // firt block b
    pthread_mutex_lock(&mutex_b);
    pthread_create(&a, NULL, thread_a, n);
    pthread_create(&b, NULL, thread_b, n);

    while (1)
    {
        if (*n == 0)
        {
            pthread_join(a, NULL);
            pthread_join(b, NULL);
            break;
        }
    }

    return 0;
}
