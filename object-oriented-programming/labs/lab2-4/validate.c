#include <string.h>
#include <ctype.h>

int validate_type(const char type[])
{
    int i, len = strlen(type);
    for(i = 0; i < len; i++)
    {
        if(isdigit(type[i]))
        {
            return 0;
        }
    }
    
    return 1;
}

int validate_destination(const char description[])
{
    int i, len = strlen(description);
    for(i = 0; i < len; i++)
    {
        if(isdigit(description[i]))
        {
            return 0;
        }
    }
    
    return 1;
}
