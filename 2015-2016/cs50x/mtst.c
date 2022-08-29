#include <stdio.h>
#include <cs50.h>

unsigned int f(int n);

int main(void)
{
    string s = GetString();
    int k = atoi(s);
   
    printf("%i\n", f(k));
}

unsigned int f(int n)
{
    int j = n;
    for (int i = 1; i < n; i++)
    {
        j = (j * (n - i)); 
    }
    return j;
}
