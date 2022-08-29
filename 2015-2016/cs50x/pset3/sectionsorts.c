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




// BUBBLE SORT //
/*intialize counter
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
        
        // swap adjacent elements if out of order
        
            // iterate through array
            for (int i = 0; i < n - 1 - k; i++)
            {
                // check if array[n] and array[n + 1] are in order
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
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


// SELECTION SORT //
/* 
for i = 0 to n -2
    min = i
    for j = i + 1 to n -1
        if array[j] < array[min]
            min = j;
    if min != i
        swap array[min] and array[i]
*/

/**
* Sorts array in place using selection sort
*/
void sort(int array[], int size)
{
    // iterate over array
    for(int i = 0; i < size - 1; i++)
    {
        //smallest element and its position in sorted array
        int smallest = array[i]
        int position = i;
                
        // look through unsorted part of array
        for (int k = i + 1; k < size; k++)
        {            
                // find the next smallest element
                if (array[k] < smallest)
                {
                    smallest = array[k];
                    position = k;
                } 
        }        
        // swap
        int temp = array[i];
        array[i] = smallest;
        array[position] =  temp;
        
    } 
}


// ~~~~~~~~~~~~~~

// INSERTION SORT
/*
for i = 0 to n -1 
    element = array[i]
    j = i
    while (j > 0 and array[j - 1] > element
        array[j] = array[j - 1]
        j = j - 1
    array[j] = element
*/

// ~~~~~~~~~~~~~~~

//  MERGE SORT
/*
sort(int array[], int start, int end)
{
    if (end > start)
    {
        int middle = (start + end) / 2;
        
            sort(array, start, middle);
            sort(array, middle + 1, end);
            
            merge(array, start, middle, middle + 1, end);
    }
}
/*
