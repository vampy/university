//---------------------------------------------------------------------------
#include <cstdio>
#include <cstdlib>

using namespace std;

//---------------------------------------------------------------------------
struct chromosome // also called individual, or potential solution
{
    double *x;      // an array of genes; one for each dimension
    double fitness;
};

//---------------------------------------------------------------------------
void generate_random(chromosome *c, int num_dims, double min_x, double max_x)
{
    for (int i = 0; i < num_dims; i++)
        c->x[i] = rand() / (double)RAND_MAX * (max_x - min_x) + min_x;
}
//---------------------------------------------------------------------------
void copy_individual(chromosome *dest, chromosome *source, int num_dims)
{
    for (int i = 0; i < num_dims; i++)
    {
        dest->x[i] = source->x[i];
    }
    dest->fitness = source->fitness;
}
//---------------------------------------------------------------------------
void compute_fitness(chromosome *c, int num_dims)
{
    c->fitness = 0;

    for (int i = 0; i < num_dims; i++)
        c->fitness += c->x[i] * c->x[i];

    // another function
    // rastrigin
    //c->fitness = 10 * num_dims;
    //for (int i = 0; i < num_dims; i++)
        //c->fitness += c->x[i] * c->x[i] - 10 * cos(2 * 3.1415 * c->x[i]);
}
//---------------------------------------------------------------------------
void mutation(chromosome c, int num_dims, double pm, double delta, double min_x, double max_x) // mutate the individual
{
    // mutate each symbol with the same pm probability

    for (int i = 0; i < num_dims; i++)
    {

        double p = rand() / (double)RAND_MAX;      // mutate the operator
        if (p < pm)
        {
            int r = rand() % 2;// I choose randomly if I add or if I subtract delta
            if (r)
            {
                if (c.x[i] + delta <= max_x)
                    c.x[i] += delta;
            }
            else
            {
                if (c.x[i] - delta >= min_x)
                    c.x[i] -= delta;
            }
        }
    }
}
//---------------------------------------------------------------------------
void one_cut_point_crossover(chromosome parent1, chromosome parent2, chromosome offspring1, chromosome offspring2, int num_dims)
{
    int pct;
    pct = 1 + rand() % (num_dims - 1); // the cutting point should be after first gene and before the last one
    for (int i = 0; i < pct; i++)
    {
        offspring1.x[i] = parent1.x[i];
        offspring2.x[i] = parent2.x[i];
    }
    for (int i = pct; i < num_dims; i++)
    {
        offspring1.x[i] = parent2.x[i];
        offspring2.x[i] = parent1.x[i];
    }
}
//---------------------------------------------------------------------------
int sort_function(const void *a, const void *b)
{
    if (((chromosome *)a)->fitness > ((chromosome *)b)->fitness)
        return 1;
    else
        if (((chromosome *)a)->fitness < ((chromosome *)b)->fitness)
            return -1;
        else
            return 0;
}
//---------------------------------------------------------------------------
void print_chromosome(chromosome *c, int num_dims)
{
    printf("x = (");

    for (int i = 0; i < num_dims; i++)
    {
        printf("%lf ", c->x[i]);
    }
    printf(") ");
    printf("fitness = %lf\n", c->fitness);
}
//---------------------------------------------------------------------------
int tournament_selection(int tournament_size, chromosome *pop, int pop_size)     // Size is the size of the tournament
{
    int selected_index;
    selected_index = rand() % pop_size;
    for (int i = 1; i < tournament_size; i++)
    {
        int r = rand() % pop_size;
        selected_index = pop[r].fitness < pop[selected_index].fitness ? r : selected_index;
    }
    return selected_index;
}
//---------------------------------------------------------------------------
void start_steady_state_ga(int pop_size, int num_gens, int num_dims, double pcross, double pm, double delta, double min_x, double max_x) // Steady-State MEP
{
    // allocate memory
    chromosome *population;
    population = new chromosome[pop_size];
    for (int i = 0; i < pop_size; i++)
        population[i].x = new double[num_dims];

    chromosome offspring1, offspring2;
    offspring1.x = new double[num_dims];
    offspring2.x = new double[num_dims];

    // initialize
    for (int i = 0; i < pop_size; i++)
    {
        generate_random(&population[i], num_dims, min_x, max_x);
        compute_fitness(&population[i], num_dims);
    }

    // sort by fitness
    qsort((void *)population, pop_size, sizeof(population[0]), sort_function);

    printf("generation 0\n");
    print_chromosome(population, num_dims); // print the best from generation 0

    for (int g = 1; g < num_gens; g++)
    {
        for (int k = 0; k < pop_size; k += 2)
        {
            // choose the parents using binary tournament
            int r1 = tournament_selection(2, population, pop_size);
            int r2 = tournament_selection(2, population, pop_size);

            // crossover
            double p = rand() / double(RAND_MAX);
            if (p < pcross)
            {
                one_cut_point_crossover(population[r1], population[r2], offspring1, offspring2, num_dims);
            }
            else
            {
                copy_individual(&offspring1, &population[r1], num_dims);
                copy_individual(&offspring2, &population[r2], num_dims);
            }

            // mutate the result and move the mutant in the new population
            mutation(offspring1, num_dims, pm, delta, min_x, max_x);
            compute_fitness(&offspring1, num_dims);
            mutation(offspring2, num_dims, pm, delta, min_x, max_x);
            compute_fitness(&offspring2, num_dims);

            // copy the best child into our population
            if (offspring1.fitness < population[pop_size - 1].fitness)
            {
                copy_individual(&population[pop_size - 1], &offspring1, num_dims);
                qsort((void *)population, pop_size, sizeof(population[0]), sort_function);
            }
            if (offspring2.fitness < population[pop_size - 1].fitness)
            {
                copy_individual(&population[pop_size - 1], &offspring2, num_dims);
                qsort((void *)population, pop_size, sizeof(population[0]), sort_function);
            }
        }
        printf("generation %d\n", g);
        print_chromosome(population, num_dims);
    }

    // free memory
    delete [] offspring1.x;
    delete [] offspring2.x;

    for (int i = 0; i < pop_size; i++)
    {
        delete [] population[i].x;
    }
    delete [] population;
}
//---------------------------------------------------------------------------
int main(void)
{

    int pop_size = 10;              // the number of individuals in population  (must be an odd number!!!)
    int num_gens = 100;             // the number of generations
    double pm = 0.1;                // mutation probability
    double pcross = 0.9;            // crossover probability
    double delta = 1;               // jumps for mutation

    int num_dims = 2;               // number of dimensions of the function to be optimized
    double min_x = -10;             // definition domain for functions
    double max_x = 10;

    srand(0);
    start_steady_state_ga(pop_size, num_gens, num_dims, pcross, pm, delta, min_x, max_x);

    return 0;
}
//---------------------------------------------------------------------------
