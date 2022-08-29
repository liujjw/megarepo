#include <stdio.h>
#include <cs50.h>

// function prototype
int cube(int a);

int main (void)
{
    int x=0;
    printf("gimme a number:");
    x = GetInt();
    printf("Cubing...\n");
    x = cube(x);
    printf("Cubed!\n");
    printf("X is now %i\n!", x);
}

/**
* Cubes arguement
*/
int cube(int n)
{
    return n*n*n; 
}
