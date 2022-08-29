/*
*crimsonblack: pset2_vigenere
*2015/8/5
*
*An implementation of the Vigenere Cipher
*User types, in the command line, a key and in new line, plaintext
*Output is a vigenere ciphertext via:
*    
*   formula: c = (plaintext[i] + keyword[j]) % 26
*/


#include <stdio.h>
#include <cs50.h>
#include <stdlib.h>
#include <ctype.h>
#include <string.h>

int main(int argc, string argv[])
{        
        // input 
    // checks for two argc and for key(argv[1]) to not have digits  
    if (argc != 2)
    {
        printf("Usage: ./vigenere <keyword>\n");
        return 1;
    }     
    string keyword = argv[1];
    for(int arg = 0, org = strlen(keyword); arg < org; arg++)
    {
        if (isdigit(keyword[arg]))
        {
            printf("Usage: ./vigenere <non-digit keyword>\n");
            return 1;
        }
    }
    string plaintext = GetString();
    
    
        // ciphering
    // increments over the length of plaintext for each new enciphered char
    // checks if alphabet, plaintext and keyword char upper or lower case
    // int encipher/cap assigns the jth char in keyword...
            // to add to ith char in plaintext
    
    // if jth keyword is upper case, change lower subtraction... 
            // by upper case to find correct difference added to plaintext
    
    // modulo ensures wraparound in alphabet(26) and keyword(strlen)
    
    for(int i = 0, n = strlen(plaintext), j = 0; i < n; i++, j++)
    {        
        if (isalpha(plaintext[i]))
        {                                                           
                // lowercase chars
            if (islower(plaintext[i]))
            {                     
                int encipher = (((keyword[j % strlen(keyword)]) - 'a') + (plaintext[i] - 'a')) % 26;
                if (isupper((keyword[j % strlen(keyword)])))
                {
                    encipher = (((keyword[j % strlen(keyword)]) - 'A') + (plaintext[i] - 'a')) % 26;     
                    printf("%c", (encipher + 'a'));
                }
                else
                {
                    printf("%c", (encipher + 'a'));
                }                       
            }
                                
                // uppercase chars
            if (isupper(plaintext[i]))
            {
                int enciphercap = ((toupper(keyword[j % strlen(keyword)]) - 'A') + (plaintext[i] - 'A')) % 26;
                if (isupper((keyword[j % strlen(keyword)])))
                {
                    enciphercap = (((keyword[j % strlen(keyword)]) - 'A') + (plaintext[i] - 'A')) % 26;     
                    printf("%c", (enciphercap + 'A'));
                }
                else
                {
                    printf("%c", (enciphercap + 'A'));
                }
                           
            } 
        }         

        // prints non-alphabets(spaces, punct, etc)
        // decrements the value j to wait for new key increment applied to plaintext 
        else
        {           
            printf("%c", plaintext[i]);
            j--;                  
        }   
    }
    printf("\n");
    
    return 0;
}
// END
