

// recursion????
// shallow.deep copying/ externstatic const
// pass by reference or copy

#include "HeapPriorityQueue.h"

HeapPriorityQueue::HeapPriorityQueue() {
    array = new PQEntry[initCapacity];
    arraySize = 0;
    arrayCapacity = initCapacity - 1; // stores 9 elements only, index 0 always empty
}

HeapPriorityQueue::~HeapPriorityQueue() {
    delete[] array;
}

void HeapPriorityQueue::changePriority(string value, int newPriority) {
    int valIndex = -1;
    int incrm = 1;
    while(incrm <= arraySize) { // look through all elems, find and set the values index
        if(array[incrm].value == value) {
            valIndex = incrm;
            break;
        }
        incrm++;
    }
    if(valIndex == -1) {
        throw "Value not found.";
    }

    int relativesIndex = -1;
    if(array[valIndex].priority < newPriority) {
        throw "Illegal to make priority less urgent.";
    }
    array[valIndex].priority = newPriority;
    relativesIndex = valIndex / 2;
    while(relativesIndex >= 1) {
        if((array[valIndex].priority < array[relativesIndex].priority) ||
                        (array[valIndex].priority == array[relativesIndex].priority
                         && array[valIndex].value < array[relativesIndex].value)) {
            PQEntry tmp = array[relativesIndex]; // swap child with its parent
            array[relativesIndex] = array[valIndex];
            array[valIndex] = tmp;
            valIndex = relativesIndex;
            relativesIndex = valIndex / 2;
        } else {
            break;
        }
    }
}

void HeapPriorityQueue::clear() {
    delete[] array; // reset everything
    array = new PQEntry[initCapacity];
    arraySize = 0;
    arrayCapacity = initCapacity - 1;
}

string HeapPriorityQueue::dequeue() {
    string returnVal = array[1].value; // always first value
    if(returnVal == "" && array[1].priority == 0) {
        throw "Empty queue.";
    }

    // move elem from last place elem to now be head
    PQEntry empty("", 0);
    array[1] = array[arraySize];
    array[arraySize] = empty;
    arraySize--;

    // bubble new elem at index 1 down with one of its children, the more urgent one
    int curPQIndex = 1;
    int childIndex1 = curPQIndex * 2;
    int childIndex2 = childIndex1 + 1;
    while(true) {
        int indexToCheck = -1;
        if(childIndex1 > arraySize) { // if child1 out of bounds, child2 by definiton too
            break;
        } else if(childIndex2 <= arraySize) { // if both are in bounds
            if(array[childIndex1].priority < array[childIndex2].priority ||
                    ((array[childIndex1].priority == array[childIndex2].priority) &&
                    (array[childIndex1].value < array[childIndex2].value))) {
                indexToCheck = childIndex1;
            } else {
                indexToCheck = childIndex2;
            }
        } else { // if only child1 in bounds
            indexToCheck = childIndex1;
        }
        if((array[curPQIndex].priority > array[indexToCheck].priority) ||
                (array[curPQIndex].priority == array[indexToCheck].priority
                 && array[curPQIndex].value > array[indexToCheck].value)) {
            PQEntry tmp = array[curPQIndex];
            array[curPQIndex] = array[indexToCheck];
            array[indexToCheck] = tmp;
            curPQIndex = indexToCheck;
            childIndex1 = curPQIndex * 2;
            childIndex2 = childIndex1 + 1;
        } else { // only if we can swap with child
            break;
        }
    }
    return returnVal;
}

// double capacity, make new array with it, and copy from old to new
// delete the old, make array point to new one
void HeapPriorityQueue::checkCapacity() {
    if(arraySize == arrayCapacity) {
        arrayCapacity *= 2;
        PQEntry* newArray = new PQEntry[arrayCapacity];
        for(int i = 1; i <= arraySize - 1; i++) {
            newArray[i] = array[i];
        }
        delete[] array;
        array = newArray;
    }
}

void HeapPriorityQueue::enqueue(string value, int priority) {
    // checks size and capacity
    arraySize += 1;
    checkCapacity();

    // creates a new entry object and place it at the first open spot
    PQEntry newEntry(value, priority);
    array[arraySize] = newEntry;

    // percolate up as long as priority is of new pqentry is less than its parent
    // its parent is at newentry's size / 2 (heap structure)
    int newNodeIndex = arraySize;
    int parentIndex = newNodeIndex / 2;
    while(parentIndex >= 1) {
        // incldues code to break lexicographic ties when prioirty is the same
        if(((array[newNodeIndex]).priority < (array[parentIndex]).priority)
                || (array[newNodeIndex].priority == array[parentIndex].priority &&
                    array[newNodeIndex].value < array[parentIndex].value)) {
            PQEntry tmp = array[parentIndex];
            array[parentIndex] = array[newNodeIndex];
            array[newNodeIndex] = tmp;
            newNodeIndex = parentIndex;
            parentIndex = newNodeIndex / 2;
        } else {
            break;
        }
    }
}

bool HeapPriorityQueue::isEmpty() const {
    return (array[1].priority == 0 && array[1].value == "");
}

string HeapPriorityQueue::peek() const {
    if(array[1].value == "" && array[1].priority == 0) {
        throw "Empty queue.";
    }
    return array[1].value;
}

int HeapPriorityQueue::peekPriority() const {
    if(array[1].value == "" && array[1].priority == 0) {
        throw "Empty queue.";
    }
    return array[1].priority;
}

int HeapPriorityQueue::size() const {
    return arraySize;
}

ostream& operator<<(ostream& out, const HeapPriorityQueue& queue) {
    cout << "{";
    for(int i = 1; i <= queue.arraySize; i++) {
        cout << queue.array[i];
        if(i < queue.arraySize) {
            cout << ", ";
        }
    }
    cout << "}";
    return out;
}
