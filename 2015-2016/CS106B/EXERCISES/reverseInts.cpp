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

int main(){
    Stack<int> myStack;
    while(true){
        int ints = getInteger("?");
        if(ints == 0) break;
        myStack.push(ints);
    }
    while(!(myStack.isEmpty())){
        cout << myStack.pop(); << endl;
    }

}


