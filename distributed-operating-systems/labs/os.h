#pragma once
#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#include <ctype.h>
#include <unistd.h>
#include <limits.h>
#include <fcntl.h>
#include <errno.h>
#include <assert.h>
#include <time.h>
#include <signal.h>

#include <sys/stat.h>
#include <sys/wait.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <sys/sem.h> // sys v semaphore
#include <sys/shm.h>

#include <sys/socket.h>
#include <netinet/in.h>
#include <netinet/udp.h>
#include <netinet/tcp.h>
#include <arpa/inet.h>


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

// pipe indexes
#define R 0
#define W 1

bool file_exists(const char *path)
{
    return access(path, F_OK) == 0;
}

void file_delete(const char *path)
{
    if (unlink(path) == -1)
    {
        perror("safe_unlink - Failed to unlink.");
        exit(EXIT_FAILURE);
    }
}

void file_delete_check(const char *path)
{
    if (file_exists(path) == true)
    {
        file_delete(path);
    }
    else
    {
        DEBUG_PRINT("file_delete_check - File '%s' does not exist. Can not delete!\n", path);
    }
}

unsigned write_int(int socket, int integer)
{
    ssize_t rvalue = write(socket, &integer, sizeof(integer));
    if (rvalue == -1)
    {
        perror("send_int - Failed to send integer");
        exit(EXIT_FAILURE);
    }

    return rvalue;
}

unsigned read_int(int socket, int *integer)
{
    ssize_t rvalue = read(socket, integer, sizeof(*integer));

    if (rvalue == 0)
    {
        DEBUG_PUTS("read_int - no process has the pipe open for writing");
    }
    else if (rvalue == -1)
    {
        perror("read_int - Failed to read int");
        exit(EXIT_FAILURE);
    }

    return rvalue;
}

unsigned write_double(int socket, double number)
{
    ssize_t rvalue = write(socket, &number, sizeof(number));
    if (rvalue == -1)
    {
        perror("write_double - Failed to send double");
        exit(EXIT_FAILURE);
    }

    return rvalue;
}

unsigned read_double(int socket, double *number)
{
    ssize_t rvalue = read(socket, number, sizeof(*number));

    if (rvalue == 0)
    {
        DEBUG_PUTS("read_double - no process has the pipe open for writing");
    }
    else if (rvalue == -1)
    {
        perror("read_double - Failed to read double");
        exit(EXIT_FAILURE);
    }

    return rvalue;
}

unsigned write_string(int socket, char string[])
{
    int length = strlen(string) + 1;

    // send length
    write_int(socket, length);

    // send string
    ssize_t rvalue = write(socket, string, length);
    if (rvalue == -1)
    {
        perror("send_string - Failed to write string");
        exit(EXIT_FAILURE);
    }

    return rvalue;
}

unsigned read_string(int socket, char string[])
{
    // receive length
    int length;
    read_int(socket, &length);

    int rvalue = read(socket, string, length);

    if (rvalue == 0)
    {
        DEBUG_PUTS("read_string - no process has the pipe open for writing");
    }
    else if (rvalue == -1)
    {
        perror("read_string - Failed to read string");
        exit(EXIT_FAILURE);
    }

    return rvalue;
}


void safe_mkfifo(const char *pathname, mode_t mode)
{
    if (file_exists(pathname) == false)
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

FILE * safe_fopen(const char *path, const char *mode)
{
    FILE *fp = fopen(path, mode);

    if (fp == NULL)
    {
        perror("safe_fopen - Failed to open file.");
        exit(EXIT_FAILURE);
    }

    return fp;
}

void safe_close(int fildes)
{
    if (close(fildes) == -1)
    {
        perror("safe_close - Failed to close.");
        exit(EXIT_FAILURE);
    }
}

void safe_fclose(FILE *fp)
{
    if (fclose(fp) != 0)
    {
        perror("safe_fclose - Failed to close file.");
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

int safe_socket(int domain, int type, int protocol)
{
    int rvalue = socket(domain, type, protocol);
    if (rvalue == -1)
    {
        perror("safe_socket - Failed to create socket");
        exit(EXIT_FAILURE);
    }

    return rvalue;
}

int safe_socket_tcp()
{
    return safe_socket(AF_INET, SOCK_STREAM, 0);
}

int safe_socket_udp()
{
    return safe_socket(AF_INET, SOCK_DGRAM, 0);
}

// DANGEROUS when address is allocated on the heap
void safe_bind(int socket, const struct sockaddr_in *address)
{
    if (bind(socket, (struct sockaddr *)address, sizeof(*address)) != 0)
    {
        perror("safe_bind - Failed to bind");
        exit(EXIT_FAILURE);
    }
}

void safe_listen(int sockfd, int backlog)
{
    if (listen(sockfd, backlog) != 0)
    {
        perror("safe_bind - Failed to listen");
        exit(EXIT_FAILURE);
    }
}

// DANGEROUS when address is allocated on the heap
int safe_accept(int socket, struct sockaddr_in *address)
{
    socklen_t address_len = sizeof(*address);
    int rvalue = accept(socket, (struct sockaddr *)address, &address_len);
    if (rvalue == -1)
    {
        perror("safe_accept - Failed to create socket");
        exit(EXIT_FAILURE);
    }

    return rvalue;
}

// DANGEROUS when address is allocated on the heap
void safe_connect(int socket, const struct sockaddr_in *address)
{
    if (connect(socket, (struct sockaddr *)address, sizeof(*address)) == -1)
    {
        perror("safe_connect - Failed to connect");
        exit(EXIT_FAILURE);
    }
}

// DANGEROUS when address is allocated on the heap
ssize_t safe_recvfrom(int sockfd, void *buf, size_t len, struct sockaddr_in *src_addr)
{
    socklen_t src_addr_len = sizeof(*src_addr);
    ssize_t rvalue = recvfrom(sockfd, buf, len, 0, (struct sockaddr *)src_addr, &src_addr_len);
    if (rvalue == -1)
    {
        perror("safe_recvfrom - Failed to recieve");
        exit(EXIT_FAILURE);
    }

    return rvalue;
}

// DANGEROUS when address is allocated on the heap
ssize_t safe_sendto(int sockfd, const void *buf, size_t len, const struct sockaddr_in *dest_addr)
{
    ssize_t rvalue = sendto(sockfd, buf, len, 0, (struct sockaddr *)dest_addr, sizeof(*dest_addr));
    if (rvalue == -1)
    {
        perror("safe_sendto - Failed to semd");
        exit(EXIT_FAILURE);
    }

    return rvalue;
}
