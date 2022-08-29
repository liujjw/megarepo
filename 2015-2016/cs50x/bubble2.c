/*#include <stdio.h>

#define MAX 3

int a[MAX*MAX];

int rand_seed = MAX;
    
/* from K&R
   - returns random number between 0 and 32767.*/
/*int random()
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
    /* bubble sort the array 
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
    */   
    
    // print
    /*for (i =0 ; i < MAX*MAX; i++)
    {
        printf("%d\n",a[i]);
        if (a[i] == a[MAX*MAX - 1])
        {
            printf("\n\n");
        }
    }
    
    return 0;
}
    */










// radix sort, by guy from Quora or something
    void radix_sort(unsigned *begin, unsigned *end)
    {
        unsigned *begin1 = new unsigned[end - begin];
        unsigned *end1 = begin1 + (end - begin);

        for (unsigned shift = 0; shift < 32; shift += 8) {
            size_t count[0x100] = {};
            for (unsigned *p = begin; p != end; p++)
                count[(*p >> shift) & 0xFF]++;
            unsigned *bucket[0x100], *q = begin1;
            for (int i = 0; i < 0x100; q += count[i++])
                bucket[i] = q;
            for (unsigned *p = begin; p != end; p++)
                *bucket[(*p >> shift) & 0xFF]++ = *p;
            std::swap(begin, begin1);
            std::swap(end, end1);
        }

    delete[] begin1;
    }




/*intialize counter(cs5 section)
do
{
    set counter to 0
    
    iterate through entire array
        if array[n] > array[n + 1]
            swap them 
            increment counter
}
while (counter > 0)
*/

void sort(int array[], int n)
{
    // cycle through array
    for(int k = 0; k < n - 1; k++)
    {
        // optimize; check if there are no swaps
        int swaps = 0;
        
        // swao adjacent elements if out of order
        
            // iterate through array
            for (int i = 0; i < n - 1 - k; i++)
            {
                // check if array[n[ and array[n + 1] are in order
                if (array[i] > array[i+1])
                {
                    int temp = array[i+1];
                    array[n+1] = array[i];
                    array[i] = temp;
                    swaps++;
                }
            }
        
        
        if (!swaps)
            break;
            

    }

    
}
