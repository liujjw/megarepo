#include <stdio.h>

#define SIZE 8

void sort(int array[], int size)
{
    // TODO: sort array using selection sort
    for (int i = 0; i < size - 2; i++)
    {
     	// int min = i;
        for(int k = i + 1; k < size - 1; k++)
        {
         	if (array[k] < array[i])
                // min = k;
            //if (min != i)
            {    
                int temp = array[i];
                array[i] = array[k];
                array[k] = temp;
        
            }
        }
    }
}

int main(void)
{
    int numbers[SIZE] = { 4, 15, 16, 50, 8, 23, 42, 108 };
    for (int i = 0; i < SIZE; i++)
    {
        printf("%i ", numbers[i]);
    }
    printf("\n");
    sort(numbers, SIZE);
    for (int i = 0; i < SIZE; i++)
    {
        printf("%i ", numbers[i]);
    }
    printf("\n");
}

