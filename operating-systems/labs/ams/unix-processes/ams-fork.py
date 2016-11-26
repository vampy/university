
Uproc : Unix Processes
Prezentarea proiectului

Five examples of using Unix processes are presented.
The examples are implementation in the C language. 
At the finish of each example, the equivqlent Python
source is presented.

The first example illustrates the link between a parent process and 
and a son's created by fork.

The second example shows different aspects of some use exec calls.

The last three examples present three specific problems that
using combinations of system calls fork, exec, wait, exit, and system


Example 1, use fork.
--------------------

1. The source fork1.c:
#include <stdio.h>
#include <stdlib.h>
main() {
    int p;
    p=fork();
    if (p == -1) {perror("fork imposibil!"); exit(1);}    
    if (p == 0) { 
        printf("Fiu: pid=%d, ppid=%d\n", getpid(), getppid());
    } else { 
        printf("Parinte: pid=%d ppid=%d\n", getpid(), getppid());
        //wait(0); 
        printf("Terminat fiul\n");      
    }
}

Run the fork1 without wait (is comment):

Parinte: pid=2704 ppid=2197
Terminat fiul
Fiu: pid=2705, ppid=2704

Rulare fork1 cu wait:

Parinte: pid=2715 ppid=2197
Fiu: pid=2716, ppid=2715
Terminat fiul

Reason: the termination message is given from the parent. 
In absence of wait, it is possible that the parent obtain the 
control before his son and display the message before
that son display his message. If wait appears, then
father expects his son before the actual completion
end message display.

The equivalent Python source:
fork1.py
--------

import os
p=os.fork()
if p == -1:
    print "fork imposibil!"
    exit(1)    
if p == 0: 
    print "Fiu: pid="+`os.getpid()`+", ppid="+`os.getppid()`
else: 
    print "Parinte: pid="+`os.getpid()`+" ppid="+`os.getppid()`
    #os.wait() 
    print "Terminat fiul"     


2. The source fork2.c :
#include <stdio.h>
#include <stdlib.h>
main() {
    int p;
    printf("Parinte: pid=%d, ppid=%d\n", getpid(), getppid());
    p=fork();
    if(p==-1){perror("fork imposibil!"); exit(1);} 
    if(p==0){
        printf("Fiu: pid=%d, ppid=%d\n", getpid(), getppid());
        //exit(2);
    }        
    printf("pid=%d, ppid=%d\n", getpid(), getppid());
}

Run fork2 without exit(2) (is comment):

Parinte: pid=2743, ppid=2197
pid=2743, ppid=2197
Fiu: pid=2744, ppid=2743
pid=2744, ppid=2743

Rulare fork2 cu exit(2):

Parinte: pid=2755, ppid=2197
pid=2755, ppid=2197
Fiu: pid=2756, ppid=2755

Reason: The last print is execute belongs to the parent and 
to the son. If exit(2) appears, the son no execute last print.

The equivalent Python source:
 fork2.py
 --------

import os
print "Parinte: pid="+`os.getpid()`+", ppid="+`os.getppid()`
p = os.fork()
if p == -1:
    print "fork imposibil!"
    exit(1)
if p == 0:
    print "Fiu: pid="+`os.getpid()`+", ppid="+`os.getppid()`
    exit(2)
print "pid="+`os.getpid()`+", ppid="+`os.getppid()`


3. Sursa fork3.c:
#include <stdio.h>
#include <stdlib.h>
main() {
    int p, i;
    p=fork();
    if (p == -1) {perror("fork imposibil!"); exit(1);}    
    if (p == 0) { 
        for (i=0; i<10; i++)
            printf("%d. Fiu: pid=%d, ppid=%d\n", i, getpid(), getppid());
    } else { 
        for (i=0; i<10; i++)
            printf("%d. Parinte: pid=%d ppid=%d\n", i, getpid(), getppid());
    }
}

Results:

0. Parinte: pid=2708 ppid=1768
1. Parinte: pid=2708 ppid=1768
2. Parinte: pid=2708 ppid=1768
0. Fiu: pid=2715, ppid=2708
3. Parinte: pid=2708 ppid=1768
1. Fiu: pid=2715, ppid=2708
4. Parinte: pid=2708 ppid=1768
2. Fiu: pid=2715, ppid=2708
5. Parinte: pid=2708 ppid=1768
3. Fiu: pid=2715, ppid=2708
6. Parinte: pid=2708 ppid=1768
4. Fiu: pid=2715, ppid=2708
7. Parinte: pid=2708 ppid=1768
5. Fiu: pid=2715, ppid=2708
8. Parinte: pid=2708 ppid=1768
6. Fiu: pid=2715, ppid=2708
9. Parinte: pid=2708 ppid=1768
7. Fiu: pid=2715, ppid=2708
8. Fiu: pid=2715, ppid=2708
9. Fiu: pid=2715, ppid=2708

