#pragma once
#include <cstdlib>
#include <cstdio>
#include <cstring>
#include <string>
#include <cctype>
#include <cstdint>
#include <ctime>
#include <cassert>
#include <unistd.h>
#include <fstream>
#include <sstream>
#include <fcntl.h>
#include <errno.h>
#include <iomanip>
#include <cstdarg>
#include <stdexcept>
#include <algorithm>

#include <sys/stat.h>
#include <sys/wait.h>
#include <sys/types.h>
#include <sys/ipc.h>

// USE: gcc <p.c>  -g -Wall -o <p>; and ./p

// typedef struct X { int x; } X;
// switch to true
#define DEBUG_MODE true

#define DEBUG_PUTS(_debug_s)    \
    if (DEBUG_MODE)             \
    {                           \
        printf("DEBUG: ");      \
        puts(_debug_s);         \
    }
#define DEBUG_PRINT(_debug_format, _debug_printf_args...)   \
    if (DEBUG_MODE)                                         \
    {                                                       \
        printf("DEBUG: ");                                  \
        printf(_debug_format, _debug_printf_args);          \
    }

#define ALARM_ATOMIC(code, seconds) alarm(seconds); code; alarm(0);

// pipe indexes
#define R 0
#define W 1

#define COUNT_OF(x) ((sizeof(x)/sizeof(0[x])) / ((size_t)(!(sizeof(x) % sizeof(0[x])))))

class File
{
public:
    static int32_t size(const char* filename) // returns -1 is files does not exist
    {
        std::ifstream in(filename, std::ifstream::ate | std::ifstream::binary);

        return (int32_t)in.tellg();
    }

    static bool exists(const char *name)
    {
        std::ifstream f(name);

        return f.good();
    }

    static void remove(const char *path)
    {
        if (::remove(path) < 0)
        {
            std::ostringstream out;
            out << "Error remove file= " << path << ", error=" << strerror(errno);
            throw std::runtime_error(out.str());
        }
    }

    static void removeCheck(const char *path)
    {
        if (exists(path) == true)
        {
            remove(path);
        }
        else
        {
            DEBUG_PRINT("file_delete_check - File '%s' does not exist. Can not delete!\n", path);
        }
    }
};

class Time
{
public:
    static std::string getTime() // in HH:MM::SS
    {
        std::time_t t = std::time(nullptr);
        std::tm *tm = std::localtime(&t);

        const int size = 16;
        char buffer[size];
        if (std::strftime(buffer, size, "%H:%M:%S",tm) == 0)
        {
            throw std::runtime_error("Error trying to getTime");
        }

        return std::string(buffer);
    }

    static std::string getDate() // in day:month:year
    {
        std::time_t t = std::time(nullptr);
        std::tm *tm = std::localtime(&t);

        const int size = 16;
        char buffer[size];
        if (std::strftime(buffer, size, "%d:%m:%Y",tm) == 0)
        {
            throw std::runtime_error("Error trying to getDate");
        }

        return std::string(buffer);
    }
};


class String
{
public:
    //-------------------------------------------------------------------------
    /** Returns a string converted to upper case.
     */
    static std::string toUpperCase(const std::string& str)
    {
        std::string name = str;
        std::transform(name.begin(), name.end(), name.begin(), ::toupper);
        return name;
    }   // toUpperCase

    //-------------------------------------------------------------------------
    /** Returns a string converted to lower case.
     */
    static std::string toLowerCase(const std::string& str)
    {
        std::string name = str;
        std::transform(name.begin(), name.end(), name.begin(), ::tolower);
        return name;
    }   // toLowerCase

    //-------------------------------------------------------------------------
    /** Splits a string into substrings separated by a certain character, and
     *  returns a std::vector of all those substring. E.g.:
     *  split("a b=c d=e",' ')  --> ["a", "b=c", "d=e"]
     *  \param s The string to split.
     *  \param c The character  by which the string is split.
     */
    static std::vector<std::string> split(const std::string& s, char c, bool keepSplitChar = false)
    {
        std::vector<std::string> result;

        try
        {
            std::string::size_type start = 0;
            while (start != std::string::npos && start < (unsigned int)s.size())
            {
                std::string::size_type i = s.find(c, start);
                if (i != std::string::npos)
                {
                    if (keepSplitChar)
                    {
                        int from = (int)start - 1;
                        if (from < 0) from = 0;

                        result.push_back(std::string(s, from, i - from));
                    }
                    else
                    {
                        result.push_back(std::string(s, start, i - start));
                    }
                    start = i + 1;
                }
                else   // end of string reached
                {
                    if (keepSplitChar)
                        result.push_back(std::string(s, start - 1));
                    else
                        result.push_back(std::string(s, start));
                    start = i;
                }
            }
        }
        catch (std::exception& e)
        {
           throw std::runtime_error("Error in split(std::string)");
        }

        return result;
    }   // split

