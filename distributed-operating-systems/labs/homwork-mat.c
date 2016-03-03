/*
 * Homework
 * Matrix N x N (one mutex for the whole matrix)
 * N threads
 * Thread i
 * - if i is even:
 *      - change column i
 *    else
 *      - change line i
 * - change rand(0, 1) from every element i
 *     - 0 - add
 *     - 1 - subtract
 */

#include "os.h"
#include "math.h"
#include <pthread.h>

#define ADD 0

int n, **matrix;
pthread_mutex_t matrix_mutex = PTHREAD_MUTEX_INITIALIZER;

void *thread_main(void *arg)
{
    int thread_number = *((int *)arg), i, j;
    int op = rand() % 2;

    if (thread_number % 2 == 0) // column
    {
        for (i = 0; i < n; i++)
        {
            if (op == ADD)
            {
                pthread_mutex_lock(&matrix_mutex);
                matrix[i][thread_number] += thread_number;
                pthread_mutex_unlock(&matrix_mutex);
            }
            else
            {
                pthread_mutex_lock(&matrix_mutex);
                matrix[i][thread_number] -= thread_number;
                pthread_mutex_unlock(&matrix_mutex);
            }
        }
    }
    else // line
    {
        for (j = 0; j < n; j++)
        {
            if (op == ADD)
            {
                pthread_mutex_lock(&matrix_mutex);
                matrix[thread_number][j] += thread_number;
                pthread_mutex_unlock(&matrix_mutex);
            }
            else
            {
                pthread_mutex_lock(&matrix_mutex);
                matrix[thread_number][j] += thread_number;
                pthread_mutex_unlock(&matrix_mutex);
            }
        }
    }

    pthread_mutex_lock(&matrix_mutex);
    printf("\nThread: %d\n", thread_number);
    print_matrix(matrix, n, n);
    pthread_mutex_unlock(&matrix_mutex);

    free(arg);
    return NULL;
}

int main(int argc, char *argv[])
{
    int i;
    printf("Matrix size N = "); scanf("%d", &n);

    pthread_t *array_threads = malloc(sizeof(pthread_t) * n);
    matrix = (int**)malloc(n * sizeof(int*));
    for (i = 0; i < n; i++)
    {
        matrix[i] = (int*)calloc(n, n * sizeof(int));
    }

    rand_seed();
    print_matrix(matrix, n, n);
    for (i = 0; i < n; i++)
    {
        int *temp_i = malloc(sizeof(int));
        *temp_i = i;
        pthread_create(&array_threads[i], NULL, thread_main, temp_i);
    }

    // clean
    for (i = 0; i < n; i++)
    {
        pthread_join(array_threads[i] , NULL) ;
    }
    free(array_threads);

    for (i = 0; i < n; i++)
    {
        free(matrix[i]);
    }
    free(matrix);

    return 0;
}
