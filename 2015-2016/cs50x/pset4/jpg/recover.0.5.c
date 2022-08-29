/**
 * recover.c
 *
 * Computer Science 50
 * Problem Set 4
 *
 * Recovers JPEGs from a forensic image.
 */
 
 /**
 *crimson_black  [8.2015--9.14.2015--10.11.2015-- took me two months!]
 */

#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>

// fseek the cursor after 4 byte buffer
#define BLOCK_AFTER4B 508

// 8 bits for one jpeg pattern 
typedef uint8_t  BYTE;


int main(void)
{
    // TODO
    // open input image file, error checking
    FILE* card_raw = fopen("card.raw", "r");
    if (card_raw == NULL)
    {    
        printf("Could not open\n");
        return 1;
    }
    
    // misc:storage for name of each file, counter i for tracking consecutive files
    char file_name[10];
    int i = 0;
    
    // a buffer for checking patterns of jpegs
    typedef struct CHUNK
    {
        BYTE one;
        BYTE two;
        BYTE three;
        BYTE four;
    }
    CHUNK;
    
    CHUNK pattern;
    
    // main stuff here
    // loop NECESSARY for reading through entire input file, moves cursor by pattern bytes
    while (fread(&pattern, sizeof(pattern), 1, card_raw) == 1)    
    {   
        // moves cursor another pattern, loop NECESSARY for reading through each 512 byte chunk
        // tandem with above (while) so they read through entire file and entire 512 chunk      
        while (fread(&pattern, sizeof(pattern), 1, card_raw) == 1)    
        {
            // go back 8 bytes from two patterns advanced cursor
            fseek(card_raw, -8, SEEK_CUR);
            // now fread in 
            fread(&pattern, sizeof(pattern), 1, card_raw);
            // exhaustive checks if fread pattern satisfies jpegs patterns
            // else advance 508 bytes and break to complete 512 byte chunk
            if (pattern.one == 0xff) 
            { 
                if (pattern.two == 0xd8)
                { 
                    if (pattern.three == 0xff)
                    {               
                        if (pattern.four == 0xe0 || pattern.four == 0xe1) 
                        {
                            // *(code)printf("we have a winner\n");
                            // found a jpeg!
                            // using space in memory to store name for output
                            // satisfies 00x.jpg parameter
                            sprintf(file_name, "00%i.jpg", i);
                            if (i > 9)
                                sprintf(file_name, "0%i.jpg", i);
                            
                            // open new output file and append
                            FILE* storage = fopen(file_name, "a");
                            // first write in the correct fread pattern
                            fwrite(&pattern, sizeof(pattern), 1, storage);   
                            
                            // now loop read and write rest of jpeg until card.raw end or ->
                            // another pattern of jpeg patterns is found
                            while (5 > 2)
                            {       
                                if (fread(&pattern, sizeof(pattern), 1, card_raw) != 1)
                                {
                                    fclose(storage);
                                    i++;
                                    break;
                                }
                                fseek(card_raw, -4, SEEK_CUR);
                                fread(&pattern, sizeof(pattern), 1, card_raw);
                                
                                if(pattern.one == 0xff)
                                { 
                                    if(pattern.two == 0xd8)
                                    {    
                                        if(pattern.three == 0xff)
                                        {    
                                            if(pattern.four == 0xe0 || pattern.four == 0xe1)
                                            {
                                                fclose(storage);
                                                i++;
                                                break;
                                            }
                                            else 
                                               fwrite(&pattern, sizeof(pattern), 1, storage);   
                                        }
                                        else 
                                            fwrite(&pattern, sizeof(pattern), 1, storage);   
                                    }
                                    else 
                                        fwrite(&pattern, sizeof(pattern), 1, storage);                                 
                                }
                                else 
                                    fwrite(&pattern, sizeof(pattern), 1, storage);                
                            }
                        
                        }
                        else
                        {    
                            fseek(card_raw, BLOCK_AFTER4B, SEEK_CUR);
                            break;
                        }
                    }
                    else
                    {    
                        fseek(card_raw, BLOCK_AFTER4B, SEEK_CUR);
                        break;
                    }
                }
                else
                {    
                    fseek(card_raw, BLOCK_AFTER4B, SEEK_CUR);
                    break;
                }
            }
 
            else
            {    
                fseek(card_raw, BLOCK_AFTER4B, SEEK_CUR);
                break;
            }

        }
    }
    // closing card.raw that was opened  
    fclose(card_raw); 
        
}
// END
