// se citeste un n de la tastarura n threaduri
// vrem sa sorteze in paralele un sir de n intregi
// de fiecare data cand se modifica sirul
// numai atuncea se face blocarea
// fiecare thread afiseaza cine e el si ce schimba
// doarme timp de 10ms

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>
#include <unistd.h>

int n, *array;
pthread_mutex_t array_mutex = PTHREAD_MUTEX_INITIALIZER;

void *func(void *arg)
{
    int thread_number = *((int *)arg), i, j;

    for (i = 0; i < n; i++)
	{
		int index_of_min = i;

		for (j = i; j < n; j++)
		{
			if (array[index_of_min] > array[j])
			{
				index_of_min = j;
			}
		}

        pthread_mutex_lock(&array_mutex);
        printf("Thred %d switching positions %d and %d\n", thread_number, i, index_of_min);
		int temp = array[i];
		array[i] = array[index_of_min];
		array[index_of_min] = temp;
        pthread_mutex_unlock(&array_mutex);

        usleep(10);
	}

    //usleep(i * 1000);
    //printf("Thread %d finished\n", thread_number);

    free(arg);
    return NULL;
}

int main(int argc, char *argv[])
{
    int i;

    printf("N = "); scanf("%d", &n);

    array = malloc(sizeof(int) * n);
    pthread_t *array_threads = malloc(sizeof(pthread_t) * n);

    for (i = 0; i < n; i++)
    {
        int number;
        printf("array[%d] = ", i); scanf("%d", &number);
        array[i] = number;
    }

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

    for (i = 0 ; i < n ; i++)
    {
        printf("%d ", array[i]);
    }

    free(array);
    free(array_threads);

    return 0;
}
