/*
*crimsonblack-pset2_caesar
*
*2015/8/3
*
*Ceasar Cipher implementation: user types ceasar key and 
*what they want encrypted, then output is encrypted.
*DECRYPTION?
*
*/

#include <stdio.h>
#include <cs50.h>
#include <string.h>
#include <ctype.h>
#include <stdlib.h>
#include <math.h>

int main(int argc, string argv[])
{        
    // whether there are two arguements in cmd line
    if (argc != 2)
    {    
        printf("Usage: ./caesar <non-negative integer>\n");
        return 1;               
    }  
        
    // function atoi changes string (the key) in argv[1] to an int
    int kcipher = atoi(argv[1]);    
    string plaintext = GetString();
            
    // iterates over each char in plaintext
    for(int i = 0, n = strlen(plaintext); i < n; i++)
    {
        // isalpha checks if alphabet
        if (isalpha(plaintext[i]))
        {
            // islower checks if lowercase
            if (islower(plaintext[i]))
            {
                // enciphering formula c = (p[i]+k) % 26
                int encipher = ((plaintext[i] - 97) + kcipher) % 26;
                // adds key integer to 'a', which is 97 
                printf("%c", encipher + 97);
            } 
            // isupper checks uppercase, see above
            if (isupper(plaintext[i]))
            {
                int enciphercap = ((plaintext[i] - 65) + kcipher) % 26;
                printf("%c", enciphercap + 65);   
            }
        }
        // prints non-alphabets without changing
        else
        {
            printf("%c", plaintext[i]);
        }
    }
    printf("\n");
            
                             
    return 0;
}
// END
