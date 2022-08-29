#include <stdio.h>
#include <cs50.h>
#include <string.h>
#include <math.h>

int main(int argc, string argv[])
{
    for(int i = 0; i < argc; i++)
    {    
        for(int j = 0, n = strlen(argv[i]); j < n; j++)
        {    
            printf("argv[%d][%d] is %c\n", i, j, argv[i][j]);
        }
    }
    return 0;
}
