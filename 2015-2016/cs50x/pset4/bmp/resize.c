// /**
// * copy.c
// *
// * Computer Science 50
// * Problem Set 4
// *
// * Copies a BMP piece by piece, just because.
// */

/** 
*crimson_black | 8_20_15
*copy of copy.c
*
*Resizes BMPs
*/
       
#include <stdio.h>
#include <stdlib.h>

#include "bmp.h"

#define PIXEL 3     // a pixel has 3 bytes 
#define HEADERS 54      // size of INFOHEADER + FILEHEADER in bytes

int main(int argc, char* argv[])
{
    // ensure proper usage 
    if (argc != 4)
    {
        printf("Usage: ./resize 1 < size < 100 infile outfile\n");
        return 1;
    }
    int size = atoi(argv[1]);
    if (size > 100 || size < 0)
    {
        printf("Usage: ./resize 1 < size < 100 infile outfile\n");
        return 1;
    }
    
    // remember filenames
    char* infile = argv[2];
    char* outfile = argv[3];

    // open input file 
    FILE* inptr = fopen(infile, "r");
    if (inptr == NULL)
    {
        printf("Could not open %s.\n", infile);
        return 2;
    }

    // open output file
    FILE* outptr = fopen(outfile, "w");
    if (outptr == NULL)
    {
        fclose(inptr);
        fprintf(stderr, "Could not create %s.\n", outfile);
        return 3;
    }

    // read infile's BITMAPFILEHEADER
    BITMAPFILEHEADER bf;
    fread(&bf, sizeof(BITMAPFILEHEADER), 1, inptr);

    // read infile's BITMAPINFOHEADER
    BITMAPINFOHEADER bi;
    fread(&bi, sizeof(BITMAPINFOHEADER), 1, inptr);
   
    // ensure infile is (likely) a 24-bit uncompressed BMP 4.0
    if (bf.bfType != 0x4d42 || bf.bfOffBits != 54 || bi.biSize != 40 || 
        bi.biBitCount != 24 || bi.biCompression != 0)
    {
        fclose(outptr);
        fclose(inptr);
        fprintf(stderr, "Unsupported file format.\n");
        return 4;
    }
    
    // changing outfile copy's headers to match resizing 
    int og_width = bi.biWidth;
    int og_height = bi.biHeight;
    bi.biWidth = bi.biWidth * size;
    bi.biHeight = bi.biHeight * size;

    // write outfile's BITMAPFILEHEADER         
    int image = bi.biWidth * bi.biHeight * PIXEL;
    int scanwidth = bi.biWidth * PIXEL;     
    if (image < 0)
    {
        if (scanwidth % 4 == 1)
            image = image - (3 * abs(bi.biHeight));
        if (scanwidth % 4 == 2)
            image = image - (2 * abs(bi.biHeight));
        if (scanwidth % 4 == 3)
            image = image - (1 * abs(bi.biHeight));    
        bf.bfSize = abs(image - HEADERS); 
    }   
    else
    {
        if (scanwidth % 4 == 1)
            image = image + (3 * abs(bi.biHeight));
        if (scanwidth % 4 == 2)
            image = image + (2 * abs(bi.biHeight));
        if (scanwidth % 4 == 3)
            image = image + (1 * abs(bi.biHeight));
        bf.bfSize = abs(image + HEADERS);
    }        
    fwrite(&bf, sizeof(BITMAPFILEHEADER), 1, outptr);

    // write outfile's BITMAPINFOHEADER        
    bi.biSizeImage = abs(image);    
    fwrite(&bi, sizeof(BITMAPINFOHEADER), 1, outptr);

    // determine padding for scanlines
    int padding =  (4 - (og_width * sizeof(RGBTRIPLE)) % 4) % 4;
           
    // iterate over infile's scanlines
    for (int i = 0, biHeight = abs(og_height); i < biHeight; i++)
    {
        // iterating over size rows 
        for (int m = 1; m <= size; m++)
        {           
            // iterate over pixels in scanline
            for (int j = 0; j < og_width; j++)
            {             
                // temporary storage
                RGBTRIPLE triple;

                // read RGB triple from infile
                fread(&triple, sizeof(RGBTRIPLE), 1, inptr);
                        
                // write RGB triple to outfile
                for (int l = 1; l <= size; l++)   
                    fwrite(&triple, sizeof(RGBTRIPLE), 1, outptr);       
            }
            // skip over padding, if any
            fseek(inptr, padding, SEEK_CUR);

            // then add it back (to demonstrate how)
            for (int k = 0; k < (4 - (bi.biWidth * sizeof(RGBTRIPLE)) % 4) % 4; k++)
            {                            
                fputc(0x00, outptr);
            }         
            
            // start over in read file until m size vertical rows is complete
            fseek(inptr, -(padding + (sizeof(RGBTRIPLE) * og_width)), SEEK_CUR);                               
        }
        fseek(inptr, padding + (sizeof(RGBTRIPLE) * og_width), SEEK_CUR);
    }
    
    // close infile
    fclose(inptr);

    // close outfile
    fclose(outptr);

    // that's all folks
    return 0;
}
