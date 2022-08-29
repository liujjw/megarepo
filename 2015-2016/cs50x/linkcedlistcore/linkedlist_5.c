#include <stdio.h>
#include <stdlib.h>

#define SIZE 5

typedef struct node
{
    // the value to store in this node
    int n;

    // the link to the next node in the list
    struct node* next;
}
node;

node* head = NULL;

void insert_sorted(int i)
{
    // TODO
    // make a new node for i
    // fill it in
    // make next pointer null
    // check for smaller/larger number
    // insert between those numbers
    // else if none, just keep pointer to null or point successively 

	node* node_1 = malloc(sizeof(node));
    node_1->n = i;
    node_1->next = NULL;
    
    node* prev_1 = NULL;
    node* crawler = head;
    int inserted = 0;
    int head_init = 0;
    while (crawler != NULL)
    {
     	prev_1 = crawler;
     	if ((*crawler).n > (*node_1).n)
        {
            
            node_1->next = prev_1->next;
            prev_1->next = node_1;
            inserted++;
            break;
    
        }
        else
            prev_1 = crawler;
        crawler = crawler->next;
    }
    if(head == NULL)
    {
        head = node_1;
        head_init++;
        
    }
    if(inserted != 1 && head_init != 1)
    {
        prev_1->next = node_1;
    }    

}
int main(int argc, char* argv[])
{
    printf("Inserting %i random ints to the list...\n", SIZE);
    for (int i = 0; i < SIZE; i++)
    {
        insert_sorted(rand() % SIZE);
    }
    printf("done!\n");

    // printing out list
    printf("Your list now contains ");
    for (node*  ptr = head; ptr != NULL; ptr = ptr->next)
    {
        printf("%i ", ptr->n);
    }
    printf("\n");

    return 0;
}

