/**
*mario.c- PSET1
*
*crimsonblack
*
*Uses hashes to form the mario half-pyramid
*/

#include <cs50.h>
#include <stdio.h>

int main(void)
{
   int h;
   
   // asks the user for a number 
   do
   {
        printf("Height from 0 to 23:");
        h = GetInt();
   }
   while (h > 23 || h < 0);
   
   // user's input is height of pyramid: use nested loops to generate pyramid 
   
   // how high given height: i < h (height) determines this value  
   for (int i = 0; i < h; i++)
        {            
            // spaces: (height)-(int i)-(1) = h-1 for the nth row
            for (int y = 0; y < h-i-1; y++)
                {
                    printf(" "); 
                }
       
                // each row starts with 2 hashes: n+2 for the nth row
                for (int n = h-1; n < h ; n++)
                
                    {                               
                        printf("##");
                    
                     // n hashes added to each row: n+2 for the nth row
                     for (int x = 0; x < h+i-h; x++)
                            {
                                printf("#"); 
                            }               
                    }  
                                    // one loop determines each row, now skip a line for the next row
                                    printf("\n"); 
        }
}

// END
                                             
