#include <stdio.h>
#include <cs50.h>

int main(int argc, string argv[])
{
    if (argc == 6)
    {
        printf("Hello, %s\n", argv[5]);
        return 0;
    }
    else
    {
        return 1;
    }
}
