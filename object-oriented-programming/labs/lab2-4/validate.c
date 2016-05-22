#include <ctype.h>
#include <string.h>

int validate_type(const char type[])
{
    for (int i = 0, len = strlen(type); i < len; i++)
    {
        if (isdigit(type[i]))
            return 0;
    }

    return 1;
}

int validate_destination(const char description[])
{
    for (int i = 0, len = strlen(description); i < len; i++)
    {
        if (isdigit(description[i]))
            return 0;
    }

    return 1;
}
