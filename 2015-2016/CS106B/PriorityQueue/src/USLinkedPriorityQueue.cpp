#include "USLinkedPriorityQueue.h"

USLinkedPriorityQueue::USLinkedPriorityQueue() {
    front = NULL;
}

USLinkedPriorityQueue::~USLinkedPriorityQueue() {
    while(front != NULL) {
        ListNode* nextNode = front->next;
        delete front;
        front = nextNode;
    }
}

void USLinkedPriorityQueue::changePriority(string value, int newPriority) {
    ListNode* curNode = front;
    if(curNode != NULL) {
        while(curNode->next != NULL && curNode->value != value) {
            curNode = curNode->next;
        }
        if(curNode->value != value) {
            throw "String value not found.";
        } else {
            if(curNode->priority < newPriority) {
                throw "New priority less urgent than current.";
            } else {
                curNode->priority = newPriority;
            }
        }
    } else {
        throw "Empty queue";
    }
}

void USLinkedPriorityQueue::clear() {
    while(front != NULL) {
        ListNode* nextNode = front->next;
        delete front;
        front = nextNode;
    }
    front = NULL;
}

string USLinkedPriorityQueue::dequeue() {
    // go through linked list and find the lowest number pri, delete it, and return its value //

    ListNode* curNode = front;
    if(curNode == NULL) {
        throw "Empty queue."; // checks if empty
    } else if(curNode->next == NULL) {
        string s = curNode->value;
        delete curNode;
        front = NULL;
        return s; // if only one node, return its val and delete it, set front to null
    }
    ListNode* lowestNode = front;
    ListNode* linkerNode = front; // sets two location nodes to front
    while(curNode->next != NULL) { // cur will never be used when it points to last element
        if(curNode->next->priority < lowestNode->priority || // if the next cur elem pri less than lowest sofar
                (curNode->next->priority == lowestNode->priority &&
                 curNode->next->value < lowestNode->value)) {
            lowestNode = curNode->next; // lowest will be the next
            linkerNode = curNode; // linker will  keep at the cur
        }
        curNode = curNode->next; // incrm cur
    }
    if(lowestNode == front) { // cant use linker ptr since nothing comes before front, special case
        ListNode* tmp = lowestNode->next;
        front = tmp;
        string s = lowestNode->value;
        delete lowestNode;
        return s;
    } else { // use linker, regular
        string value = lowestNode->value;
        linkerNode->next = lowestNode->next;
        delete lowestNode;
        return value;
    }
}

void USLinkedPriorityQueue::enqueue(string value, int priority) {
    ListNode* newNode = new ListNode(value, priority);
    newNode->next = front;
    front = newNode;
}

bool USLinkedPriorityQueue::isEmpty() const {
    return front == NULL;
}

string USLinkedPriorityQueue::peek() const {
    ListNode* curNode = front;
    if(curNode == NULL) {
        throw "Empty queue.";
    }
    int hiPriority = curNode->priority;
    string hiPriorityVal;
    while(curNode != NULL) {
        if(curNode->priority < hiPriority) {
            hiPriority = curNode->priority;
            hiPriorityVal = curNode->value;
        }
        curNode = curNode->next;
    }
    return hiPriorityVal;
}

int USLinkedPriorityQueue::peekPriority() const {
    ListNode* curNode = front;
    if(curNode == NULL) {
        throw "Empty queue.";
    }
    int hiPriority = curNode->priority;
    while(curNode != NULL) {
        if(curNode->priority < hiPriority) {
            hiPriority = curNode->priority;
        }
        curNode = curNode->next;
    }
    return hiPriority;
}

int USLinkedPriorityQueue::size() const {
    int counter = 0;
    ListNode* curNode = front;
    while(curNode != NULL) {
        counter++;
        curNode = curNode->next;
    }
    return counter;
}

ostream& operator<<(ostream& out, const USLinkedPriorityQueue& queue) {
    ListNode* curNode = queue.front;
    cout << "{";
    while(curNode != NULL) {
        cout << curNode->value <<
                ":" << curNode->priority;
        if(curNode->next != NULL) {
            cout << ", ";
        }
        curNode = curNode->next;
    }
    cout << "}" << endl;
    return out;
}
