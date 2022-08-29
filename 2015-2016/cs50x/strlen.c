#include <stdio.h>
#include <cs50.h>

int main(int argc, char* argv[])
{
    string s = GetString();
    int length = 0;
    while (s[length] != '\0')
    length++;
    
    return 0;
}
