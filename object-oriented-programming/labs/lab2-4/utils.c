#include <stdio.h>

void print_eol()
{
    printf("\n");
}

// Read a certain number of character from the keyboard 
// put it in *string
// return the number of characters read
int read_string(char *string, int read_length)
{
    char read_char;
    int i = 0;
    
    // weird case when we have \n on our first character
    if((read_char = getchar()) != '\n')
    {
        // not special case
        string[i] = read_char;
        i++;
    }
       
    while ((read_char = getchar()) != '\n' && i < read_length)
    {
        string[i] = read_char;
        i++;
    }
    // put last character
    string[i] = '\0';
    
    return i;
}