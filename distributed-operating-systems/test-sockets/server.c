/*
Avem un client si server TCP
Clientul trimite un string
Serverul returneaza stringul inversat
*/
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <unistd.h>
#include <errno.h>
#include <time.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

#define PORT 19002
#define MAX_BUFFER 1024
#define MAX_CLIENTS 10

void string_reverse(char string[], int n)
{
    int i, limit = n/2;
    for (i = 0; i < limit; i++)
    {
        char temp = string[i];
        string[i] = string[n - i - 1];
        string[n - i - 1] = temp;
    }
}

int main()
{
    int sd = socket(AF_INET, SOCK_STREAM, 0);
    if (sd < 0)
    {
        perror("failed to create socket");
        exit(1);
    }
    struct sockaddr_in adress;
    adress.sin_family = AF_INET;
    adress.sin_port = htons(PORT);
    adress.sin_addr.s_addr = INADDR_ANY;
    
    if (bind(sd, (struct sockaddr *)&adress, sizeof(adress)) < 0)
    {
        perror("failed to bind");
        exit(1);
    }
    
    if (listen(sd, SOMAXCONN) < 0)
    {
        perror("failed to listen");
        exit(1);
    }
    
    while (1)
    {
        // open client connection
        struct sockaddr_in client_addr;
        socklen_t client_addr_len = sizeof(client_addr);
        int cd = accept(sd, (struct sockaddr *)&client_addr, &client_addr_len);
        if (cd < 0)
        {
            perror("Failed to accept");
            close(cd);
            continue;
        }
        
        // read from client
        char buffer[MAX_BUFFER];
        int read_n = read(cd, buffer, MAX_BUFFER - 1);
        if (read_n == -1)
        {
            perror("Failed to read");
            close(cd);
            continue;
        }
        
        // reverse
        buffer[read_n] = 0;
        string_reverse(buffer, read_n);
        
        // send back
        if (write(cd, buffer, read_n) == -1)
        {
            perror("failed to write");
        }
        
        close(cd);
    }

    close(sd);
    return 0;
}
