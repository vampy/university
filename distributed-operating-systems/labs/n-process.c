#include "os.h"

void n_processes(int n)
{
    if (n == 0) return;

    pid_t pid = safe_fork();
    if (pid == 0)
    {
        printf("n = %d, Dad: %d, Child: %d\n", n, getppid(), getpid());
        n_processes(n - 1);
        wait(0);
    }
}

int main(int argc, char *argv[])
{
    int n;
    printf("Number of threads: "); scanf("%d", &n);

    n_processes(n);

    return EXIT_SUCCESS;
}
