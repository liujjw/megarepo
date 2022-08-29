#include <stdio.h>
#include <cs50.h>
#include <string.h>
#include <ctype.h>

int main(int argc, string argv[])
{
    string s = GetString();
    
    for (int i = 0, n = strlen(s); i < n; i++)
    {
        
        //if (s[i] >= 'a' && s[i] <='z')
        //if (islower(s[i]))
        //{
        //    printf("%c", s[i] - ('a'-'A'));
        //    printf("%c", toupper(s[i]));
        //}
        //else
        //{
        //    printf("%c", s[i]);
        //}
        
        printf("%c", toupper(s[i]));
        
    }
    printf("\n");
}

