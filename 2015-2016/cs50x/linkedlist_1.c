// spec: "return the length of a singly linked list"

#include <assert.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

#define SIZE 10

typedef struct node
{
    // the value to store in this node
    int n;

    // the link to the next node in the list
    struct node* next;
}
node;

node* head = NULL;

int length(node* head)
{
    // TODO
    node* ptr = head;
    if (ptr == NULL)
        return 2;
    
    int counter = 1;
    while(ptr->next != NULL)
    {
        ptr = ptr->next;
        counter++;
    }
    return counter;
}

int main(int argc, char* argv[])
{
    // create linked list
    for (int i = 0; i < SIZE; i++)
    {
        node* new = malloc(sizeof(node));

        if (new == NULL)
        {
            exit(1);
        }
        new->n = i;
        new->next = head;
        head = new;
    }

    printf("Making sure that list length is indeed %i...\n", SIZE);

    // test length function
    assert(length(head) == SIZE);
    printf("good!\n");

    return 0;
}

