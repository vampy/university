#pragma once
#include <stdlib.h>
#include <time.h>
#include <math.h>
// USE: gcc <p.c> -g -Wall -lm -o <p>

int max(int a, int b)
{
    return a > b ? a : b;
}

int min(int a, int b)
{
    return a < b ? a : b;
}

/**
 * ------------------ RANDOM FUNCTIONS ------------------------------------
*/

void rand_seed()
{
    static int is_srand_seeded = 0;
    if (is_srand_seeded)
    {
        return;
    }

    srand(time(NULL));
    is_srand_seeded = 1;
}

unsigned rand_interval(unsigned min, unsigned max)
{
    // Not quite random
    // SEE: https://stackoverflow.com/questions/2509679/how-to-generate-a-random-number-from-within-a-range
    return rand() % (max - min + 1) + min ;
}
