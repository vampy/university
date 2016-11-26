#include <stdio.h>
#include <pthread.h>

#define T 10
#define N 10000

int counter = 0;
int thousands = 0;

pthread_mutex_t mtx;
pthread_mutex_t mtx1000;

/* We need to have in the mutex the IF as weel, so that we take the
 * decision base don counter in a place where counter wil not be changed
 * by other threads. The same mutex will also protect thousands from
 * corruption */
void* increment_A(void* p) {
    int i;
    for(i=0; i<N; i++) {
        pthread_mutex_lock(&mtx);
        counter++;
        if(counter % 1000 == 0) {
            thousands++;
        }
        pthread_mutex_unlock(&mtx);
    } 
    return NULL;
}

/* Apperently equivalent to increment_A but not really. In this verison,
 * it is possible that after the first lock/unlock region (known is
 * critical region or critical section), another thread changes counter
 * and the decision in the second critical seciton will be taken based
 * on a differnt counter value than the one calculated by the current
 * thread. This may not be desirable ...
 */
void* increment_B(void* p) {
    int i;
    for(i=0; i<N; i++) {
        pthread_mutex_lock(&mtx);
        counter++;
        pthread_mutex_unlock(&mtx);

        pthread_mutex_lock(&mtx);
        if(counter % 1000 == 0) {
            thousands++;
        }
        pthread_mutex_unlock(&mtx);
    } 
    return NULL;
}

/* A correct, but very slow and inefficient solution. It will prevent
 * the corruption of data, but it will kill concurrency
 */
void* increment_C(void* p) {
    int i;
    pthread_mutex_lock(&mtx);
    for(i=0; i<N; i++) {
        counter++;
        if(counter % 1000 == 0) {
            thousands++;
        }
    } 
    pthread_mutex_unlock(&mtx);
    return NULL;
}

/* This is arguably a faster solution than increment_A, especiall if the
 * "local % 1000 == 0" is replaced by something very very slow. In
 * increment_A, the lock is held while that ocndition is verified, jsut
 * so that central part (ie counter) is not changed. We can however
 * store counter in a local variable (named here simply "local") and
 * then continue to evaluating the condition without holding the
 * lock. We still need to protect the thousands variable, and we could
 * do it with the same mutx (ie mtx). But this creates situations where
 * a thread, before incrementing counter, will wait for another to
 * increment thousands. To avoid this unneeded wait, we are usign
 * another mutex.
 */
void* increment(void* p) {
    int i;
    int local;
    for(i=0; i<N; i++) {
        pthread_mutex_lock(&mtx);
        counter++;
        local = counter;
        pthread_mutex_unlock(&mtx);

        if(local % 1000 == 0) {
            pthread_mutex_lock(&mtx1000);
            thousands++;
            pthread_mutex_unlock(&mtx1000);
        }
    } 
    return NULL;
}

int main() {
    int i;
    pthread_t thr[T];

    pthread_mutex_init(&mtx, NULL);
    pthread_mutex_init(&mtx1000, NULL);

    for(i=0; i<T; i++) {
        pthread_create(&thr[i], NULL, increment, NULL);
    }

    for(i=0; i<T; i++) {
        pthread_join(thr[i], NULL);
    }

    pthread_mutex_destroy(&mtx);
    pthread_mutex_destroy(&mtx1000);

    return 0;
}
