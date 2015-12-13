// 7. Print Pascal triangle of dimension n of all combinations C(m,k) of m objects taken by
//
// k, k = 0, 1, ..., m, for line m, where m = 1, 2, ..., n.

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

// Calculate the factorial of a number
// input:
//       n - the number which we take the factorial
// return:
//       n! - the factorial 
int factorial(int n)
{
    int i, n_fact = 1;

    for(i = 1 ; i <= n ; i++) 
    {
        n_fact = n_fact * i;
    }

    return n_fact;
}

// Print on the screen a pascal triangle
// input:
//      dimension - the number of rows of the triangle pascal
void print_pascal_triangle(int dimension)
{
    int k, i;
    
    for(k = 0 ; k < dimension; k++)
    {
        // print the current row
        // Combination of k taken with i
        for(i = 0 ; i <= k ; i++)
        {
            printf("%d ", factorial(k)/(factorial(i) * factorial(k - i)));
        }
        
        printf("\n");
    }
}
 
int main()
{
    int n;
    
    printf("Dimension n = ");
    scanf("%d", &n);
    
    print_pascal_triangle(n);

    return 0;
}
