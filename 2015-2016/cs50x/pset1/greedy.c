/**
*crimsonblack_pset1_greedy
*
*Asks for change and uses a greedy algorithm to return
*said change in fewest possible coins
*/
#include <stdio.h>
#include <cs50.h>
#include <math.h>

int main(void)
{
    // prompts user for non-negative dollars in change
    float change;
    do
    { 
        printf("Yo how much change owed:");
        change = GetFloat();
    }
    while (change < 0);
    
    // converting: (int) casts the rounded float change*100 into an int
    int ichange;
    ichange = (int) roundf(change * 100);
    
    // initalizes coinsused to 0
    int coins = 0;
    // tests quarters and subtracts from value and adds to coins count
    while (ichange - 25 >= 25 || ichange >= 25)
    {
        coins++;
        ichange = ichange - 25;
    }
    
    // tests dimes
    while (ichange - 10 >= 10 || ichange >= 10)
    {
        coins++;
        ichange = ichange - 10;
    } 
    
    // tests nickels
    while (ichange - 5 >= 5 || ichange >= 5)
    {
        coins++;
        ichange = ichange - 5;
    } 
    
    // tests pennies
    while (ichange - 1 >= 1 || ichange >= 1)
    {
        coins++;
        ichange = ichange - 1;
    }  
    
    // printing final answer, value of coins
    printf("%i\n", coins);     
}
// END