Reason: mixing the output of the parent with son
execution of the above is just one of many possible.
If you are running several times, is observed each
on other (possible) mixing. Everything depends on which
between the two processes get first the access.

The equivalent Python source:
fork3.py
--------

import os
p = os.fork()
if p == -1:
    print "fork imposibil!"
    exit(1)
if p == 0: 
    for i in range(10):
        print `i`+". Fiu: pid="+`os.getpid()`+", ppid="+`os.getppid()`
else: 
    for i in range(10):
        print `i`+". Parinte: pid="+`os.getpid()`+" ppid="+`os.getppid()`


Example 2: use simple execl, execlp, execv:
--------------------------------------------------

These three programs, though different, have the same effect.
All three use an exec command type, for the launch of it:

ls -l

These three sources, ignore at the moment the comments, are:
                 ----------------------------------------------
                          
Source execl.c:
#include<stdio.h>
#include<unistd.h>
main() {
    printf("Urmeaza rezultatul executiei comenzii ls:\n");
    execl("/bin/ls", "/bin/ls", "-l", NULL);
    //execl("/bin/ls","/bin/ls","-l","execl.c", "fork1.c", "xx", NULL);
    //execl("/bin/ls","/bin/ls","-l","*.c", NULL);
    printf("Aici nu se ajunge decat in urma unei erori exec\n");
}

Source execlp.c:
#include<stdio.h>
#include<unistd.h>
main() {
    printf("Urmeaza rezultatul executiei comenzii ls:\n");
    execlp("ls", "ls", "-l", NULL) == -1;
    printf("Aici nu se ajunge decat in urma unei erori exec\n");
//    if (execlp("ls","ls","-l",NULL) == -1) {
//      printf("Aici nu se ajunge decat in urma unei erori exec\n");
//      perror("Aici se impune un mesaj explicativ; sistemul raspunde");
//    }
}

Source execv.c:
#include<stdio.h>
#include<unistd.h>
main() {
    char* argv[3];
    argv[0] = "/bin/ls";
    argv[1] = "-l";
    argv[2] = NULL;
    printf("Urmeaza rezultatul executiei comenzii ls:\n");
    execv("/bin/ls",argv);
    printf("Aici nu se ajunge decat in urma unei erori exec\n");
}
 
The effect to each of the programs are:

Urmeaza rezultatul executiei comenzii ls:
total 184
-rwxr-xr-x 1 florin florin 7176 2011-03-17 16:47 a.out
-rwxrwxrwx 1 florin florin  340 2011-03-17 16:43 execl.c
-rwxrwxrwx 1 florin florin  404 2011-03-17 16:43 execlp.c
-rwxrwxrwx 1 florin florin  370 2011-03-17 16:43 execv.c
-rwxrwxrwx 1 florin florin  364 2011-03-17 15:45 fork1.c
-rw-r--r-- 1 florin florin  353 2011-03-17 16:06 fork2.c
-rw-r--r-- 1 florin florin  386 2011-03-17 16:10 fork3.c

1.
The first program uses excl. Therefore is need to specified
with absolute path, /bin/ls. Follow the argument list
command line (hence the doubling occurs first argument).

The second uses exclp, so the order is sought in
directories in the PATH, so you just specify ls.

The third uses execv. Like the first, specific
absolute path. This program prepares in advance on a
array of three pointers to put the arguments of the command 
line and the NULL pointer to mark the end of the table.
The compiler automatically allocate space for each constant
string. After the assignments a[0] = --- and a[1] = ---
that string addressed are assigned.
If necessary, such a picture can be dynamically allocates
in the heap (via malloc), then it will initialize the
corresponding values ??using the specific methods the C language

Primul program foloseste excl. De aceea comanda se specifica
cu calea completa /bin/ls. Urmeaza lista argumentelor din
linia de comanda (de aceea apare dublarea primului argument).

Al doilea foloseste exclp, deci comanda este cautata in 
directoarele din PATH, de aceea se specifica doar ls.

Al treilea foloseste execv. La fel ca primul, specifica
calea absoluta. Acest program pregateste in prelabil un 
tablou cu trei pointeri la stringuri in care se pun cele
doua argumente ale liniei de comanda si pointerul NULL
ce marcheaza sfarsitul tabloului. Compilatorul in mod
automat aloca spatiu de memorie pentru fiecare constanta
string. In urma atribuirii a[0] = --- sau a[1] = ---
se atribuie adresa stringului respectiv.
If is necessary, the arry can be dynamic allocate in the heap,
and then to assign the values to it, as in the C language.

2. 
It can be seen, studying the runs, it will not display text
in the second printf, every program launched.
Simply change the first argument to exec, ut xxxx instead of the
ls for example, and will receive:

Urmeaza rezultatul executiei comenzii ls:
Aici nu se ajunge decat in urma unei erori exec

