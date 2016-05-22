#include <assert.h>
#include <stdio.h>
#include <stdlib.h>

#include "holiday.h"
#include "vector.h"

void test_vector_all()
{
    Vector* vector = (Vector*)malloc(sizeof(Vector));

    vector_init(vector, sizeof(int));
    assert(vector->_capacity == VECTOR_INITIAL_CAPACITY);
    assert(vector->_length == 0);
    assert(vector_is_empty(vector) == 1);
    assert(vector_length(vector) == 0);
    assert(vector->_element_size == sizeof(int));

    assert(vector_get(vector, 10) == NULL);

    // Test append
    int v1 = 1;
    vector_append(vector, &v1);
    int* v1_p = (int*)vector_get(vector, 0);
    assert(*v1_p == v1);
    assert(vector_length(vector) == 1);

    int v2 = v1 + 1;
    vector_append(vector, &v2);
    int* v2_p = (int*)vector_get(vector, 1);
    assert(*v2_p == v2);
    assert(vector_length(vector) == 2);

    int v3 = v2 + 1;
    vector_append(vector, &v3);
    int* v3_p = (int*)vector_get(vector, 2);
    assert(*v3_p == v3);
    assert(vector_length(vector) == 3);

    int v4 = v3 + 1;
    vector_append(vector, &v4);
    int* v4_p = (int*)vector_get(vector, 3);
    assert(*v4_p == v4);
    assert(vector_length(vector) == 4);

    int v5 = v4 + 1;
    vector_append(vector, &v5);
    int* v5_p = (int*)vector_get(vector, 4);
    assert(*v5_p == v5);
    assert(vector_length(vector) == 5);

    // Test remove
    vector_remove(vector, 4);
    assert(vector_length(vector) == 4);
    assert(vector_get(vector, 4) == NULL);
    int* r1_p = (int*)vector_get(vector, 3);
    assert(*r1_p == v4);

    vector_remove(vector, 3);
    assert(vector_length(vector) == 3);
    assert(vector_get(vector, 3) == NULL);
    int* r2_p = (int*)vector_get(vector, 2);
    assert(*r2_p == v3);

    vector_remove(vector, 2);
    assert(vector_length(vector) == 2);
    assert(vector_get(vector, 2) == NULL);
    int* r3_p = (int*)vector_get(vector, 1);
    assert(*r3_p == v2);

    vector_free(vector);
    free(vector);
}

void test_storage_all()
{
    Storage* storage = storage_create();

    assert(storage_holiday_id_exists(storage, 23) == 0);

    Holiday temp_holiday_1 = {.id = 2, .type = "Type"};
    assert(storage_holiday_id_exists(storage, 2) == 0);
    storage_insert_holiday(storage, &temp_holiday_1);
    assert(storage_holiday_id_exists(storage, 2) == 1);

    Holiday temp_holiday_2 = {.id = 3, .type = "Type"};
    assert(storage_holiday_id_exists(storage, 3) == 0);
    storage_insert_holiday(storage, &temp_holiday_2);
    assert(storage_holiday_id_exists(storage, 3) == 1);

    Holiday temp_holiday_3 = {.id = 4, .type = "Type"};
    assert(storage_holiday_id_exists(storage, 4) == 0);
    storage_insert_holiday(storage, &temp_holiday_3);
    assert(storage_holiday_id_exists(storage, 4) == 1);

    storage_undo(storage);
    assert(storage_holiday_id_exists(storage, 4) == 0);

    storage_destroy(storage);
}
