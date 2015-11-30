// Find the number of cores on the system by doing computations.
#include "../os.h"
#include <sched.h>
#include <pthread.h>

#define NR_PROC 8

int max(const int a, const int b)
{
    return a > b ? a : b;
}

// hold largest found proc number
int largest = 0;
pthread_mutex_t largest_mutex;

void *thread_main(void *arg)
{
    int cpu_nr = sched_getcpu();

    pthread_mutex_lock(&largest_mutex);
    if (cpu_nr > largest)
    {
        largest = cpu_nr;
    }
    pthread_mutex_unlock(&largest_mutex);

    return NULL;
}

pid_t calculate_processes(const int nr_threads)
{
    pid_t pid = safe_fork();
    if (pid == 0)
    {
        // init
        int i;
        pthread_t thr[nr_threads];
        pthread_mutex_init(&largest_mutex, NULL);

        for (i = 0; i < nr_threads; i++)
        {
            pthread_create(&thr[i], NULL, thread_main, NULL);
        }

        for (i = 0; i < nr_threads; i++)
        {
            pthread_join(thr[i], NULL);
        }
        pthread_mutex_destroy(&largest_mutex);

        exit(largest);
    }

    return pid;
}

int main(int argc, char *argv[])
{
    int nr_threads = 25;
    pid_t processes[NR_PROC];

    while (1)
    {
        int i, previous, current;
        bool retry = false;
        for (i = 0; i < NR_PROC; i++)
        {
            processes[i] = calculate_processes(nr_threads);
        }

        for (i = 0, previous = 0, current = 0; i < NR_PROC; i++)
        {

            waitpid(processes[i], &current, 0);
            current = WEXITSTATUS(current);

            largest = max(largest, current); // save largest
            if (previous && previous != current)
            {
                DEBUG_PRINT("Found different cores: %d != %d\n", previous, current);
                nr_threads = (int)(nr_threads * 1.4);
                retry = true;
            }
            previous = current;
        }

        if (!retry && largest >= max(current, previous))
        {
            break;
        }
    }

    printf("%d\n", largest + 1);

    return EXIT_SUCCESS;
}
