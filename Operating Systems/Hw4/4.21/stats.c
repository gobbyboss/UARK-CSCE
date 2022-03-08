#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>

int NUM_THREADS = 3;
double AVERAGE;
int MAXIMUM, MINIMUM;

void *averageRunner(void *param)
{
    int *nums = (int *)param;
    double sum = 0;
    for(int i = 0; i < 4; i++)
    {
        sum += nums[i];
    }
    AVERAGE = sum / 4;
    pthread_exit(NULL);
}

void *minimumRunner(void *param)
{
    int *nums = (int *)param;
    MINIMUM = nums[0];
    for(int i = 0; i < 4; i++)
    {
        if(nums[i] < MINIMUM)
        {
            MINIMUM = nums[i];
        }
    }
 
    pthread_exit(NULL);
}

void *maximumRunner(void *param)
{
    int *nums = (int *)param;
    MAXIMUM = nums[0];
    for(int i = 0; i < 4; i++)
    {
        if(nums[i] > MAXIMUM)
        {
            MAXIMUM = nums[i];
        }
    }
 
    pthread_exit(NULL);
}

int main(int argc, char *argv[])
{
    pthread_t tid[NUM_THREADS];
 
    if(argc != 5)
    {
        printf("Incorrect number of arguments.\n");
    }

    int *input = (int*)malloc((argc - 1)*sizeof(int));
    for(int i = 1; i < argc; i++)
    {
        input[i - 1] = atoi(argv[i]);
    }
   
    pthread_create(&tid[0], NULL, averageRunner, (void *)input);
    pthread_create(&tid[1], NULL, minimumRunner, (void *)input);
    pthread_create(&tid[2], NULL, maximumRunner, (void *)input);
    
    for(int i = 0; i < NUM_THREADS; i++)
    {
        pthread_join(tid[i], NULL);
    }

    printf("Average: %f\n", AVERAGE);
    printf("Maximum: %d\n", MAXIMUM);
    printf("Minimum: %d\n", MINIMUM);

    return 0;
}