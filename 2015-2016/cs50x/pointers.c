#include <stdio.h>

int main(void)
{
    FILE* file = fopen("hello", "w");
    if (file != NULL)
    {
        fprintf(file, "hello, world!");
        fclose(file);
    }
    
}

#include <stdio.h>

int main (void)
{   
    FILE* file  = fopen("hello", "w");
    if (file != NULL)
    {
        fprintf(file, "hello, world!");
        fclose(file);   
    }
    
}
