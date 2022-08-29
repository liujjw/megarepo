/**
 * recover.c
 *
 * Computer Science 50
 * Problem Set 4
 *
 * Recovers JPEGs from a forensic image.
 */
 
 /**
 *crimson_black  [8.2015--9.14.2015--10.11.2015]
 */

#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>

// for pattern detection to be numerical, but still 8 bits to be checkable
typedef uint8_t BYTE;

int main(void)
{
	// opening input and checking
    FILE* card_raw = fopen("card.raw", "r");
    if (card_raw == NULL)
    {
        printf("could not open card.raw\n");
        return 1;
    }
    
    // counter i, char array for filenames, a buffer of 512 8-bit BYTES 
    int i = 0;
    char file_name[10];
    BYTE buffer[512];
    
    // loop until no more 512 bytes chunks in card.raw
    while(fread(&buffer, 512, 1, card_raw) == 1)
    {
        // making sure 512 bytes is not skipped when the check is performed above
        fseek(card_raw, -512, SEEK_CUR);
        
        // reading first 512 bytes    
        fread(&buffer, 512, 1, card_raw);
        
        // checking in first 4 bytes in buffer for jpeg patterns    
        if(buffer[0] == 0xff && buffer[1] == 0xd8 && buffer[2] == 0xff && (buffer[3] == 0xe0 || buffer[3] == 0xe1))
        {
            // jpeg found: store 00x.jpg into file_name buffer and use it to open a file 
            sprintf(file_name, "00%i.jpg", i);
            if (i > 9)
                sprintf(file_name, "0%i.jpg", i);
                
            FILE* storage = fopen(file_name, "a");
            
            // write in first 512 bytes into new output
            fwrite(&buffer, 512, 1, storage);
            
            // continue writing new 512 blocks until new jpeg pattern is found or eof    
            while(fread(&buffer, 512, 1, card_raw) == 1)
            {
                // check for skipping
                fseek(card_raw, -512, SEEK_CUR);
                
                // reading another 512
                fread(&buffer, 512, 1, card_raw);
                
                // checking if any new pattern for jpeg to signal end of this jpeg
                // (jpegs continuous on card.raw per spec in pset)       
                if(buffer[0] == 0xff && buffer[1] == 0xd8 && buffer[2] == 0xff && (buffer[3] == 0xe0 || buffer[3] == 0xe1))
                {
                    // 512 skip check so upper while loop can read in this new jpeg pattern block
                    fseek(card_raw, -512, SEEK_CUR);
                    // closing and updating counter
                    fclose(storage);
                    i++;
                    break;
                }
                // if not new jpeg, just write 512 into output file
                fwrite(&buffer, 512, 1, storage);
            }
         }

    }
    // closing card.raw   
    fclose(card_raw);    
}
// END
