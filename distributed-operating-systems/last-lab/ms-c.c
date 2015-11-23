/* cu coz de mesaje
producatorul pune 2 tipuri de mesaje in coada
la primul un numar random
second string citit de la tastura
face chestia asta din 10 in 10 secunde

consumatorul la infinit
daca numarul din primul mesaj e par
afiseaza stringul
daca e e impar
afiseaza invers stringul
*/

#include "../os.h"

typedef struct msg
{
    long   type;
    int    nr;
    char   string[256];
} msg_t;


void string_reverse(char *string, int len)
{
    int i;
    
    int last_pos = len-1;
    for(i = 0; i < len/2; i++)
    {
        char tmp = string[i];
        string[i] = string[last_pos - i];
        string[last_pos - i] = tmp;
    }
}

int main()
{
    int id;
    key_t key = safe_ftok(".", 1);
    size_t length = sizeof(msg_t) - sizeof(long);
    msg_t m;
    
    while (1)
    {
        int nr;
        char string[256];
        
        if ((id = msgget(key, 0600)) == -1)
        {
            perror("msgget");
            exit(1);
        }
        printf("Id = %d\n", id);
        
        // get number
        if (msgrcv(id, &m, length, 1, 0) == -1)
        {
            perror("msgrcv");
            exit(1);
        }
        nr = m.nr;
        printf("Got nr = %d\n", nr);
        
        // get string
        if (msgrcv(id, &m, length, 2, 0) == -1)
        {
            perror("msgrcv");
            exit(1);
        }
        strcpy(string, m.string);
        
        
        if (nr % 2 == 0)
        {
            printf("Got string = %s\n", string);
        }
        else // reverse
        {
            string_reverse(string, strlen(string));
            printf("Got string = %s\n", string);
        }
        
        printf("\n");
    }
    
    
    return 0;
}