/*2 programe
un program A
creaza un segment de 100 octeti, dupa ce e plin incepe de la inceput
si din secunda in secunda pune un octet aleator

programul B
din 3 in 3 secunde
verifica daca gaseste 2 octeti cel putin

afiseaza toti octetii care se repeta
si contitnutul segmentului
daca a apare de cel putin de 2 ori si b
afiseaza
ab si continutul segmenetuil
*/

#include "../os.h"
#include "../math.h"

int main()
{
    int id, i = 0, isize = 100;
    char *data;
    key_t key = safe_ftok(".", 1);
    
    
    if ((id = shmget(key, isize, 0600 | IPC_CREAT)) == -1)
    {
        perror("shmget");
        exit(1);
    }
    printf("Created: id = %d\n", id);
    
    
    data = (char *)shmat(id, NULL, 0);
    if (*((int *)data) == -1)
    {
        perror("shmat: ");
        exit(1);
    }
    data[isize] = 0;
    while (1)
    {
        if (i == isize)
        {
            i = 0;
        }
        
        data[i] = rand_interval(97, 122);
                
        i++;
        //sleep(1);
    }
    
        /* detach from the segment: */
    if (shmdt(data) == -1) 
    {
        perror("shmdt");
        exit(1);
    }
    
    return 0;
}

