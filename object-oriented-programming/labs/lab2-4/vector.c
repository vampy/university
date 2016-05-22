#include "vector.h"
#include <assert.h>
#include <memory.h>
#include <stdio.h>
#include <stdlib.h>

// TODO make memset in double capacity inteligent to 0
void vector_init(Vector* vector, size_t element_size)
{
    assert(element_size > 0);
    // assert(vector != NULL);

    // init variables
    vector->_length       = 0;
    vector->_capacity     = VECTOR_INITIAL_CAPACITY;
    vector->_element_size = element_size;

    // allocate memory
    vector->_data = (void**)calloc(VECTOR_INITIAL_CAPACITY, sizeof(void*));
}

void vector_append(Vector* vector, void* element)
{
    // double if space not left
    vector_double_capacity_if_full(vector);

    // append
    vector->_data[vector->_length] = vector_new_element(vector);

    // vector->_data[vector->_length] = element;
    memcpy(vector->_data[vector->_length], element, vector->_element_size);
    vector->_length++;
}

void* vector_get(Vector* vector, int index)
{
    VECTOR_RETURN_CHECK_INDEX(vector, index)

    return vector->_data[index];
}

void vector_set(Vector* vector, int index, void* element)
{
    assert(index >= 0);
    assert(index < vector->_length);

    // the elemenent is already on the heap
    memcpy(vector->_data[index], element, vector->_element_size);
}

void vector_remove(Vector* vector, int index)
{
    assert(index >= 0);
    assert(index < vector->_length);

    int i;
    for (i = index; i < vector->_length - 1; i++)
    {
        memcpy(vector->_data[i], vector->_data[i + 1], vector->_element_size);
    }

    // clear last element
    free(vector->_data[vector->_length - 1]);
    vector->_length--;
}

void vector_double_capacity_if_full(Vector* vector)
{
    // double capacity if no space left
    if (vector->_length >= vector->_capacity)
    {
        printf("JUST DOUBLED: Before=%d", vector->_capacity);
        vector->_capacity *= 2;
        printf(", After=%d \n", vector->_capacity);
        vector->_data = (void**)realloc(vector->_data, sizeof(void*) * vector->_capacity);
    }
}

void* vector_new_element(Vector* vector) { return calloc(1, vector->_element_size); }

// Free all the data allocated in the array
void vector_free(Vector* vector)
{
    vector_clear_data(vector);
    free(vector->_data);
}

// Clean the elements in the array
void vector_clear_data(Vector* vector)
{
    int i;
    for (i = 0; i < vector->_length; i++)
    {
        if (vector->_data[i] != NULL)
            free(vector->_data[i]);
    }
    vector->_length = 0;
}

// helper functions
int vector_length(Vector* vector) { return vector->_length; }

int vector_is_empty(Vector* vector) { return vector->_length == 0; }

void* vector_last(Vector* vector)
{
    VECTOR_RETURN_CHECK_INDEX(vector, vector->_length - 1)

    return vector->_data[vector->_length - 1];
}

void* vector_first(Vector* vector)
{
    VECTOR_RETURN_CHECK_INDEX(vector, 0)

    return vector->_data[0];
}

void vector_push_back(Vector* vector, void* element) { vector_append(vector, element); }

void vector_pop_back(Vector* vector) { vector_remove(vector, vector->_length - 1); }

Vector* vector_new_from(Vector* vector)
{
    Vector* new_vector = (Vector*)malloc(sizeof(Vector));
    vector_init(new_vector, vector->_element_size);

    for (int i = 0; i < vector->_length; i++)
        vector_append(new_vector, vector_get(vector, i));

    return new_vector;
}
