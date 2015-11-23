#include "os.h"

int semid;

void set_value(int sem, int new_value)
{
    struct sembuf op;
    op.sem_num = sem; // the sem number in our set semid
    op.sem_op = new_value;
    op.sem_flg = 0;

    if (semop(semid, &op, 1) < 0)
    {
        perror("semop");
        exit(1);
    }
}

void lock_sem(int sem)
{
    set_value(sem, -1);
}

void unlock_sem(int sem)
{
    set_value(sem, 1);
}

int main()
{
    key_t key = safe_ftok("/home/dan/dev/os", 'a');

    semid = semget(key, 1, IPC_CREAT | 0600);
    if (semid == -1)
    {
        perror("error");
        exit(-1);
    }
    unlock_sem(0);

    int sem_value = semctl(semid, 0, GETVAL);
    if (sem_value == -1)
    {
        perror("sem");
        exit(1);
    }
    printf("The value of the sem is %d\n", sem_value);

    set_value(0, -sem_value - 1);

    //semctl(semid, 0, IPC_RMID);

    return 0;
}
