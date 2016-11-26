#include <stdio.h>
#include <stdlib.h>

int main(int argc, char *argv[])
{
    int rows = 2, i, j, k, l;
    
    int ****matrix = (int****)malloc(rows * sizeof(int***));
    for(i = 0; i < rows; i++)
    {
        matrix[i] = (int***)malloc(rows * sizeof(int**));
        for(j = 0; j < rows; j++)
        {
            matrix[i][j] = (int**)malloc(rows * sizeof(int*));
            for(k = 0; k < rows; k++)
            {
                matrix[i][j][k] = (int*)malloc(rows * sizeof(int));
                for(l = 0; l < rows; l++)
                {
                    matrix[i][j][k][l] = 0;
                    printf("%d ", matrix[i][j][k][l]);
                }
                printf("\n");
            }
            printf("\n");
        }
    }
    
    for(i = 0; i < rows; i++)
    {
        for(j = 0; j < rows; j++)
        {
            for(k = 0; k < rows; k++)
            {
                free(matrix[i][j][k]);
            }
            free(matrix[i][j]);
        }
        free(matrix[i]);
    }
    free(matrix);
    
    return 0;
} 
