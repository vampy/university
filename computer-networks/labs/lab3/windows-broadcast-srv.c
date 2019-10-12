#include"winsock2.h"
#include<iostream>
#include<conio.h>
 
using namespace std;
 
#define MYPORT 9009    // the port users will be connecting to
 
 
int main()
{
    WSADATA wsaData;
    WSAStartup(MAKEWORD(2,2), &wsaData);
 
    SOCKET sock;
    sock = socket(AF_INET,SOCK_DGRAM,0);
 
    char broadcast = '1';
 
 
    if(setsockopt(sock,SOL_SOCKET,SO_BROADCAST,&broadcast,sizeof(broadcast)) < 0)
    {
        cout<<"Error in setting Broadcast option";
        closesocket(sock);
        return 0;
    }
 
    struct sockaddr_in Recv_addr;   
    struct sockaddr_in Sender_addr; 
 
    int len = sizeof(struct sockaddr_in);
 
    char recvbuff[50];
    int recvbufflen = 50;
    char sendMSG[]= "Broadcast message from READER";
 
    Recv_addr.sin_family       = AF_INET;         
    Recv_addr.sin_port         = htons(MYPORT);    
    Recv_addr.sin_addr.s_addr  = INADDR_ANY; 
 
    if (bind(sock,(sockaddr*)&Recv_addr, sizeof (Recv_addr)) < 0)
    {
        cout<<"Error in BINDING"<<WSAGetLastError();
        _getch();
        closesocket(sock);
        return 0;
    }
 
    recvfrom(sock,recvbuff,recvbufflen,0,(sockaddr *)&Sender_addr,&len);
 
    cout<<"\n\n\tReceived Message is : "<<recvbuff;
    cout<<"\n\n\tPress Any to send message";
    _getch();
 
 
    if(sendto(sock,sendMSG,strlen(sendMSG)+1,0,(sockaddr *)&Sender_addr,sizeof(Sender_addr)) < 0)
    {
        cout<<"Error in Sending."<<WSAGetLastError();
        cout<<"\n\n\t\t Press any key to continue....";
        _getch();
        closesocket(sock);
        return 0;
    }
    else
        cout<<"\n\n\n\tREADER sends the broadcast message Successfully";
 
 
    cout<<"\n\n\tpress any key to CONT...";
    _getch();
 
    closesocket(sock);
    WSACleanup();
}
