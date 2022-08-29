#include <cs50.h>
#include <stdio.h>

#define SIZE 8

bool search(int needle, int haystack[], int size)
{
    int lower = 0;
    int upper = size - 1;
    
    // Binary search
    while (lower <= upper)
    {
        // find the middle
        int middle = (upper + lower) / 2;
        
        // compare middle to value_wanted
        if (haystack[middle] == needle)
        {
            return true;
        }
        else if (haystack[middle] < needle)
        {
            lower = middle + 1;
        }
        else if (haystack[middle] > needle)
        {
            upper = middle - 1;
        }
        
    }   
    return false;   
}

int main(void)
{
    int numbers[SIZE] = { 4, 8, 15, 16, 23, 42, 50, 108 };
    printf("needle; ");
    int n = GetInt();
    if (search(n, numbers, SIZE))
    {
        printf("YES\n");
    }
    else 
    {
        printf("NO\n");
    }
}

