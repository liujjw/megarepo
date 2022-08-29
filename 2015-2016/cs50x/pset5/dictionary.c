/****************************************************************************
 * dictionary.c
 *
 * Computer Science 50
 * Problem Set 5
 *
 * Implements a dictionary's functionality.
 ***************************************************************************/

#include <stdbool.h>
 
// including more header files
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>


#include "dictionary.h"

// pre-processing hash table SIZE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
// reason we dont use full size is because collisions will be bound to occur since the computation atp is unpredicatable
 			// <http://stackoverflow.com/questions/14409466/simple-hash-functions>
 			// furthermore, we probably cant predict how much space we'll actually use b/c of collisions
 // a prime size."for even distribution across across hash space"

// diminished returns if SIZE is too big: thats because the hash function doesnt calulate that large of numbers anyway so they wont modulo
// load size seems to take the same amount of time regardless of size
// in the thousands seems to be much slower than 10 thousands  or even millions
 // @not prime faster in modulus?
 /*
// primes are MUCH faster, DOUBLE the SPEED!!!!!!!!!!!!!!!!!!!!!!!!!!!!
WORDS MISSPELLED:     1114
WORDS IN DICTIONARY:  143091
WORDS IN TEXT:        19190
TIME IN load:         0.36
TIME IN check:        0.13
TIME IN size:         0.00
TIME IN unload:       0.02
TIME IN TOTAL:        0.52

0.o
 */
// problem is they dont get that big anyway
#define SIZE 99991 //99991 // 1299827// 50333// 599843

 // size of LENGTH defined in dictionary.h


// struct for one bucket into hash table, with linked list functionality
typedef struct node
{
	// length 45 plus 1 for null term
    char real_word[LENGTH + 1];
	struct node* next_one;
}
node;

	// we want it to be spread equally <http://stackoverflow.com/questions/14409466/simple-hash-functions>
    // good ones also increase the size of table as hashing into 70-80% space, since hash tables need not be filled completely as collisons bound
                // to occur even with good algorithm at that scale
    // 0(1), constant time
    // adapted <http://stackoverflow.com/questions/14409466/simple-hash-functions>, initial testing gives decent results
    // <https://www.cs.hmc.edu/~geoff/classes/hmc.cs070.200101/homework10/hashfuncs.html>
 
// why unsigned int, solves negative hash problem
// we calculate same index with upper case letter and lower
// so if in text there's capital we can make same index
// however all words in dictionary must be lower case
// also, repeating words in dictionary don't seem to check properly
int hash_function_const(const char* key, int size)
{
    // function from stackoverflow user 

    unsigned int index = 1;
    int n = strlen(key);
    for(int i = 0; i < n; i++)
    {
        // if the key is uppercase, change it to lowercase first
        (key[i] >= 'A' && key[i] <= 'Z') ? (index *= ((key[i] + ('a' - 'A')) << 1) + i) : (index *= (key[i] << 1) + i);
        // @modifications, bitwise leftshift faster equivalent to (+ key[i]) == leftshift 1 == * 2 == * 2^ shift amount, syllogism
        // <http://stackoverflow.com/questions/20393373/performance-wise-how-fast-are-bitwise-operator-vs-normal-modulus>
            // bitwise modulus may not be necessary

    }
    return index % size;
}



int hash_function(char* key, int size)
{
    
    unsigned int index = 1;
    // @declaring n as strlen key
    int n = strlen(key); 
    for(int i = 0; i < n; i++)
    {
        // ternary conditionals!
        // not necessary here, lowercase assumed for all
        index *= (key[i] << 1) + i;
    }
    
    return index % size;

}


// a hash table of node ptr's 
node* hashtable[SIZE];

// size(), keeping track of how much words loaded in
int counter_SIZE = 0;

// case insensitive strcmp
bool strcmp_insensitive(const char* word, char* real_word)
{
    // loop through each word
    // lowercase them and compare with tolower 
    // apostrophes somewhere
    // if one is longer than the other
    // exit

    int counter = 0;
    for (int i = 0; word[i] != '\0' && real_word[i] != '\0'; i++)
    {
        // if the letter is capital chnge dictionary letter to capital, since const char we cannot modify char word
        if (word[i] >= 'A' && word[i] <= 'Z')
        {
            // if real_word is lowercase, might save some apostrophe cases
            if(real_word[i] >= 'a' && real_word[i] <= 'z')
            {   
                real_word[i] = (real_word[i] - 'a') + 'A';
            }
        }

        // check
        if (word[i] - real_word[i] == 0)
        {
            counter++;
        }

        // look at if statement following this one, it changes our real_word to a capital, so our dictionaryword will be capital from now 
        // on, so we must revert it here
        if(real_word[i] >= 'A' && real_word[i] <= 'Z')
        {
            real_word[i] = (real_word[i] - 'A') + 'a'; 
        }
    }
    // if all of word and real_word is used 
    if(counter == strlen(word) && counter == strlen(real_word))
    {
        return true;
    }
    
    return false;
}


