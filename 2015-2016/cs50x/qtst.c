#include <stdio.h>
#include <cs50.h>

_Bool odd(unsigned int n);

int main(int argc, char* argv[])
{
    char* number = GetString();
    unsigned int n = (int) number;
    if (_Bool odd(unsigned int n) == true)
    {
        printf("true\n");
    }
    
}

_Bool odd(unsigned int n)
{
    return true;
}
