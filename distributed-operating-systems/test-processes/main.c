#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <math.h>

#define R 0
#define W 1

/*
 * Avem 2 procese
 * Un proces A citeste un 2 numere de la tastarura
 * Si ii trimie prin pipe la procesul B, numarul citit
 * Procesul B determina a^b si il trimite la A
 * Si trimite la A
 * A afiseaza ce o primit de la B
*/
int main(int argc, char *argv[])
{
    int a2b[2], b2a[2];
    
    if (pipe(a2b) < 0)
    {
        perror("pipe");
        exit(1);
    }
    if (pipe(b2a) < 0)
    {
        perror("pipe");
        exit(1);
    }
    
    pid_t pid_a = fork();
    if (pid_a < 0)
    {
        perror("fork");
        exit(1);
    }
    if (pid_a == 0)
    {
        // A
        float a, b;
        printf("Process A\n");
        close(a2b[R]);
        close(b2a[W]);
        
        printf("a = "); scanf("%f", &a);
        printf("b = "); scanf("%f", &b);
        //printf("A: %f, %f\n", a, b);
        
        write(a2b[W], &a, sizeof(a));
        write(a2b[W], &b, sizeof(b));
        
        float result;
        read(b2a[R], &result, sizeof(result));
        
        printf("A: %f ^ %f = %f\n", a, b, result);
      
        exit(0);
    }
    
    // B
    printf("Process B\n");
    float a, b;
    read(a2b[R], &a, sizeof(a));
    read(a2b[R], &b, sizeof(b));
    //printf("B: %f, %f\n", a, b);
    
    float result = powf(a, b);
    //printf("B: result = %f\n", result);
    write(b2a[W], &result, sizeof(result));
    
    wait(0);
    close(a2b[W]);
    close(a2b[R]);
    close(b2a[R]);
    close(b2a[W]);
    
    return 0;
}
