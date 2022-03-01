#include <ctype.h>
#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>

#define writeEnd 1
#define readEnd 0
#define SIZE 50

int main()
{
    int ifd[2];
    int ofd[2];
    char input[SIZE];
    char output[SIZE];

    printf("Please type in a message:\n");
    fgets(input, SIZE, stdin);


    if (pipe(ifd) == -1) {
		fprintf(stderr,"Pipe failed");
		return 1;
	}

    if (pipe(ofd) == -1) {
		fprintf(stderr,"Pipe failed");
		return 1;
	}


    pid_t pid;
    pid = fork();

    //Fork Failure
    if(pid < 0)
    {
        fprintf(stderr, "Fork Failed");
        return 1;
    }
    //Child
    else if(pid == 0)
    {
        read(ifd[readEnd], output, SIZE);
        close(ifd[readEnd]);
        for(int i = 0; i < SIZE; i++)
        {
            if(output[i] < 91)
            {
               output[i] = tolower(output[i]);
            }
            else
            {
                output[i] = toupper(output[i]);
            }  
        }
        write(ofd[writeEnd], output, strlen(output) + 1);
        close(ofd[writeEnd]);
    }
    //Parent
    else
    {
        // Writing message to pipe
        write(ifd[writeEnd], input, strlen(input)+1);
        close(ifd[writeEnd]);

        wait(NULL);

        // Reading message from pipe
        read(ofd[readEnd], output, SIZE);
        close(ofd[readEnd]);
        printf("%s", output);
        return 0;
    }
}