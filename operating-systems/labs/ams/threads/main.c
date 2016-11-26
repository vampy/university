/*
 * Implement a program that gets as arguments a file name followed by words.
 * For each word, create a separate thread that
 * counts its appearances in the given file. Print out the sum of the appearances of all words.
 *
 * ex: int int int
 * float float
 * float float
float
 *
 * typedef struct file_word
{
    FILE *file;
    char word[1024];
} file_word;
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>

int sum = 0;
char *filename = NULL;
pthread_mutex_t sum_mutex = PTHREAD_MUTEX_INITIALIZER;


void *search_word(void *w)
{
    // open file

    FILE *handle = fopen(filename, "r");
    if(handle == NULL)
    {
        perror("File error: ");
        return NULL;
    }

    // find word
    char line[1024], *word = (char *)w;
    int count = 0, word_len = strlen(word);

    while(fgets(line, sizeof(line),  handle) != NULL)
    {
        char *found = line;
        //int line_len = strlen(line);

        // word in line
        while((found = strstr(found, word)) != NULL)
        {
            // move pointer
            found +=  word_len;
            count++;

           //puts("word in line");
           //printf("%d\n", temp);

           //putchar(*(found - 1));
           //putchar(found[word_len]);

           // previous and next char is space, to be word
        //    if(*(found - 1) == ' ' && (found[word_len]) == ' ')
        //    {
        //
        //    }
        }
    }
    fclose(handle);

    // write to global sum
    pthread_mutex_lock(&sum_mutex);
    sum += count;
    pthread_mutex_unlock(&sum_mutex);

    printf("Thread -> word = %s, count = %d\n", word, count);

    return NULL;
}

int main(int argc, char *argv[])
{
    if (argc <= 2)
    {
        printf("Usage: %s filename word1 word2 ... wordn\n", argv[0]);
        exit(1);
    }
    int i, words = argc - 2;
    filename = argv[1];

    // create threads minus the executable and filename
    pthread_t threads[words];

    for(i = 0; i < words; i++)
    {
        //puts(argv[i + 2]);
        pthread_create(&threads[i], NULL, search_word, argv[i + 2]);
    }

    for(i = 0; i < words; i++)
    {
        pthread_join(threads[i], NULL);
    }

    printf("Sum = %d\n", sum);

    return 0;
}