/**
 * Returns true if word is in dictionary else false.
 */
bool check(const char* word)
{
    // hash function the word, if match
    // go to bucket
    // else return false, or maybe collision so
    // if true
    // strcompare first bucket and traaverse linked list for word

    // make function calculate same index with both upper and lower case
    int index = hash_function_const(word, SIZE);
    
    if (hashtable[index] == NULL)
    {
        return false;
    }
    else
    {
        if (strcmp_insensitive(word, hashtable[index]->real_word))
        {
            return true;
        }
        // check against next linked list
        else if (hashtable[index]->next_one != NULL)
        {
            node* head_cmp = hashtable[index]->next_one;
            while(head_cmp != NULL)
            {
                // make hashtable[index]->next_one its own ptr
                if (strcmp_insensitive(word, head_cmp->real_word)) 
                {
                    return true;
                }
                else
                {
                    head_cmp = head_cmp->next_one;
                }
            }
        }
    }
    return false;
}


/**
 * Loads dictionary into memory.  Returns true if successful else false.
 */
bool load(const char* dictionary)
{
    // open dictionary passed in 
    FILE* our_dictionary = fopen(dictionary, "r");
    if (our_dictionary == NULL)
    {
    	printf("Could not open dictionary\n");
    	return false;
    }
    
    // buffer for storing every word read in, length 46
    char word[LENGTH + 1];
    while (fscanf(our_dictionary, "%s", word) > 0)
    {
    	int index = hash_function(word, SIZE);
    	// malloc a node in index if there's no node already there
    	if(hashtable[index] == NULL)
    	{
    		hashtable[index] = malloc(sizeof(node));
            // copying over ith char in word to real word string in node ptr in hash table
			int i = 0;
			while (word[i] != '\0')
			{
				hashtable[index]->real_word[i] = word[i];
				i++;
			}
			// adding null terminator to end of word
			hashtable[index]->real_word[i] = '\0';
			// nulling next ptr
			hashtable[index]->next_one = NULL;
            counter_SIZE++;
    	}
    	
    	// what if something is already there??
        // traversing hashtable index
    	else 
    	{
            node* head = hashtable[index];
            // if there's other nodes, traverse (preemtpive hint at bot ptrs )
            while (head->next_one != NULL)
            {
                head = head->next_one;
            }

            // make a temporary node to store 
    		node* temp = malloc(sizeof(node));
    		// fill in the node
    		int i = 0;
    		// while there's no null ptr and therefore not end of string
    		while (word[i] != '\0')
    		{
    			temp->real_word[i] = word[i];
    			i++;
    		}
			// adding null terminator to end of word
    		temp->real_word[i] = '\0';
			temp->next_one = NULL;
    		
            // set the next node ptr proabbly already in hash index and point to this new entry
    		head->next_one = temp;
            counter_SIZE++;
            // releasing head if for when we call it again for good practice i guess
            head = NULL;
            temp = NULL;
        }
    	
    	
    }
    // if there is not at least one word in dictionary 
    if (counter_SIZE == 0)
    {
        printf("Dictionary has no words\n");
        return false;
    }    
    
    fclose(our_dictionary); 
    return true;
}


/**
 * Returns number of words in dictionary if loaded else 0 if not yet loaded.
 */
unsigned int size(void)
{
    // seems that after scoping the counter correctly, when load runs first its data gets updated into counter, but its not updating in this
    // specific file 
    return counter_SIZE;
}

/**
 * Unloads dictionary from memory.  Returns true if successful else false.
 */
bool unload(void)
{
    // set a cursor to first element, then set a temp to cursor, advance cursor, then free temp!!
    // much faster than go in and out in previous


    for (int i = 0; i < SIZE; i++)
    {
        node* cursor = hashtable[i]; 
        while(cursor != NULL)
        {
            node* temp = cursor;
            cursor = cursor->next_one;
            free(temp);
        }

    }
    
    return true;
}
