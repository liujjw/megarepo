#include <iostream>
#include <fstream>
#include <sstream>
#include <cstdlib>
#include <string>
#include "console.h"
#include "vector.h"
#include "grid.h"
#include "simpio.h"
#include "filelib.h"
#include "strlib.h"
#include "set.h"
#include "stack.h"
#include "error.h"
#include "queue.h"
#include "map.h"
#include "iomanip.h"
using namespace std;

/* Shifts each of the elements in a given vector
 * by one to the left towards index 0.
 * The effect if this is to simulate a stack's elements
 * shifted towards the pop.
 */
void shiftAllByOne(Vector<int> & buffer){
    int tmpValue = buffer[buffer.size() - 1]; // set tmp to last value
    for(int i = buffer.size() - 1; i >= 0; i--){ // start at the end
        int leftIndex = i - 1; // left index is the adjacent index of value on the left
        if(leftIndex < 0) leftIndex = buffer.size() - 1; // if i is 0 then left index(i - 1)loops around to end
        int rightValue = tmpValue; // right value was past left value(ik hard to explain)
        tmpValue = buffer[leftIndex]; // tmp saves the left index's value
        buffer[leftIndex] = rightValue; // left index value gets changed to right value
    }
}

void roll(Stack<int> & stack, int n, int k){
    if(n < 0 || k < 0 || n > stack.size())
        error("roll: arguement out of range");

    Vector<int> buffer; // read the top n elements wanted, pop first
    for(int i = 0; i < n; i++)
        buffer.add(stack.pop());

    for(int i = 0; i < k; i++)
        shiftAllByOne(buffer);
    // with subset of n, shift them all by the requested k times circularly towards pop

    for(int i = buffer.size() - 1; i >= 0; i--)
        stack.push(buffer[i]);
    // push everything back to stack from buffer end first so closer to 0 ends up on top

}

int main(){
    Stack<int> stack;
    while(true){
        int n = getInteger("?");
        if(n == 0) break;
        stack.push(n);
    }
    roll(stack, 4, 1);
    cout << endl;
    cout << stack;
    return 0;

}


