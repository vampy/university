#pragma once
#include <pthread.h>
#include <stdlib.h>
// USE: gcc <p.c> -g -Wall -pthread -o <p>

void safe_mutex_init(pthread_mutex_t *mutex, const pthread_mutexattr_t *mutexattr)
{
    if (pthread_mutex_init(mutex, mutexattr) != 0)
    {
        perror("safe_mutex_init: Failed to init mutex");
        exit(EXIT_FAILURE);
    }
}