3. 
In the spirit of the point 2, the desire to present
programs as simple as possible, we "broke" a golden rule
in C programming:
"To be tested each time the result returned by a
C function or system call! "
Consequently, each call to exec should do so
as shown in the execlp.c comments:

    if (execlp("ls","ls","-l",NULL) == -1) {
        printf("Aici nu se ajunge decat in urma unei erori exec\n");
        perror("Aici se impune un mesaj explicativ; sistemul raspunde");
    }

See man exec. 
If replace an exec call with a sequence like above, and replace ls with
xxxx it obtains:

Aici nu se ajunge decat in urma unei erori exec
Aici se impune un mesaj explicativ; sistemul raspunde: No such file or directory

4.
In the source execl.c, two comment lines with execl will appears. Replacing  
one by one execl with another, it will obtain:

Urmeaza rezultatul executiei comenzii ls:

/bin/ls: cannot access xx: No such file or directory
-rwxrwxrwx 1 florin florin 340 2011-03-17 17:39 execl.c
-rwxrwxrwx 1 florin florin 364 2011-03-17 15:45 fork1.c

Urmeaza rezultatul executiei comenzii ls:
/bin/ls: cannot access *.c: No such file or directory

In first case the effects is:

ls -l execl.c fork1.c xx

and the file xx do not exists in the current directory.

In the second case is the command:

ls -l *.c

but that is not performed as we expected!
-----------------------------------------
Why? Because the specification *.c is a a generic 
specification for the file, but only shell "knows "
that replaces the specification with all c files from directory.
The same thing happens with the assessment of environment variables,
${---}, reverse apostrofy construction ( `---` )
redirect I/O standard, etc.
These processing are specific for shell, its are not treated by exec.
If is necessary, the processing must be done
C program before the call to exec!
Instead, the system calls treated these replaces: ("ls-l *. c")

The equivalent Python sources:
execl.py
--------

import os
print "Urmeaza rezultatul executiei comenzii ls:"
os.execl("/bin/ls", "/bin/ls", "-l");
#os.execl("/bin/ls","/bin/ls","-l","execl.c", "fork1.c", "xx")
#os.execl("/bin/ls","/bin/ls","-l","*.c")
print "Aici nu se ajunge decat in urma unei erori exec"
 
execlp.py
---------

import os
print "Urmeaza rezultatul executiei comenzii ls:"
os.execlp("ls", "ls", "-l")
print "Aici nu se ajunge decat in urma unei erori exec"
#if os.execlp("ls","ls","-l") == -1:
#    print "Aici nu se ajunge decat in urma unei erori exec"
#    print "Aici se impune un mesaj explicativ; sistemul raspunde"
 
execv.py
--------

import os
argv = ["/bin/ls", "-l"]
print "Urmeaza rezultatul executiei comenzii ls:"
os.execv("/bin/ls",argv)
print "Aici nu se ajunge decat in urma unei erori exec"


Example 3: How pear of the nenule numbers hawe the even sum?
-----------------------------------------------------------

The problem is trivially simple, but suitable for
exemplify the use of fork, wait and exit.

This statement of the problem: give at the command line some
pair of integers. The program will be created, for each two 
consecutive arguments from the command line a son process.
Either of the son process back the return code:
     0 if the sum is even,
     1 if sum is odd,
     2 if one of the arguments is 0 or not integer.
The parent wait after finish the sons and display the result.

The source paritate.c is:

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>
main(int argc, char* argv[]) {
    int pare = 0, impare = 0, nenum = 0, i, n1, n2;
    for (i = 1; i < argc-1; i += 2) {
        if (fork() == 0) {
            n1 = atoi(argv[i]);   // atoi intoarce 0
            n2 = atoi(argv[i+1]); // si la nenumeric 
            if (n1 == 0 || n2 == 0) exit(2);
            if ((n1 + n2) % 2 == 0) exit(0);
            else                    exit(1);
            // Aici se termina fiecare fiu 
        }
    }
    // Parintele asteapta terminarile fiilor
    for (i = 1; i < argc-1; i += 2) {
        wait(&n1);
        switch (WEXITSTATUS(n1)) {
            case 0: pare++;break;
            case 1: impare++;break;
            default: nenum++;
        }
    }    
    printf("Pare %d, impare %d, nenumerice %d\n",pare, impare, nenum);
}

At the end, every child return an appropriate return code.
Father receives in an entire configuration bits n1
among which there is the return code value.
Function (macro in reality) WEXITSTATUS extracted the code value.

The equivalent Python source:
paritate.py
-----------

import sys
import os
import string
pare, impare, nenum = 0, 0, 0
for i in range(1,len(sys.argv)-1,2):
    if os.fork() == 0:
        try   : n1 = string.atoi(sys.argv[i])
        except: n1 = 0
        try   : n2 = string.atoi(sys.argv[i+1]) 
        except: n2 = 0
        if n1 == 0 or n2 == 0: exit(2)
        if (n1 + n2) % 2 == 0: exit(0)
        else                 : exit(1)
        # Aici se termina fiecare fiu 
