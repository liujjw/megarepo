#include <stdio.h>
#include <string.h>
#include <math.h>
#include <stdlib.h>
#include <cs50.h>
#define CLASS_SIZE 20

int main(int argc, string argv[])

{
    // delcare array
    int scores_array[CLASS_SIZE];
    
    //populate array
    for(int i = 0; i < CLASS_SIZE; i++)
    {
        printf("Give me a score for student %i: ", i);
        scores_array[i] = GetInt();       
    }
    for(int k = 0; k < CLASS_SIZE; k++)
    {
        printf("Here are the scores for Student %i: %i\n", k, scores_array[k]);
       
    }
}
/*<data type> name[<size>]
*int temperature[3];
*temperature[0] = 65;
*int temperature[] = {65,66,67};
*/
