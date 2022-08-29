#include <stdio.h>

#define MAX 3

int a[MAX*MAX];

int rand_seed = MAX;
    
/* from K&R
   - returns random number between 0 and 32767.*/
int random()
{
    rand_seed = rand_seed*1103515245 + 12345;   
    return(unsigned int)(rand_seed/65536) % 32768; 
}

int main(void)
{
    int i, k, j, l;
    // fill the array
    for(i = 0; i < MAX*MAX; i++)
    {
        a[i] = random();
        printf("%d\n", a[i]);
        if (a[i] == a[MAX*MAX - 1])
        {
            printf("\n\n------SORTED------\n");
        }
    }
    /* bubble sort the array */
    for(k = 0; k < MAX*MAX - 1; k++)
    {
        for(j = 0; j < (MAX*MAX) - k - 1; j++)
        {
            if (a[j] > a[j + 1])
            {
                l = a[j];
                a[j] = a[j + 1];
                a[j + 1] = l;
                
            }
        }
         
    }
    // print
    for (i =0 ; i < MAX*MAX; i++)
    {
        printf("%d\n",a[i]);
        if (a[i] == a[MAX*MAX - 1])
        {
            printf("\n\n");
        }
    }
    
    return 0;
}