# Parintele asteapta terminarile fiilor
for i in range(1,len(sys.argv)-1,2):
    n1 = os.wait()
    if os.WEXITSTATUS(n1[1]) == 0  : pare += 1
    elif os.WEXITSTATUS(n1[1]) == 1: impare += 1
    else                     : nenum += 1
print "Pare "+`pare`+", impare "+`impare`+", nenumerice "+`nenum`


Example 4: A program for compiling and running another program.
--------------------------------------------------------------

The example has the same effect as the following shell script:
#!/bin/sh
if gcc -o ceva $1
then ./ceva
else echo "erori de compilare"
fi

We do will implement in sh, but we use the following C program instead,
the source compilerun.c:
#include<stdio.h>
#include<unistd.h>
#include<string.h>
#include <sys/wait.h>
main(int argc, char* argv[]) {
    char com[200];
    strcpy(com, "gcc -o ceva "); // fabricat comanda
    strcat(com, argv[1]);
    if (WEXITSTATUS(system(com)) == 0)
        execl("./ceva","./ceva", NULL);
    printf("Erori de compilare\n");
}

To compile we use: 

gcc -o comprun compilerun.c

To run we use, for examplep

./comprun fork1.c

As a result, when compiling the source argument (fork1.c) is correct,
gcc compiler then creates the file and return something
a return code, then something is released by execl.
If compilation fails, it will print only message.

The equivalent Python source:
compilerun.py
-------------

import sys
import os
com = "gcc -o ceva "+sys.argv[1]
if os.WEXITSTATUS(os.system(com)) == 0:
    os.execl("./ceva","./ceva")
print "Erori de compilare"
 

Example 5: Simultaneous processing of multiple text files.
-------------------------------------------------- ----------

We want to turn a text file into another text file with the 
same content, but all the words he begin with a capital letter.
Such a program will be called:

capitalizare inputfile outputfile
 
We aim to process multiple such files.
Therefore we will create a master process, which gets from the 
command line the file names whose contents will be capitalized:

master file1 file2 - - - filen

The result will consist of files:
fisier1.CAPIT, fisier2.CAPIT - - - fisiern.CAPIT

Master process creates n son processes, and every son will launch
program through execl:

capitalizare filei filei.CAPIT

The source capitalizare.c is:
#include<stdio.h>
#include<string.h>
#define MAXLINIE 100
main(int argc, char* argv[]) {
    FILE *fi, *fo;
    char linie[MAXLINIE], *p;
    fi = fopen(argv[1], "r");
    fo = fopen(argv[2], "w");
    if (fi == NULL && fo == NULL) exit(1);
    for ( ; ; ) {
        p = fgets(linie, MAXLINIE, fi);
        linie[MAXLINIE-1] = '\0';
        if (p == NULL) break;
        for (p = linie; ; ) {
            p = strstr(p, " ");
            if (p == NULL) break;
            p++;
            if (*p == '\n') break;
            *p = toupper(*p);
        }
        fprintf(fo, "%s", linie);
    }
    fclose(fo);
    fclose(fi);
}

The program receives the name from the command line
for the two files. These files are opened and read
the first file line by line.
With the pointer p, the current line are scanning
and looking for a space, but that is not
last character in line. The next character is
then converted to uppercase (toupper do this
transformation only if the character is actually a
lower case).

The source master.c is:
#include<stdio.h>
#include<unistd.h>
#include<string.h>
main(int argc, char* argv[]) {
    int i;
    char nume[200];
    for (i=1; argv[i]; i++) {
        if (fork() == 0) { // un fiu pentru un fisier de capitalizat
            strcpy(nume, argv[i]);
            strcat(nume, ".CAPIT"); // fabricat numele iesirii
            // incarcat programul de capitalizare
            execl("capitalizare","capitalizare",argv[i], nume, 0);
        }
    }
    printf("Lansat simultan %d procese de capitalizare\n",i-1);
}

They go through the command line arguments for each
of son process.
In the array nume the output file name is built.
Then load the program capitalizared with two names
data file "command line".

The programs are compiled:

gcc -o capitalizare capitalizare.c
gcc -o master master.c

To start use the following command:

./master fis1 fis2 - - - fisn


The equivalent Python sources:
capitalizare.py
---------------

import sys
fi = open(sys.argv[1], "r")
fo = open(sys.argv[2], "w")
if fi == None or fo == None: exit(1)
while True:
    linie = fi.readline()
    if not linie: break
    p = 0
    while True:
        p = linie.find(" ", p)
        if p < 0: break
        p += 1
        if p == len(linie): break
        linie = linie[:p]+linie[p].upper()+linie[p+1:]
    fo.write(linie+"\n")
fo.close()
fi.close()


