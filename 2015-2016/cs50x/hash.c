#include <cs50.h>
#include <ctype.h>
#include <stdio.h>
#include <string.h>
int hash_function(char* key, int size)
{
    // TODO
    // calculates the sums of ascii values of each char in string
    unsigned int index = 1;
    // looping through string key and adding each time to index
    for (int i = 0; i < strlen(key); i++)
    {	
        // index += key[i] * 0.1;
        index = (index * key[i] + key[i] + i);// << 1;
    }
    // mod by size to keep within boundaries of hash table
    return index % size;
}

int main(int argc, char* argv[])
{
    // get hash table size
    printf("Hash table size: ");
    int size = GetInt();

    // get key
    printf("Key: ");
    char* key = GetString();

    // calculate and print index
    printf("The string '%s' is mapped to index %i\n", key, 
    hash_function(key, size));

    return 0;
}

