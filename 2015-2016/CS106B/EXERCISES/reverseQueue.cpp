#include <iostream>
#include <fstream>
#include <sstream>
#include <cstdlib>
#include "console.h"
#include "vector.h"
#include "grid.h"
#include "simpio.h"
#include "filelib.h"
#include "strlib.h"
#include "set.h"
#include "stack.h"
#include "queue.h"
#include "map.h"
#include "iomanip.h"
using namespace std;

void reverseQueue(Queue<int> & aQueue){
	Stack<int> myStack;
	while(!aQueue.isEmpty())
		myStack.push(aQueue.dequeue());
	while(!myStack.isEmpty())
		aQueue.enqueue(myStack.pop());
}

int main(){
    Queue<int> myQueue;
    while(true){
        int ints = getInteger("?");
        if(ints == 0) break;
        myQueue.enqueue(ints);
    }

    reverseQueue(myQueue);
    while(!(myQueue.isEmpty())){
    	cout << myQueue.dequeue() << endl;
    }

}


