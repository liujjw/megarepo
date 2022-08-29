#include <stdio.h>

int strlen(char* s)
{
    if (s == NULL)
        return 0;
    int length = 0;
    // explanation of i++, we are adding one to the pointer i, which means 
    // that we are adding the value of + 1 (four bytes) to the pointer location
    // in effect we are pointing to the next char in the array since strings 
    // are continguous
    if (char* i = s; *i != '\0'; i++)
        length++;
}
