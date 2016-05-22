// LAB 1
// 14. Determine a calendar data (as year, month, day) starting from two integer numbers
// representing the year and the day number inside that year.

#include <math.h>
#include <stdio.h>
#include <stdlib.h>

#define DEBUG 0

// Check if the year is a leap year
// input:
//      year - the year we want to check
// return: 1 if true 0 otherwise
int is_leap_year(int year)
{
    if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
        return 1;

    return 0;
}

// Read the data from the keyboard
// input:
//      *year - a pointer to the year
//      *day  - a pointer to the day
// return 1 if everything is ok 0 otherwise
int read_data(int* year, int* day)
{
    printf("The year: ");
    if (scanf("%d", year) == 0)
    {
        printf("The year is not an number aborting");
        return 0;
    }

    printf("The day: ");
    if (scanf("%d", day) == 0)
    {
        printf("The day is not an number aborting");
        return 0;
    }
    printf("\n");

    return 1;
}

// Validate the year and the day
// input:
//      year - the given year
//      day  - the given day
// return 1 if everything is ok 0 otherwise
int validate_data(int year, int day)
{
    int limit_day = 365; // normal year
    if (is_leap_year(year))
        limit_day++; // 366 leap year

    // check year
    if (year <= 0)
    {
        printf("The year is not in AD >= 0\n");
        return 0;
    }

    // check day
    if (day <= 0)
    {
        printf("The day must be strictly positive\n");
        return 0;
    }
    if (day > limit_day)
    {
        printf("Not a valid day bigger than %d\n", limit_day);
        return 0;
    }

    return 1;
}

// Validate the year and the day
// input:
//      year - the given year
//      day  - the given day
//      *found_month - where the result of our month will be stored
//      *found_day  - where the result of our day will be stored
// return 1 if everything is ok 0 otherwise
int find_month_and_day(int year, int day, int* found_month, int* found_day)
{
    int months[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}, i, temp_day = 0;

    // make february longer
    if (is_leap_year(year))
        months[1]++; // 29 days

    // iterate and add the months
    for (i = 0; i < 12; i++)
    {
        temp_day += months[i];

        if (DEBUG)
            printf("DEBUG: i = %d, temp_day = %d, day = %d\n", i, temp_day, day);

        // found the month i
        if (temp_day >= day)
        {
            *found_month = i + 1; // because of our indexing add +1
            if (i > 0)
            {
                // subtract from the temp_day which represents the
                // days from the beginning of the year until the end
                // of the month, the user given day (aka temp_day - day)
                // then substract from that the number of days in this
                // month
                *found_day = abs(temp_day - day - months[i]);

                return 1;
            }
            // first month do nothing
            *found_day = day;

            return 1;
        }
    }

    // we should not reach here
    return 0;
}

int main()
{
    int read_year, read_day, found_month, found_day;

    char* months_map[] = {"NULL", // not valid index 0
                          "January",
                          "February",
                          "March",
                          "April",
                          "May",
                          "June",
                          "July",
                          "August",
                          "September",
                          "October",
                          "November",
                          "December"};

    if (read_data(&read_year, &read_day) == 0)
        exit(1);

    if (DEBUG)
        printf("DEBUG: read from user --> year = %d, day = %d \n", read_year, read_day);

    if (validate_data(read_year, read_day) == 0)
        exit(1);

    if (find_month_and_day(read_year, read_day, &found_month, &found_day) == 0)
    {
        printf("An internal error occured");
        exit(0);
    }

    // print the result
    printf("The full date (day/month/year): %02d/%02d/%04d \n", found_day, found_month, read_year);
    printf("The full date with name (day/month/year): %02d/%s/%04d \n", found_day, months_map[found_month], read_year);

    return 0;
}
