#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "utils.h"
#include "holiday.h"
#include "validate.h"
#include "vector.h"

#define SAFE_SCANF(sf) \
    if(sf == 0) \
    {    return 0; }
    
#define VALIDATE_HOLIDAY(v_temp_holiday, v_operation)       \
    if(validate_type(v_temp_holiday.type) &&                \
        validate_destination(v_temp_holiday.destination))   \
    {                                                       \
       v_operation;                                         \
    }                                                       \
    else                                                    \
    {                                                       \
       printf("INVALID: Type or Destination contains digits.\n"); \
    }

void ui_print_help_menu()
{
    puts("Help menu");
    puts("\t 1. Add holiday offer");
    puts("\t 2. Update holiday offer");
    puts("\t 3. Delete offer");
    puts("\t 4. List all offers");
    print_eol();
    puts("\t 5. Filter by type");
    puts("\t 6. Filter by price");
    puts("\t 7. Undo the last operation");
    print_eol();
    puts("\t 0. Quit");
}

// 
/*
 * Read the actual data not the meta info
 * 
*/
int ui_read_holiday_data(Holiday *holiday)
{
    // read destination
    printf("Destination: ");
    read_string(holiday->destination, HOLIDAY_DESTINATION_LENGTH);
    
    // read type
    printf("Type: ");
    read_string(holiday->type, HOLIDAY_TYPE_LENGTH);
    
    // read price
    printf("Price: ");
    SAFE_SCANF(scanf("%f", &(holiday->price)))
}

/*
 * Read the data for the holiday from the keyboard
 *    Storage *st - the Storage space
 *    holiday *ho - the holiday to read in
*/
int ui_read_holiday(Storage *storage, Holiday *holiday)
{
    // read id
    do
    {
        printf("Id(must be unique): ");
        SAFE_SCANF(scanf("%d", &(holiday->id)));
    }
    while(storage_holiday_id_exists(storage, holiday->id));
    
    ui_read_holiday_data(holiday);
}

int ui_start()
{
    //test_holiday();
    Storage *storage = storage_create();
    
    puts("Travel agency program");
    print_eol();
    ui_print_help_menu();
    while(1) 
    {
        int command;
        
        printf(">> ");
        SAFE_SCANF(scanf("%d", &command))
        
        if(command == 1)
        {
            Holiday temp_holiday;
            
            puts("Add holiday");
            
            ui_read_holiday(storage, &temp_holiday);
            VALIDATE_HOLIDAY(temp_holiday, storage_insert_holiday(storage, &temp_holiday))
        }
        else if(command == 2)
        {
            Holiday temp_holiday;
            
            puts("Update holiday");
            printf("Id: "); SAFE_SCANF(scanf("%d", &(temp_holiday.id)))
            
            if(storage_holiday_id_exists(storage, temp_holiday.id))
            {
                ui_read_holiday_data(&temp_holiday);
                VALIDATE_HOLIDAY(temp_holiday, storage_update_holiday(storage, &temp_holiday))
            }
            else
            {
                puts("That ID does not exist");
            }
        }
        else if(command == 3)
        {
            int id;
            puts("Delete holiday");
            printf("Id: "); SAFE_SCANF(scanf("%d", &id))
            
            if(storage_holiday_id_exists(storage, id))
            {
                storage_delete_holiday(storage, id);
            }
            else
            {
                puts("That ID does not exist");
            }
        } 
        else if(command == 4)
        {
            puts("List all holidays");
            storage_print_all(storage);
        }
        else if(command == 5)
        {
            char temp_type[HOLIDAY_TYPE_LENGTH];
            int read_chars;
            
            puts("Filter by type");
            printf("Type: ");
            read_chars = read_string(temp_type, HOLIDAY_TYPE_LENGTH);
            
            if(read_chars)
            {
                storage_filter_by_type(storage, temp_type);
                storage_print_all(storage);
            }

        }
        else if(command == 6)
        {
            int choice;
            float temp_price;
            
            puts("Filter by price");   
            printf("Greater than price(1) or less than price(-1): -1 or 1: "); SAFE_SCANF(scanf("%d", &choice)); 
            if(choice == -1 || choice == 1 || choice == 0)
            {
                printf("Price: "); SAFE_SCANF(scanf("%f", &temp_price))
                storage_filter_by_price(storage, temp_price, choice);
                storage_print_all(storage);
            }
        }
        else if(command == 7)
        {
            puts("Undo the last operation");
            if(storage_undo(storage))
            {
                printf("Last operation undone"); print_eol();
            }
            else
            {
                printf("The undo history is EMPTY"); print_eol();
            }
        }
        else if(command == 0) // exit
        {
            puts("Quiting...");
            break;
        }
        else if(command < 0 || command > 7) // command not identifed
        {
            printf("Command %d not found please try again", command);
            print_eol();
        }
    }
    
    storage_destroy(storage);
    return 1;
}