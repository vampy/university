CC      = gcc
ifeq ($(CC),gcc)
	CFLAGS  = -O0 -g -Wall -std=c11
else
	CFLAGS  = -O0 -g -Wall -std=c++11
endif
LDFLAGS = 

all:
	$(CC) $(LDFLAGS) $(CFLAGS) -o server server.c
	$(CC) $(LDFLAGS) $(CFLAGS) -o client client.c

clean:
	rm client server