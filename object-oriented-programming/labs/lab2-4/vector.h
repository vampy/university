#ifndef VECTOR_H_
#define VECTOR_H_

#define VECTOR_INITIAL_CAPACITY 128

#define VECTOR_RETURN_CHECK_INDEX(vector, index) \
    if(index < 0 || index >= vector->_length) \
    {                                         \
        return NULL;                          \
    }   

// the type
typedef struct 
{
    int _length; // the length so far
    int _capacity; // total available slots
    size_t _element_size; // size of an individual element
    
    void **_data; // the generic data
} Vector;


// Init the internal structure of the vector
void vector_init(Vector*, size_t);

// Add an element to the vector
void vector_append(Vector*, void*);

// Get an element by position
void *vector_get(Vector*, int);

// Set an element by position
void vector_set(Vector*, int, void*);

// Remove an element by position
void vector_remove(Vector*, int);

// Double vector capacity if it is full
void vector_double_capacity_if_full(Vector*);

void *vector_new_element(Vector*);

// Free the internal structure of the vector
void vector_free(Vector*);

// Clear the vector of all elements
void vector_clear_data(Vector*);

// helper functions
int vector_length(Vector*);

int vector_is_empty(Vector*);

void *vector_last(Vector*);

void *vector_first(Vector*);

void vector_push_back(Vector*, void*);

void vector_pop_back(Vector*);

// Create a 101 copy
Vector *vector_new_from(Vector*);

#endif //  VECTOR_H_
