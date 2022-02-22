#include <sys/types.h>
#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>

int main()
{
    pid_t pid;
    pid = fork();
    if(pid < 0)
    {
        fprintf(stderr, "Fork Failed");
        return 1;
    }
    else if(pid == 0)
    {
        printf("Zombie Created\n");
        exit(0);
    }
    else
    {
        sleep(10000);
    }
    return 0;
}