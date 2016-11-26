#include <stdio.h>
#include <pthread.h>
#include <stdlib.h>


pthread_mutex_t mtx;
pthread_t t[n];
pthread_cond_t condition=PTHREAD_COND_INITIALIZER;
pthread_cond_t condition2=PTHREAD_COND_INITIALIZER;


void* f(void* arg)
{	int i;
	for(i=0;i<10;i++)
{
	pthread_mutex_lock(&mtx);
	printf("NR=%d\n", (unsigned int)pthread_self());
	pthread_cond_signal(&condition2);
	pthread_cond_wait(&condition,&mtx);

	pthread_mutex_unlock(&mtx);
}
}

void* f2(void* arg)
{ int i;
  for(i=0;i<10;i++)
{
  pthread_mutex_lock(&mtx);
	pthread_cond_wait(&condition2,&mtx);

	printf("NR=%d\n", (unsigned int)pthread_self());
	pthread_cond_signal(&condition);
  pthread_mutex_unlock(&mtx);
}
}

int main()
{
	pthread_mutex_init(&mtx,NULL);
	int i;
	int it1,it2;
	it1=pthread_create(&t[0],NULL,f,NULL);
	it2=pthread_create(&t[1],NULL,f2,NULL);
		pthread_join(t[0],NULL);

		pthread_join(t[1],NULL);

	
	
	pthread_mutex_destroy(&mtx);

	printf("I am Mr.Main\n");

return 0;
}