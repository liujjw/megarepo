/**
*mario.c- PSET1
*
*crimsonblack
*
*Uses asterisks to form the mario half-pyramid
*/

#include <cs50.h>
#include <stdio.h>

int main(void)
{
   int h;
   
   // asks the user for a number 
   do
   {
        printf("Choose a height from 0 to 23:");
        h = GetInt();
   }
   while (h > 23 || h < 0);
   
   // user's input forms height of pyramid     
   
   for (int i = 0; i < h; i++)
        {            
            for (int y=0; y<h-i-1; y++)
                {
                    printf(" "); 
                }
            
                for (int n = h-1; n < h ; n++)
                
                    {                               
                        printf("##");
                    
                     for (int x=0; x<h+i-h; x++)
                            {
                                printf("#"); 
                            }               
                    }  
            printf("\n"); 
        }
}                                                    
