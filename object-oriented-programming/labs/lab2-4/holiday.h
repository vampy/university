#ifndef HOLIDAY_H_
#define HOLIDAY_H_

#include "vector.h"

#define HOLIDAY_DESTINATION_LENGTH 127
#define HOLIDAY_TYPE_LENGTH 127

#define STORAGE_UNDO_OPERATION_PUSH_BACK 1
#define STORAGE_UNDO_OPERATION_SET 2
#define STORAGE_UNDO_OPERATION_DELETE 3

typedef struct Holiday
{
    int id; // unique id
    char destination[HOLIDAY_DESTINATION_LENGTH + 1];
    char type[HOLIDAY_TYPE_LENGTH + 1];
    float price;
} Holiday;


typedef struct Storage
{
    Vector *vector;
    
    // only 128 history operations
    Vector *undo_vectors[128];
    int undo_length;
} Storage;



Storage *storage_create();

void storage_destroy(Storage*);

// Helper function verify if id exists
int storage_holiday_id_exists(Storage*, const int);

// Helper function insert a holiday
void storage_insert_holiday(Storage*, Holiday*);

// Helper function update a holiday
void storage_update_holiday(Storage*, Holiday*);

// Helper function delete a holiday
void storage_delete_holiday(Storage*, const int);

// Do a 1 on 1 copy of holiday
void storage_holiday_copy(Holiday*, const Holiday*);

// Undo the last operation
// Return 1 on success
// else 0 when there is nothing to undo
int storage_undo(Storage*);

// Filter the current repository by price
// Third argument is direction
// -1 less than
// 0 equal to
// 1 greater than
void storage_filter_by_price(Storage*, const float, int);

// Filter the current repository by type
void storage_filter_by_type(Storage*, const char*);

// Print a holiday
void storage_print_holiday(const Holiday*);

// Print all holidays
void storage_print_all(const Storage*);


void test_holiday();

#endif /* HOLIDAY_H_ */