master.py
---------

import sys
import os
for i in range(1, len(sys.argv)):
    if os.fork() == 0: # un fiu pentru un fisier de capitalizat
        nume = sys.argv[i]+".CAPIT" # fabricat numele iesirii
        # incarcat programul de capitalizare
        os.execl("./capitalizare","./capitalizare",sys.argv[i], nume)
print "Lansat simultan "+`i`+" procese de capitalizare"

The statement of each homework will be like:

"It requires a server and one or more clients"

For pipe and FIFO communication we will use only 
one client, and the statement will be changed to:

"It requires a server and one client"

We are making this change because:

1. A pipe or a FIFO channel is used to establish 
communication between two processes. It is difficult 
to maintain multiple clients.

2. In order to use pipe of FIFO between multiple
clients we should use at least one of the following:
   - the server should manage a separate channel for 
each client.
   - using some locking logic and a single channel.
   - using popen calls.

3. We will use the same statement for another assignments, 
where we will need to support multiple clients. Because 
of this we advise our students to code their solutions 
in a modular manner in order to reuse the business logic. 
Only the communication logic will need changes for the following
assignment. 

We will present using an example for the implementation details 
of using pipe, FIFO and popen.

The implementation is in the C language.
At the end of the presentation, the equivalent Python sources
will be presented.

The problem is:

Example: a limited list of file names that have a certain pattern.
------------------------------------------------------------

The problem statement is:
It requires a server and a client. The client will send an integer l 
and a string s. The server will respond with a message that has 
maximum l entries. Each entry will represent a file, from the 
current directory, that has the name ending with string s.

We will present the implementation using multiple functions. 

At first we will build two functions that will have the same role, 
to present the list of the file names:

Mesaj *dir(int l, char *s)
       ---
Mesaj *dirPopen(int l, char *s)
       --------
       
The 'Mesaj' data type will be different based on the used channel
and we will use the following format:

      /--PLUS--\ /------------- MAXS --------------\
|lung|          |s                                  |
|----|----------|xxxxxxxxxxxxxxxxxxxxxxxxxxxx|------|
     |                                       | 
      \ -------------- lung ----------------/

lung - is an integer that represents the length of the message body. 
PLUS zone - contains the a couple of integers, dependent of the used channel.
s - is an array of MAXS characters.
Each student will customize the message structure bases on the problem requirements. 
 
Source dir.c contains dir and dirPopen functions. These functions are independent  
       -----          ---     --------
regarding the communication channel that is used:

Mesaj *dir(int l, char *s) {
    static Mesaj resp;  
    DIR *dirp;
    struct dirent *d;
    char *p, *q, *n;
    int i;
    p = resp.s;
    resp.lung = 0;
    dirp = opendir (".");
    for (i=0; i<l; ) {
        d = readdir (dirp);
        if (d == NULL) break;
        n = d->d_name;
        if (strlen(n) < strlen(s)) continue;
        if (strcmp(n+strlen(n)-strlen(s), s) != 0) continue;
        if (resp.lung+strlen(n)+1 > MAXS) break;
        i++;
        strcpy(p, n);
        resp.lung += strlen(n)+1;
        p += strlen(n)+1;
    }
    closedir (dirp);
    resp.lung += PLUS;
    return &resp;
}

Mesaj *dirPopen(int l, char *s) {
    static Mesaj resp;  
    FILE *fin;
    char n[MAXL], *p, *q, comanda[MAXL];
    int i;
    strcpy(comanda, "ls -1 *");
    strcat(comanda, s);
    fin = popen(comanda, "r");
    p = resp.s;
    resp.lung = 0;
    for (i=0; i<l; ) {
        q = fgets(n, MAXL, fin);
        if (q == NULL) break;
        n[strlen(n)-1] = '\0';
        if (resp.lung+strlen(n)+1 > MAXS) break;
        i++;
        strcpy(p, n);
        resp.lung += strlen(n)+1;
        p += strlen(n)+1;
    }
    pclose (fin);
    resp.lung += PLUS;
    return &resp;
}

The above functions are receiving an integer named l and a string names s, 
and will return a pointer to a Mesaj structure, that will 
contain the list of file names. 
In the message body, in s section, we will have a sequence 
of null terminated strings (C convention) with the requested file
names. The 'lung' value will represent the sum of the total length 
of the sequence from s and PLUS zone. 

dir function will build the response using system calls like: 
opendir, readdir, closedir and structures like DIR*, struct dirent.

dirPopen function will use the popen system call, using a call like
popen("ls -1 *.c") if the string s is ".c". In dirPopen we will use the
standard output of popen call to extract the first l entries. (We can 
notice that dirPopen output will be different then dir output!)

For pipe and FIFO the message body will be defined in mesaj.h file:
                                                      -------
#define MAXS 10000
#define MAXL 1000

