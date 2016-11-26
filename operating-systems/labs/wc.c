#include <stdio.h>
#include <stdlib.h>

int main(int argc, char *argv[])
{
    int word_count = 0, line_count = 0;
    char read_char;
    FILE *fp = fopen("/etc/passwd", "r");

    if(fp == NULL)
    {
        perror("An error occured: ");
        exit(1);
    }
    printf("Reading file: \n");
    
    do
    {
        read_char = fgetc(fp);
        if(read_char == '\n')
        {
            line_count++;
        }
        word_count++;
    } while (read_char != EOF);
    
    // minus the last EOF
    word_count--;
    
    printf("word_count=%d, line_count=%d \n", word_count, line_count);
    
    // clean up
    fclose(fp);
    
    return 0;
}
