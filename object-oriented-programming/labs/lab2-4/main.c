/**
 * P9. Travel agency
 * Create an application to manage the holiday offerings (destination, type – city
 * break,..,price) in a travel agency.
 * 1. Add a new holiday offer
 * 2. Modify holiday offer
 * 3. Remove offer
 * 4. Filter
 *    1. By type
 *    2. By price
 * 5. Undo the last operation - the last operation that has modified the list of
 * holidays is cancelled
 */

#include <stdio.h>
#include <stdlib.h>

#include "utils.h"
#include "ui.h"
#include "test.h"

int main()
{
    int ui_code = ui_start();

    // begin test
    test_vector_all();
    test_storage_all();

    if (ui_code == 0)
    {
        puts("Something went wrong. Aborting");
        exit(1);
    }

    return 0;
}
