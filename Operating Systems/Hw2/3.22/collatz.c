#include <sys/types.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <fcntl.h>
#include <sys/mman.h>

int main(int argc, char *argv[])
{
    int collatz;
    const char* name = "/collatz-memory";
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
    size_t SIZE = 4096;
    int fd = shm_open(name, O_CREAT | O_EXCL | O_RDWR, S_IRWXU | S_IRWXG | S_IRWXO);//0777);
    if (fd < 0) {
        perror("shm_open()");
        return EXIT_FAILURE;
    }

    ftruncate(fd, SIZE);

    char *data =
      (char *)mmap(NULL, SIZE, PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0);
    
    pid_t pid;
    pid = fork();
    int iterator = 0;

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
            int output = snprintf(data, SIZE, "%d\n", collatz);
            SIZE -= output;
            data += output;
            if(collatz % 2 == 1)
            {  
                collatz = 3 * collatz + 1; 
                iterator++;
            }
            else
            {
                collatz /= 2;
                iterator++;
            }
        }
        int output = snprintf(data, SIZE, "%d\n", collatz);
        munmap(data, SIZE);
        shm_unlink(name);
    }
    //Parent
    else
    {
        wait(NULL);
        printf("%s", (char *)data);
        shm_unlink(name);
        close(fd);
        return 0;
    }
}