typedef struct {
    int lung;
    int i;
    char s[MAXS];
} Mesaj;
#define PLUS (sizeof(int))

The read / write of a Mesaj message using pipe or FIFO is done
in two steps:
  1. read / write the 'lung' field.
  2. we read / write multiple times, for each entry in the message body.
       
Why do we need such a convention?
We will use read and write system calls. These system calls are atomic 
and they are not assuring that all the requested number of bytes are transferred 
at once (the third argument). Each call will return the number of bytes 
that was able to transfer. 
Also because the above calls are not returning anything that could relate to the 
client, like we said previously it is difficult to maintain communication 
with multiple clients this way.

For exchanging messages we will use the following 
functions: Read, Write, ReadMes, WriteMes.
           ----  -----  -------  --------
The first two will use read and write system call multiple times
until all n bytes are exchanges, n is the input parameter. 
ReadMes and WriteMes are exchanging at first the length of the body 
and then body content.

All of the above functions are defined in ReadWrite.c source.
                                          -----------
void Read(int f, char *t, int n) {
  char *p;
  int i, c;
  for (p=t, c=n; ; ) {
    i = read(f, p, c);
    if (i == c) return;
    c -= i;
    p += i;
  }
}

void Write(int f, char *t, int n) {
  char *p;
  int i, c;
  for (p=t, c=n; c; ) {
    i = write(f, p, c);
    if (i == c) return;
    c -= i;
    p += i;
  }
}

Mesaj *ReadMes(int canal) {
    static Mesaj mesaj;
    read(canal, (char*)&mesaj.lung, sizeof(int));
    Read(canal, (char*)&mesaj+sizeof(int), mesaj.lung);
    return &mesaj;
}

void WriteMes(int canal, Mesaj *pm) {
    write(canal, (char*)pm, sizeof(int));
    Write(canal, (char*)pm+sizeof(int), pm->lung);
}

The server main activity will be defined by the following function:

void parinte(int in, in out)
     -------

The input parameters are channel handlers that will be used to 
read/write the message from/to the client. 
For each case, the handlers will be for pipe, FIFO or how we will 
see on the next assignments, handlers for shared memory or queue messages.
The logic of this function is like: read one message from the client, 
call dir (or dirPopen) function, write the response to the client.

Source code for parinte.c:
                ---------
void parinte(int in, int out) {
    Mesaj *pm;
    for ( ; ; ) {
        pm = ReadMes(in);
        //pm = dirPopen(pm->i, pm->s);
        pm = dir(pm->i, pm->s);
        WriteMes(out, pm);
    }
}


The main client logic is defined by the following function:

void fiu(int in, in out)
     ---

Like on the server site, the arguments are representing channel handlers,
in this case used to exchange messages with the server part.
The client will read from standard input an integer l and 
a string s, after this will build a message that will be send to the server.
After sending the message will wait for a response, and once received it will
print the content on the standard output.

The fiu.c source presents the client logic:
    -----
void fiu(int in, int out) {
    Mesaj *pm, mesaj;
    char *pc,linie[MAXL];
    int i;
    for ( ; ; ) {
        printf("Dati: numar|sufix: ");
        pc = (char*)fgets(linie, MAXL, stdin);
        if (pc == NULL) break;
        linie[strlen(linie)-1] = '\0';
        pc = strstr(linie, "|");
        if (pc == NULL) continue;
        mesaj.i = atoi(linie);
        strcpy(mesaj.s, pc+1);
        mesaj.lung = PLUS + strlen(mesaj.s) + 1;
        
        WriteMes(out, &mesaj); 
        
        pm = ReadMes(in);

        pc = pm->s;
        printf("%d\n",pm->lung);
        for (i = PLUS; i < pm->lung; ) {
            printf("%d %s\n", i, pc);
            i += strlen(pc) + 1;
            pc += strlen(pc) + 1;
        }
    }
}

Finally we will present the main sources.

The first implementation contains pipe communication logic and
is presented in pipe.c source file:
                ------
#include <dirent.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include "mesaj.h"
#include "ReadWrite.c"
#include "dir.c"
#include "parinte.c"
#include "fiu.c"
main() {
    int fp[2], pf[2], pid;
    // Doua pipe, fp: fiu->parinte, pf: parinte->fiu
    if (pipe(fp) < 0 || pipe(pf) < 0) exit(1);
    pid = fork();
    if (pid < 0) exit(2);
    if (pid > 0) { // Codul serverului (parintelui)
        fclose(stdin);
        fclose(stdout);
        close(fp[1]);
        close(pf[0]);
        parinte(fp[0], pf[1]);
    } else { // Codul clientului (fiului)
        close(fp[0]);
        close(pf[1]);
        fiu(pf[0], fp[1]);
        close(pf[0]);
        close(fp[1]);
    }
}

We should notice that we are closing the unused channels on each side. 
Be this we are establishing the communication direction and free the unused
resources. 

