#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#define HIT_MARKER 1
#define MISS_MARKER 0

int NUM_THREADS = 4;
long HIT, MISS = 0;

pthread_mutex_t hitMutex, missMutex;


int checkHit(float x, float y)
{
    float checkRadius = sqrt(pow(x, 2) + pow(y, 2));
    if(checkRadius <= 1)
    {
        //Hit
        return HIT_MARKER;
    }
    else
    {
        //MISS
        return MISS_MARKER;
    }
    
}

void *runner(void *param)
{
    time_t t;
    srand((unsigned) time(&t));
    float x, y;
    long runCount = (long)param;
    while(runCount >= 0)
    {
       x = rand() % 1000;
       y = rand() % 1000;
       x /= 1000;
       y /= 1000;
       if(checkHit(x,y) == HIT_MARKER)
       {
            pthread_mutex_lock(&hitMutex);
            HIT++;
            pthread_mutex_unlock(&hitMutex);
       }
       else
       {
           pthread_mutex_lock(&missMutex);
           MISS++;
           pthread_mutex_unlock(&missMutex);
       }
       runCount--;
    }
    pthread_exit(NULL);
}

int main()
{
    pthread_t tid[NUM_THREADS];
    long input = 2500000;
    for(int i = 0; i < NUM_THREADS; i++)
    {
        pthread_create(&tid[i], NULL, runner, (void *)input);
    }
    for(int i = 0; i < NUM_THREADS; i++)
    {
        pthread_join(tid[i], NULL);
    }

    double hits = HIT;
    double total = HIT + MISS;
    float output = 4 * hits / total;
    printf("%s", "Output of ( 4 * Number of Hits / Number of Points: ");
    printf("%f\n", output);
    exit(1);
}