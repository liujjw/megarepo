#include <cs50.h>
#include <math.h>
#include <stdio.h>

/**     Change-greedy.c-pset1-crimsonblack
*   
*get amount in dollars
*   (while) quarters can be used
*       increase count
*       amount decrease by a quarter
*   (while) dimes can be used
*       increase count
*       amount decrease by a dime
*   and so on...
*   print number of coins used
*/


    // function prototypes intialized
// prototype
double hundred(double m);

// prototype
int subtract25(int k);

// prototype
int subtract10(int f);

// prototype
int subtract5(int g);

// prototype
int subtract1(int l);


int main(void)
{
    // prompt user for non-negative US dollars in change
    float m;
    do
    {
        printf("Yo how much change owed?\n");
        m = GetFloat();
        if (m < 0)
            {
            printf("Can't be negative! Retry\n");
            }    
    }
    while (m < 0);
     
    // converting!!!  (int) explicitly casts the rounded float m*100 into an int instead of an float
    int b;
    b = (int) roundf(hundred(m));
            
    ///printf("%i\n", b);//**
     
    
           // calculating using proto functions and while loops        
            int t = 0;
            while (b-25>=25|| b>=25)
            {   
                t++;
                b = subtract25(b);               
            }
             
                                   
            while (b-10>=10|| b>=10)
            {
                t++;
                b = subtract10(b);                
            }
            
                                    
            while (b-5>=5|| b>=5)
            {
                t++;
                b = subtract5(b);                
            }
            
                                    
            while (b-1>=1|| b>=1)
            {
                t++;
                b = subtract1(b);                
            }
 
 // printing final answer, value of t                       
printf("%i\n", t);
}

    // prototype functions defined
// times hundred
double hundred(double n)
{           
    return n*100;
}
            
// subtract25
int subtract25(int k)
{
    return k-25;
}  
            
// subtract10
int subtract10(int f)
{
    return f-10;
}  
        
// subtract5
int subtract5(int g)
{
    return g-5;
} 
            
// subtract1
int subtract1(int l)
{
    return l-1;
}                  

// END