In the second implementation we are presenting FIFO.

We need at first to create two FIFO channels in our current directory:

$ mkfifo fifo1
$ mkfifo fifo2

If the fifo files exist we should remove them using:

$ rm fifo1
$ rm fifo2

Because the client and the server are running by the same user 
the fifo files should reside in the current directory. 
If the processes are created by separate users we will define
the fifo files in a common location, like: /tmp/fifo1, /tmp/fifo2.

The server source code is defined in fifos.c:
                                     --------
#include <dirent.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include "mesaj.h"
#include "ReadWrite.c"
#include "dir.c"
#include "parinte.c"
main() {
    int f1, f2;
    fclose(stdin);
    fclose(stdout);
    f1 = open("fifo1", O_WRONLY);
    f2 = open("fifo2", O_RDONLY);
    parinte(f2, f1);
}

The client source code is defined in fifoc.c:
                                     -------
#include <string.h>
#include <stdio.h>
#include <fcntl.h>
#include "mesaj.h"
#include "ReadWrite.c"
#include "fiu.c"
main() {
    int f1, f2;
    f1 = open("fifo1", O_RDONLY);
    f2 = open("fifo2", O_WRONLY);
    fiu(f1, f2);
    close(f1);
    close(f2);
}

Bellow are presented some executions with and without popen, pipe and FIFO.
     ---------------

florin@florin-laptop:~/probleme/UPipe-H$ #dir
florin@florin-laptop:~/probleme/UPipe-H$ gcc pipe.c
florin@florin-laptop:~/probleme/UPipe-H$ ./a.out
Dati: numar|sufix: 5|.c
47
4 fiu.c
10 ReadWrite.c
22 parinte.c
32 fifos.c
40 pipe.c
Dati: numar|sufix: ^C
florin@florin-laptop:~/probleme/UPipe-H$ #dirPopen
florin@florin-laptop:~/probleme/UPipe-H$ gcc pipe.c
florin@florin-laptop:~/probleme/UPipe-H$ ./a.out
Dati: numar|sufix: 5|.c
43
4 fifoc.c
12 fifos.c
20 fiu.c
26 parinte.c
36 pipe.c
Dati: numar|sufix: ^C
florin@florin-laptop:~/probleme/UPipe-H$ mkfifo fifo1
florin@florin-laptop:~/probleme/UPipe-H$ mkfifo fifo2
florin@florin-laptop:~/probleme/UPipe-H$ #dirPopen
florin@florin-laptop:~/probleme/UPipe-H$ gcc -o s fifos.c
florin@florin-laptop:~/probleme/UPipe-H$ gcc -o c fifoc.c
florin@florin-laptop:~/probleme/UPipe-H$ ./s&
[1] 2066
florin@florin-laptop:~/probleme/UPipe-H$ ./c
Dati: numar|sufix: 5|.c
43
4 fifoc.c
12 fifos.c
20 fiu.c
26 parinte.c
36 pipe.c
Dati: numar|sufix: ^C     
florin@florin-laptop:~/probleme/UPipe-H$ #dir
florin@florin-laptop:~/probleme/UPipe-H$ kill 2066
florin@florin-laptop:~/probleme/UPipe-H$ rm fifo1
[1]+  Terminated              ./s
florin@florin-laptop:~/probleme/UPipe-H$ rm fifo2
florin@florin-laptop:~/probleme/UPipe-H$ mkfifo fifo1
florin@florin-laptop:~/probleme/UPipe-H$ mkfifo fifo2
florin@florin-laptop:~/probleme/UPipe-H$ gcc -o s fifos.c
florin@florin-laptop:~/probleme/UPipe-H$ gcc -o c fifoc.c
florin@florin-laptop:~/probleme/UPipe-H$ ./s&
[1] 2142
florin@florin-laptop:~/probleme/UPipe-H$ ./c
Dati: numar|sufix: 5|.c
47
4 fiu.c
10 ReadWrite.c
22 parinte.c
32 fifos.c
40 pipe.c
Dati: numar|sufix: 


The equivalent Python sources:
Mesaj.py
--------

class Mesaj:
    MAXS = 10000
    SIZEOFINT = 10 # privit ca text
    PLUS = SIZEOFINT
    lung = 0
    i = 0
    s = [""]
    def __init__(self, ser):
        if ser == None: return
        self.lung = int(ser[:self.SIZEOFINT])
        self.i = int(ser[self.SIZEOFINT:2*self.SIZEOFINT])
        self.s = ser[2*self.SIZEOFINT:self.SIZEOFINT+self.lung].split("|")

    def __str__(self):
        ser = ""
        for l in self.s:
            ser += l+"|"
        ser = ser[:-1]
        ser = self.i2s(self.lung)+self.i2s(self.i)+ser
        return ser
        
    def i2s(self, i):
        sir = "000000000000000000"+`i`
        if sir.endswith("L"): sir = sir[:-1]
        return sir[-self.SIZEOFINT:]