    // ------------------------------------------------------------------------
    template <class T>
    static std::string toString (const T& any)
    {
        std::ostringstream oss;
        oss << any ;
        return oss.str();
    }   // toString template

    // ------------------------------------------------------------------------
    /** Specialisiation for bools to return 'true' or 'false'*/
    static inline std::string toString(const bool& b)
    {
        return (b ? "true" : "false");
    }    // toString(bool)
};

void safe_mkfifo(const char *pathname, mode_t mode)
{
    if (File::exists(pathname) == false)
    {
        if (mkfifo(pathname, mode) == -1)
        {
            perror("safe_makefifo - Failed to create named fifo.");
            exit(EXIT_FAILURE);
        }
    }
    else
    {
        DEBUG_PRINT("safe_makefifo - FIFO '%s' already exists. Clean it properly!\n", pathname);
    }
}

// O_RDONLY or O_WDONLY
int safe_open(const char *path, int oflag)
{
    int fh = open(path, oflag);

    // check if opened
    if (fh == -1)
    {
        perror("safe_open - Failed to open file.");
        exit(EXIT_FAILURE);
    }

    return fh;
}

void safe_close(int fildes)
{
    if (close(fildes) == -1)
    {
        perror("safe_close - Failed to close.");
        exit(EXIT_FAILURE);
    }
}

pid_t safe_fork()
{
    pid_t pid = fork();
    if (pid == -1)
    {
        perror("safe_fork - Failed to fork");
        exit(EXIT_FAILURE);
    }

    return pid;
}

void safe_pipe(int fildes[2])
{
    if (pipe(fildes) == -1)
    {
        perror("safe_pipe - Failed to open pipe");
        exit(EXIT_FAILURE);
    }
}
void close_pipe(int fildes[2])
{
    safe_close(fildes[R]);
    safe_close(fildes[W]);
}

void print_array(const int array[], const unsigned len)
{
    unsigned i;
    for (i = 0; i < len; i++)
    {
        printf("%d\t", array[i]);
    }
    printf("\n");
}

void print_matrix(int *matrix[], const unsigned rows, const unsigned columns)
{
    unsigned i;
    for (i = 0; i < rows; i++)
    {
        print_array(matrix[i], columns);
    }
}
key_t safe_ftok(const char *pathname, int proj_id)
{
    key_t rvalue = ftok(pathname, proj_id);
    if (rvalue == -1)
    {
        perror("safe_ftok - Failed to ftok");
        exit(EXIT_FAILURE);
    }

    return rvalue;
}

/*
** packi16() -- store a 16-bit int into a char buffer (like htons())
*/
void packi16(unsigned char *buf, unsigned int i)
{
    buf[0] = i>>8; buf[1] = i;
}

/*
** packi32() -- store a 32-bit int into a char buffer (like htonl())
*/
void packi32(unsigned char *buf, unsigned long int i)
{
    buf[0] = i>>24; buf[1] = i>>16;
    buf[2] = i>>8;  buf[3] = i;
}


/*
** unpacki16() -- unpack a 16-bit int from a char buffer (like ntohs())
*/
int unpacki16(unsigned char *buf)
{
    unsigned int i2 = ((unsigned int)buf[0]<<8) | buf[1];
    int i;

    // change unsigned numbers to signed
    if (i2 <= 0x7fffu) { i = i2; }
    else { i = -1 - (unsigned int)(0xffffu - i2); }

    return i;
}

/*
** unpacku16() -- unpack a 16-bit unsigned from a char buffer (like ntohs())
*/
unsigned int unpacku16(unsigned char *buf)
{
    return ((unsigned int)buf[0]<<8) | buf[1];
}

/*
** unpacki32() -- unpack a 32-bit int from a char buffer (like ntohl())
*/
long int unpacki32(unsigned char *buf)
{
    unsigned long int i2 = ((unsigned long int)buf[0]<<24) |
                           ((unsigned long int)buf[1]<<16) |
                           ((unsigned long int)buf[2]<<8)  |
                           buf[3];
    long int i;

    // change unsigned numbers to signed
    if (i2 <= 0x7fffffffu) { i = i2; }
    else { i = -1 - (long int)(0xffffffffu - i2); }

    return i;
}

