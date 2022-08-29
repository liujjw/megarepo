/**
*mario.c- PSET1
*
*crimsonblack
*
*Uses hashes to form the mario half-pyramid
*/

#include <stdio.h>
#include <cs50.h>

int main(void)

{
    int height;
    // asks the user for a number
    do
    {       
        printf("Height:");
        height = GetInt();
    }
    while (height < 0 || height > 23);
    
    // how high given height: i < h (height) 
    for (int i = 0; i < height; i++)
    {
        // spaces: (height)-(int i)-(1) = h-1 for the nth row
        for (int space = 0; space < height - 1 - i; space++)
        {
            printf(" ");
        }   
        // each row starts with 2 hashes: n+2 for the nth row
        for (int k = 0; k < 1 ; k++)
        {
            printf("##");                 
            // n hashes added to each row: n+2 for the nth row
            for (int j = 0; j < height + i - height; j++)
            {
                printf("#");                   
            }
        }                                             
        printf("\n");        
    }
    
}
// END
