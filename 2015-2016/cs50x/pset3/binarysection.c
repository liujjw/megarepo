/**
 * Returns true if value is in array of n values, else false.
 */
 
bool search(int value, int values[], int n)
{
    // Set values for the top and the bottom of the search 
    int lower = 0;
    int upper = n -1;
    
    // Binary search
    while (lower <= upper)
    {
        // find the middle
        int middle = (upper + lower) / 2;
        
        // compare middle to value_wanted
        if (values[middle] == value)
        {
            return true;
        }
        else if (value[middle] < value)
        {
            lower = middle + 1;
        }
        else if (value[middle] > value)
        {
            upper = middle - 1;
        }
        
    }
    
    return false;
}
