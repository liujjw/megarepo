#include <stdio.h>

#define SIZE 8

void sort(int array[], int size)
{
    // TODO: sort array using bubble sort
    for (int i = 0; i < size - 1; i++)
    {         
        int swaps = 0;
        for (int k = 0; k < size - 1 - i; k++)    
	    {
    	    if (array[k] > array[k + 1])
            {    
                int temp = array[k];
                array[k] = array[k + 1];
                array[k + 1] = temp;
                swaps++;
		    }       
        }    
        if (!swaps)
            break;
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

