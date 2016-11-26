#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>

int result;
int main()
{
    int array[] = {1, 2, 3, 4}, result_first = 0, result_second = 0;
    pid_t first = fork();
    if(first == 0)
    {
        result_first += array[0] + array[1];
        printf("Child 1, %d \n", result_first);
        exit(0);
    }

    pid_t second = fork();
    if(second == 0)
    {
        result_second += array[2] + array[3];
        printf("Child 2, %d \n", result_second);
        exit(0);
    }

    wait(0);
    wait(0);

    result += result_first + result_second;
    printf("Parent: result=%d \n", result);

    return 0;
}
