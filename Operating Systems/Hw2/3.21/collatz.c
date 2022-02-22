#include <sys/types.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>

int main(int argc, char *argv[])
{
    int collatz;
    if(argc == 2) 
    {
      collatz = atoi(argv[1]);
    }
    else if(argc > 2)
    {
        printf("Too many arguments.\n");
        return 0;
    }
    else
    {
        printf("Please add an integer n.\n");
        return 0;
    }

    if(collatz < 1)
    {
        printf("Not a positive integer.\n");
        return 0;
    }
    
    pid_t pid;
    pid = fork();

    if(pid < 0)
    {
        fprintf(stderr, "Fork Failed");
        return 1;
    }
    //Child
    else if(pid == 0)
    {
        const char *delimiter = ", ";   
        while(collatz != 1)
        {
            if(collatz % 2 == 1)
            {
                printf("%d", collatz);
                printf("%s", delimiter);
                collatz = 3 * collatz + 1; 
            }
            else
            {
                printf("%d", collatz);
                printf("%s", delimiter);
                collatz /= 2;
            }
        }
        printf("%d", collatz);
        printf("\n");
    }
    //Parent
    else
    {
        wait(NULL);
        return 0;
    }
  
}