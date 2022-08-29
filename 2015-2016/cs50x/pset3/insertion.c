#include <stdio.h>
#include <cs50.h>

#define SIZE 8

void insertion(int argv[], int size)
{   
    // TODO
    for (int i = 1; i < size; i++)
    {
        int j = i;
        while (j > 0 && argv[j] < argv[j - 1])
        {
            int temp = argv[j];
            argv[j] = argv[j - 1];
            argv[j - 1] = temp;
            j--;
        }
    
    }
}   

int main(void)
{
    int array[SIZE] = {4, 15, 16, 50, 8, 23, 42, 108};
    for (int i = 0; i < SIZE; i++)
    {
        printf("%i ", array[i]);
    }
    insertion(array, SIZE);
    for (int k = 0; k < SIZE; k++)
    {
        printf("%i ", array[k]);
    }
    return 0;
}
