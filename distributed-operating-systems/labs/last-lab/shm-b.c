#include "../os.h"

int is_char_in(char find, char *haystack, int len)
{
    int i;

    for (i = 0; i < len; i++)
    {
        if (haystack[i] == find)
        {
            return 1;
        }
    }

    return 0;
}

int main()
{
    int id, i = 0, isize = 100;
    char *data, repeating[100], repeating_len = 0;;
    key_t key = safe_ftok(".", 1);

    if ((id = shmget(key, isize, 0600)) == -1)
    {
        perror("shmget");
        exit(1);
    }

    data = (char *)shmat(id, NULL, 0);
    if (*((int *)data) == -1)
    {
        perror("shmat: ");
        exit(1);
    }
    while (1)
    {
        data[isize] = 0;
        for (i = 0; i < isize; i++)
        {
            if (is_char_in(data[i], (data + i + 1), isize))
            {
                //printf("Repeating char: %c\n", data[i]);
                if (!is_char_in(data[i], repeating, repeating_len))
                {
                    repeating[repeating_len++] = data[i];
                }
            }
        }

        printf("Repeating: \n");
        for (i = 0; i < repeating_len; i++)
        {
            printf("%c  ", repeating[i]);
        }
        printf("\nData = %s\n\n", data);
        repeating_len = 0;
        
        sleep(3);
    }

    return 0;
}
