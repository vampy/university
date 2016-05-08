// minimax.cpp : Defines the entry point for the console application.
//

// MIT licence ... you may do whatever you want with this source code
// last updated on 2015.03.29
// version 0.2

#include <algorithm>
#include <climits>
#include <cstdio>
#include <cstdlib>

using namespace std;

//------------------------------------------------------------------------------
struct node
{
    int score;
    int num_children;
    int* children = NULL;
};

//------------------------------------------------------------------------------
bool read_tree(node*& tree, int& num_nodes, char* tree_file)
{
    // each node has an index.
    // root has index 0
    // the indexes of other nodes are obtained by traversing the tree level by level.

    // file structure: on each line we have information about one node:
    // node_index num_children
    // if num_children is 0 then the next value is the score of that leaf
    // if num_children > 0 then we have a list of indexes for each children

    FILE* f = fopen(tree_file, "r");
    if (f == NULL)
        return false;

    fscanf(f, "%d", &num_nodes);
    tree = new node[num_nodes];

    for (int i = 0; i < num_nodes; i++)
    {
        int node_index;
        fscanf(f, "%d", &node_index);
        fscanf(f, "%d", &tree[node_index].num_children);

        if (!tree[node_index].num_children) // leaf
        {
            fscanf(f, "%d", &tree[node_index].score);
            tree[node_index].children = NULL;
        }
        else // internal node
        {
            tree[node_index].children = new int[tree[node_index].num_children];
            tree[node_index].score    = 0; // it is not used anyway
            for (int j = 0; j < tree[node_index].num_children; j++)
            {
                fscanf(f, "%d", &tree[node_index].children[j]);
            }
        }
    }
    if (fclose(f) != 0)
        return false;

    return true;
}
//------------------------------------------------------------------------------
int minimax_ab(node* tree, int node_index, int level, int a, int b, int* visited, int& num_visited)
{
    if (!tree[node_index].num_children) // leaf node, get heuristic score
    {
        visited[num_visited++] = node_index;
        return tree[node_index].score;
    }

    if (level % 2) // min level
    {
        int min_score          = INT_MAX;
        visited[num_visited++] = node_index;

        for (int i = 0; i < tree[node_index].num_children; i++)
        {
            int child_score = minimax_ab(tree, tree[node_index].children[i], level + 1, a, b, visited, num_visited);

            min_score = min(min_score, child_score);
            b         = min(b, min_score);
            if (b <= a) // cut
                break;
        }

        return min_score;
    }
    else // max level
    {
        int max_score          = -INT_MAX;
        visited[num_visited++] = node_index;

        for (int i = 0; i < tree[node_index].num_children; i++)
        {
            int child_score = minimax_ab(tree, tree[node_index].children[i], level + 1, a, b, visited, num_visited);

            max_score = max(max_score, child_score);
            a         = max(a, max_score);
            if (b <= a) // cut
                break;
        }

        return max_score;
    }
}
//------------------------------------------------------------------------------
int main(int argc, char* argv[])
{
    if (argc != 2)
    {
        printf("Usage: %s <tree_file.txt>\n", argv[0]);
        return 1;
    }
    char* tree_file = argv[1];
    int num_nodes;
    node* tree = NULL;

    if (read_tree(tree, num_nodes, tree_file))
    {
        int a = -INT_MAX;
        int b = INT_MAX;

        int* visited     = new int[num_nodes]; // map, number => node index
        bool* is_visited = new bool[num_nodes];
        int num_visited  = 0;

        int alpha = minimax_ab(tree, 0, 0, a, b, visited, num_visited);

        printf("Alpha = %d\n", alpha);

        printf("Number of visited nodes = %d\n", num_visited);
        printf("Visited nodes = ");
        for (int i = 0; i < num_visited; i++)
        {
            printf("%d ", visited[i]);
        }

        // compute the unvisited nodes
        for (int i = 0; i < num_nodes; is_visited[i++] = false)
            ; // set vector to false;
        for (int i = 0; i < num_visited; is_visited[visited[i++]] = true)
            ;

        printf("\nUnvisited nodes: ");
        for (int i = 0; i < num_nodes; i++)
        {
            if (!is_visited[i])
            {
                printf("%d ", i);
            }
        }

        // free memory allocated by tree
        for (int i = 0; i < num_nodes; i++)
        {
            if (tree[i].children)
            {
                delete[] tree[i].children;
            }
        }

        if (tree != NULL)
        {
            delete[] tree;
        }
        delete[] visited;
        delete[] is_visited;
    }
    else
    {
        printf("ERROR: cannot read/find file!");
    }
    printf("\n");

    return 0;
}
//------------------------------------------------------------------------------