dir.py
------

import os
import Mesaj
def dir(l, s):
    mesaj = Mesaj.Mesaj(None)
    mesaj.s = []
    lung = 0
    i = 1
    for linie in os.listdir("."):
        if i > l: break
        if lung + len(linie) + len(mesaj.s) > mesaj.MAXS: break
        if len(linie) < len(s): continue
        if linie[len(linie)- len(s):] != s: continue
        mesaj.s += [linie]
        i += 1
        lung += len(linie)
    mesaj.lung = mesaj.PLUS + lung + len(mesaj.s) - 1
    if len(mesaj.s) == 0: mesaj.lung += 1
    return mesaj

def dirPopen(l, s):
    mesaj = Mesaj.Mesaj(None)
    mesaj.s = []
    lung = 0
    i = 1
    for linie in os.popen("ls -1", "r").readlines():
        linie = linie[:-1]
        if i > l: break
        if lung + len(linie) + len(mesaj.s) > mesaj.MAXS: break
        if len(linie) < len(s): continue
        if linie[len(linie)- len(s):] != s: continue
        mesaj.s += [linie]
        i += 1
        lung += len(linie)
    mesaj.lung = mesaj.PLUS + lung + len(mesaj.s) - 1
    if len(mesaj.s) == 0: mesaj.lung += 1
    return mesaj

ReadWrite.py
------------

import Mesaj
import os
def Read(f, n):
    c = n
    sir = ""
    while c > 0:
        s = os.read(f, c);
        sir += s
        c -= len(s)
    return sir

def Write(f, sir):
    c = len(sir)
    p = 0
    while c > 0:
        i = os.write(f, sir[p:])
        c -= i
        p += i

def ReadMes(canal):
    lung = os.read(canal, Mesaj.Mesaj.SIZEOFINT)
    ser = Read(canal, int(lung))
    return Mesaj.Mesaj(lung+ser)

def WriteMes(canal, mesaj):
    lung = mesaj.SIZEOFINT+mesaj.lung
    Write(canal, str(mesaj)[:lung])

parinte.py
----------

import ReadWrite
import dir
def parinte(iN, out):
    while True:
        mesaj = ReadWrite.ReadMes(iN)
        mesaj = dir.dir(mesaj.i, mesaj.s[0])
        #mesaj = dir.dirPopen(mesaj.i, mesaj.s[0])
        ReadWrite.WriteMes(out, mesaj)


fiu.py
------

import sys
import Mesaj
import ReadWrite
def fiu(iN, out):
    while True:
        print "Dati: numar|sufix: ",
        linie = sys.stdin.readline()
        if not linie:  break
        linie = linie[:-1]
        pc = linie.find("|")
        if pc < 0: continue
        mesaj = Mesaj.Mesaj(None)
        mesaj.s = []
        mesaj.i = int(linie[:pc])
        mesaj.s += [linie[pc+1:]]
        mesaj.lung = mesaj.PLUS + len(mesaj.s[0])

        ReadWrite.WriteMes(out, mesaj) 

        mesaj = ReadWrite.ReadMes(iN)

        for l in mesaj.s:
            print l

pipe.py
-------

import sys
import os
import parinte
import fiu
def main():
    fp = os.pipe()
    pf = os.pipe()
    pid = os.fork()
    if pid < 0: exit(2)
    if pid > 0: # Codul serverului (parintelui)
        sys.stdin.close()
        sys.stdout.close()
        os.close(fp[1])
        os.close(pf[0])
        parinte.parinte(fp[0], pf[1])
    else: # Codul clientului (fiului)
        os.close(fp[0])
        os.close(pf[1])
        fiu.fiu(pf[0], fp[1])
        os.close(pf[0])
        os.close(fp[1])
main()

fifos.py
--------

import sys
import os
import parinte
def main():
    sys.stdin.close()
    sys.stdout.close(
    f1 = os.open("fifo1", os.O_WRONLY, 0666)
    f2 = os.open("fifo2", os.O_RDONLY, 0666)
    parinte.parinte(f2, f1)
main()

fifoc.py
--------

import os
import fiu
def main():
    f1 = os.open("fifo1", os.O_RDONLY, 0666)
    f2 = os.open("fifo2", os.O_WRONLY, 0666)
    fiu.fiu(f1, f2)
    os.close(f1)
    os.close(f2)
main()

Prezentarea problemei

For each command line argument the main process will launch a subprocess (type A).
The A process will try to execute, in a new subprocess (type B), using one of the exec functions, the received argument.
In case of failure, the process of type A, will send to his parent, using a pipe channel, the error code (errno value) generated by the exec call.
On success, the process A, will wait for process of type B and once finished it will transmit to his parent the zero code, using the same pipe channel.
The main process will print for each argument if the execution was successful or not. Only for failed executions the error received code will be printed too.


