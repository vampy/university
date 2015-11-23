#pragma once
#include "os.h"

typedef struct table
{
    int matrix[3][3]; // 0 is undefined, 1 is X, 2 is O
} table_t;

bool table_is_occupied(table_t *table, int row, int col)
{
    return table->matrix[row][col] == 0;
}

int table_put_rand_pos(table_t *table, const int value)
{
    int row, col;
    do
    {
        row = rand() % 3;
        col = rand() % 3;
    } while(table_is_occupied(table, row, col));

    DEBUG_PRINT("Put on table[%d][%d] = %c", row, col, value == 0 ? 'O' : 'X');
    if (value == 0) // 0
    {
        table->matrix[row][col] = 2;
    }
    else // X
    {
        table->matrix[row][col] = 1;
    }

    return row * col;
}

int table_set_value(table_t *table, int modified_pos, int value)
{
    int row = modified_pos / 3;
    int col = modified_pos % 3;

    if (table_is_occupied(table, row, col))
        return -1;

    table->matrix[row][col] = value == 0 ? 2 : 1;

    return 0;
}

int table_is_finished(table_t *table)
{
    int i, j, sum_rows = 0, sum_cols = 0;

    for (i = 0; i < 2; i++)
    {
        // on columns
        if (table->matrix[i][0] == table->matrix[i + 1][0])
        {
            for (j = 0; j < 3; j++)
            {
                sum_cols += table->matrix[i][0] + table->matrix[i + 1][0] + table->matrix[i][0]
            }
        }

        // if sum is 3 or sum is 6
    }
}

void table_print(table_t *table)
{
    print_matrix(table->matrix, 3, 3);
}
