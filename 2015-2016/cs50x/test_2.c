#include <stdio.h>
#include <cs50.h>
#include <string.h>

int atoi_not(char* s);
int main(void)
{
    string s = GetString();
    printf("%d\n", atoi(s));
}
int atoi_not(char* s)
{
    if (s == NULL)
        return 0;
    int value = 0;
    for (int i = 0; i < strlen(s); i++)
    {
        if (s[i] < '0' || s[i] > '9')
            return 0;
       value *= 10;
       value += s[i] - '0'; 
        
    }
    return value;
}
