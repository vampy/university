#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <string.h>

#include "holiday.h"
#include "utils.h"
#include "vector.h"

#define STORAGE_SET_UNDO(block) \
    if (st->_undo_state.undoing == 0) block

Storage* storage_create()
{
    // init controller
    Storage* storage = (Storage*)malloc(sizeof(Storage));

    // init current data
    storage->vector = (Vector*)malloc(sizeof(Vector));
    vector_init(storage->vector, sizeof(Holiday));

    // init undo
    storage->undo_length = 0;

    return storage;
}

void storage_destroy(Storage* storage)
{
    // free current data
    vector_free(storage->vector);
    free(storage->vector);

    // free undo
    int i;
    for (i = 0; i < storage->undo_length; i++)
    {
        vector_free(storage->undo_vectors[i]);
        free(storage->undo_vectors[i]);
    }

    // free main container aka controller
    free(storage);
}

void storage_add_to_undo(Storage* storage, Vector* vector)
{
    // printf("Added to undo"); print_eol();
    storage->undo_vectors[storage->undo_length] = (Vector*)vector_new_from(vector);
    storage->undo_length++;
}

int storage_undo(Storage* storage)
{
    // nothing to undo
    if (storage->undo_length == 0)
    {
        return 0;
    }

    // free current data
    vector_free(storage->vector);
    free(storage->vector);

    // set current data to last undo, will be freed by destroy
    storage->vector = (Vector*)storage->undo_vectors[storage->undo_length - 1];
    // printf("Undo set"); print_eol();

    // remove from undo
    storage->undo_vectors[storage->undo_length - 1] = NULL;
    storage->undo_length--;

    return 1;
}

// return the index in our storage where te id is present
int storage_find_index_by_id(Storage* storage, const int id)
{
    int i, length = vector_length(storage->vector);

    for (i = 0; i < length; i++)
    {
        Holiday* temp_holiday = (Holiday*)vector_get(storage->vector, i);
        if (temp_holiday->id == id)
        {
            return i;
        }
    }
    return -1;
}

int storage_holiday_id_exists(Storage* storage, const int id)
{
    if (storage_find_index_by_id(storage, id) != -1) return 1;

    return 0;
}

void storage_insert_holiday(Storage* storage, Holiday* holiday)
{
    storage_add_to_undo(storage, storage->vector);
    vector_append(storage->vector, holiday);
}

void storage_update_holiday(Storage* storage, Holiday* holiday)
{
    storage_add_to_undo(storage, storage->vector);
    int index = storage_find_index_by_id(storage, holiday->id);
    //     printf("update, index=%d, id=%d\n", index, wtf);
    //     print_holiday(ho);

    vector_set(storage->vector, index, holiday);
}

void storage_delete_holiday(Storage* storage, const int id)
{
    storage_add_to_undo(storage, storage->vector);
    int index = storage_find_index_by_id(storage, id);

    vector_remove(storage->vector, index);
}

void storage_print_header_all_holidays()
{
    printf("%s\t%s\t\t\t%s\t\t%s", "ID", "Description", "Type", "Price");
    print_eol();
    print_eol();
}

void storage_print_holiday(const Holiday* holiday)
{
    printf("%d\t%s\t\t\t%s\t\t%f\n", holiday->id, holiday->destination, holiday->type, holiday->price);
}

void storage_print_all(const Storage* storage)
{
    int i, j, length = vector_length(storage->vector);
    if (length == 0)
    {
        printf("There are NO holidays.");
        print_eol();
        return;
    }

    storage_print_header_all_holidays();
    for (i = 0; i < length; i++) storage_print_holiday((Holiday*)(vector_get(storage->vector, i)));

    // ALL undos
    //     printf("HISTORY"); print_eol(); print_eol();
    //     for(i = 0; i < storage->undo_length; i++)
    //     {
    //         printf("-------------------"); print_eol(); print_eol();
    //         length = vector_length(storage->undo_vectors[i]);
    //         for(j = 0; j < length; j++)
    //         {
    //             storage_print_holiday((Holiday
    //             *)(vector_get(storage->undo_vectors[i], j)));
    //         }
    //     }
}

void storage_filter_by_price(Storage* storage, const float price, int direction)
{
    storage_add_to_undo(storage, storage->vector);
    Vector* new_filtered = (Vector*)malloc(sizeof(Vector));
    vector_init(new_filtered, sizeof(Holiday));

    int i, length = vector_length(storage->vector);
    for (i = 0; i < length; i++)
    {
        Holiday* temp_holiday = (Holiday*)vector_get(storage->vector, i);

        if (direction == -1)
        {
            if (temp_holiday->price < price)
            {
                vector_append(new_filtered, temp_holiday);
            }
        }
        else if (direction == 1)
        {
            if (temp_holiday->price > price)
            {
                vector_append(new_filtered, temp_holiday);
            }
        }
        else if (direction == 0)
        {
            if (temp_holiday->price == price)
            {
                vector_append(new_filtered, temp_holiday);
            }
        }
    }

    // delete old
    vector_free(storage->vector);
    free(storage->vector);
    storage->vector = new_filtered;
}

void storage_filter_by_type(Storage* storage, const char* type)
{
    storage_add_to_undo(storage, storage->vector);
    Vector* new_filtered = (Vector*)malloc(sizeof(Vector));
    vector_init(new_filtered, sizeof(Holiday));

    int i, length = vector_length(storage->vector);
    for (i = 0; i < length; i++)
    {
        Holiday* temp_holiday = (Holiday*)vector_get(storage->vector, i);

        if (strcmp(temp_holiday->type, type) == 0)
        {
            vector_append(new_filtered, temp_holiday);
        }
    }

    // delete old
    vector_free(storage->vector);
    free(storage->vector);
    storage->vector = new_filtered;
}

// void test_holiday()
// {
//     storage st;
//     storage_init(&st);
//
//     holiday ho_push = {.id = 1, .destination = "Test", .type="TestType"};
//
//     // push
//     storage_push_back(&st, &ho_push);
//
//     // test get
//     holiday ho_test = storage_get(&st, 0);
//     assert(ho_test.id == 1);
//     assert(strcmp(ho_test.destination, "Test") == 0);
//     assert(strcmp(ho_test.type, "TestType") == 0);
//
//     storage_free(&st);
// }

// Copy an holiday
// void storage_holiday_copy(Holiday *destination, const Holiday *source)
// {
//     destination->id = source->id;
//     destination->price = source->price;
//     strcpy(destination->destination, source->destination);
//     strcpy(destination->type, source->type);
// }
