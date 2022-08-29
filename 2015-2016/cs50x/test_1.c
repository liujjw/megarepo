#include <stdio.h>
#include <cs50.h>

int not_strlen(char* s);
int main(void)
{
    char* s = GetString();
    printf("%i\n", not_strlen(s));
    
}
int not_strlen(char* s)
{
    int counter = 0;
    for(int length = 0; s[length] != '\0' ; length++)
        counter++;
    return counter;
}
