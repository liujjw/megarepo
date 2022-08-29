/*
* crimsonblack-pset2_initials
*
*8/3/2015
*
*User inputs name, output is their initials
*
*/


#include <stdio.h>
#include <string.h>
#include <cs50.h>
#include <ctype.h>

int main(int argc, string argv[])
{
    // user inputs a string
    string name = GetString();
    // prints the first character in string and capitalizes        
    printf("%c", toupper(name[0]));
    
    // loops through the length of name    
    for(int i = 0; i < strlen(name); i++)
    {
        // if the ith character in name is a space
        if (name[i] == (' '))
        {
            // print the character in array name after it and capitalize
            printf("%c", toupper(name[i + 1]));  
        }      
    }
    printf("\n");        
}
// END
