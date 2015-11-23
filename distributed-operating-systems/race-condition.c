/*
    Race condition
    2 threaduri
    variabile globale a si b
    A: a = 5
    B: a++, print(a+b)

    Initial values
    a = 3
    b = 4
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>
#include <unistd.h>

#define N 2

int a = 3;
int b = 4;

void *func(void *arg)
{
    a = 5;

    return NULL;
}

void *func2(void *arg)
{
    a++;
    printf("%d\n", a + b);

    return NULL;
}

int main(int argc, char *argv[])
{
    int i;
    pthread_t thr[N];

    pthread_create(&thr[0], NULL, func, NULL);
    pthread_create(&thr[1], NULL, func2, NULL);

    // clean
    for (i = 0 ; i < N ; i++)
    {
        pthread_join(thr[i] , NULL) ;
    }

    return 0;
}