/*
** unpacku32() -- unpack a 32-bit unsigned from a char buffer (like ntohl())
*/
unsigned long int unpacku32(unsigned char *buf)
{
    return ((unsigned long int)buf[0]<<24) |
           ((unsigned long int)buf[1]<<16) |
           ((unsigned long int)buf[2]<<8)  |
           buf[3];
}


/*
** pack() -- store data dictated by the format string in the buffer
**
**   bits |signed   unsigned    string
**   -----+----------------------------------
**      8 |   c        C
**     16 |   h        H
**     32 |   l        L
**      - |                      s
**
**  (16-bit unsigned length is automatically prepended to strings)
*/

unsigned int pack(unsigned char *buf, char *format, ...)
{
    va_list ap;

    signed char c;              // 8-bit
    unsigned char C;

    int h;                      // 16-bit
    unsigned int H;

    long int l;                 // 32-bit
    unsigned long int L;

//    char *s;                    // strings
//    unsigned int len;

    unsigned int size = 0;

    va_start(ap, format);

    for(; *format != '\0'; format++)
    {
        switch(*format) {
        case 'c': // 8-bit
            c = (signed char)va_arg(ap, int); // promoted
            buf[size] = c;
            size++;
            break;

        case 'C': // 8-bit unsigned
            C = (unsigned char)va_arg(ap, unsigned int); // promoted
            buf[size] = C;
            size++;
            break;

        case 'h': // 16-bit
            h = va_arg(ap, int);
            packi16(buf + size, h);
            size += 2;
            break;

        case 'H': // 16-bit unsigned
            H = va_arg(ap, unsigned int);
            packi16(buf + size, H);
            size += 2;
            break;

        case 'l': // 32-bit
            l = va_arg(ap, long int);
            packi32(buf + size, l);
            size += 4;
            break;

        case 'L': // 32-bit unsigned
            L = va_arg(ap, unsigned long int);
            packi32(buf + size, L);
            size += 4;
            break;

//        case 's': // string
//            s = va_arg(ap, char*);
//            len = strlen(s);
//            size += len + 2;
//            packi16(buf, len);
//            buf += 2;
//            memcpy(buf, s, len);
//            buf += len;
//            break;
        }
    }
    va_end(ap);

    return size;
}

/*
** unpack() -- unpack data dictated by the format string into the buffer
**
**   bits |signed   unsigned    string
**   -----+----------------------------------
**      8 |   c        C
**     16 |   h        H
**     32 |   l        L
**      - |                       s
**
**  (string is extracted based on its stored length, but 's' can be
**  prepended with a max length)
*/
void unpack(unsigned char *buf, char *format, ...)
{
    va_list ap;

    signed char *c;              // 8-bit
    unsigned char *C;

    int *h;                      // 16-bit
    unsigned int *H;

    long int *l;                 // 32-bit
    unsigned long int *L;

    unsigned int size = 0;
//    char *s;
//    unsigned int len, maxstrlen=0, count;

    va_start(ap, format);
    for(; *format != '\0'; format++) {
        switch(*format)
        {
        case 'c': // 8-bit, TODO test
            c = va_arg(ap, signed char*);
            if (buf[size] <= 0x7f)
                { *c = buf[size];} // re-sign
            else
                { *c = -1 - (unsigned char)(0xffu - buf[size]); }
            size++;
            break;

        case 'C': // 8-bit unsigned
            C = va_arg(ap, unsigned char*);
            *C = buf[size];
            size++;
            break;

        case 'h': // 16-bit
            h = va_arg(ap, int*);
            *h = unpacki16(buf + size);
            size += 2;
            break;

        case 'H': // 16-bit unsigned
            H = va_arg(ap, unsigned int*);
            *H = unpacku16(buf + size);
            size += 2;
            break;

        case 'l': // 32-bit
            l = va_arg(ap, long int*);
            *l = unpacki32(buf + size);
            size += 4;
            break;

        case 'L': // 32-bit unsigned
            L = va_arg(ap, unsigned long int*);
            *L = unpacku32(buf + size);
            size += 4;
            break;

//        case 's': // string
//            s = va_arg(ap, char*);
//            len = unpacku16(buf);
//            buf += 2;
//            if (maxstrlen > 0 && len > maxstrlen) count = maxstrlen - 1;
//            else count = len;
//            memcpy(s, buf, count);
//            s[count] = '\0';
//            buf += len;
//            break;

        default:
            break;
//            if (isdigit(*format)) // track max str len
//            {
//                maxstrlen = maxstrlen * 10 + (*format-'0');
//            }
        }

//        if (!isdigit(*format)) maxstrlen = 0;
    }
    va_end(ap);
}
