#include<stdio.h>
#include<pthread.h>

#define N 10

/* Threads in Linux */
void * func( void * arg ) 
{
	printf("stuff %d" , (int)pthread_self() );
	return NULL;
}

// 
int main()
{	
	int i ;
	pthread_t thr[N];
	for ( i = 0 ; i < N ; i++ ) 
    {
		pthread_create (&thr[i] , NULL , func , NULL) ;
	}
	
	for ( i = 0 ; i < N ; i++ ) 
    {
        pthread_join(thr[i] , NULL ) ;
    }
	return 0;
}