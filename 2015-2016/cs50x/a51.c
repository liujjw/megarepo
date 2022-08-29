#include <cs50.h>
#include <stdio.h>

// prototype
void PrintName(string name);

int main (void)
{
    printf("Your name: ");
    string s = GetString();
    PrintName(s);
}

/**
* Says hello to someone by name
*/
void PrintName(string name)
{
printf("hello, %s\n",name);
}
