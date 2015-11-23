/*
avem un program care genereaza un numar in 1 si 1000

memorie partajata

face un segment de lungimea care lo creat
daca e creat, pune pe prima pozitie libera din segment
si afiseaza continutul segmentului
*/


/*
Avem un client si un server
clientul ii trimite un numar, putem sa facem orice vrem noi cu el
serverul tine o matrice si incepe sa completeze din centrul matricii
la primul numar o sa il scrie in centru
al doilea deasupra (dupleaza ce o fost inapoi)
al treilea in dreapta (dubleaza deasupra)
in dreapta in stanga

*/
#include "os.h"
#include "math.h"

#define PATH "/home/dan/dev/os/"

int main()
{
    rand_seed();
    int number = rand_interval(1, 100);
    key_t key = safe_ftok(PATH, 1);


    int id = shmget(key, 1024, IPC_CREAT | IPC_EXCL | 0666);
    if (id == -1 && errno == EEXIST)
    {
        printf("Does not exist!");
        // create it
        int id = shmget(key, number, IPC_CREAT | 0666);
        if (id == -1)
        {
            perror("Can not create shared memory");
            exit(1);
        }
        

    }
    else
    {
        printf("Does exist!");
    }

    return 0;
}
