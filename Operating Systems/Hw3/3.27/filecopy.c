#include <sys/types.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>

#define writeEnd 1
#define readEnd 0
#define SIZE 5000

int main(int argc, char *argv[])
{
    if(argc != 3)
    {
        printf("Incorrect number of arguments.");
        return 0;
    }

    int fd[2];
    char input[SIZE];
    char output[SIZE];
    int inputFile = open(argv[1], 0);
    
   
    if (pipe(fd) == -1) {
		fprintf(stderr,"Pipe failed");
		return 1;
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
        read(fd[readEnd], output, sizeof(output) - 4);
        FILE *outputFile = fopen(argv[2], "w");
        int results = fputs(output, outputFile);
        close(fd[readEnd]);
    }
    //Parent
    else
    {
       while(read(inputFile, input, sizeof(input)) > 0)
       {
           write(fd[writeEnd], input, sizeof(input));
           close(fd[writeEnd]);
       }
    }
    return 0;
}