#include "SLinkedPriorityQueue.h"

SLinkedPriorityQueue::SLinkedPriorityQueue() {
    front = NULL;
}

SLinkedPriorityQueue::~SLinkedPriorityQueue() {
    while(front != NULL) {
        ListNode* nextNode = front->next;
        delete front;
        front = nextNode;
    }
}

void SLinkedPriorityQueue::changePriority(string value, int newPriority) {
    ListNode* curNode = front;
    if(curNode == NULL) {
        throw "Empty queue";
    }
    if(curNode->value == value) {
        curNode->priority = newPriority;
    } else {
        while(curNode->next != NULL) {
            if(curNode->next->value == value) {
                break;
            }
            curNode = curNode->next;
        }
        if(curNode->next->value != value) {
            throw "String value was not found.";
        }
        ListNode* tmp = curNode->next->next;
        delete curNode->next;
        curNode->next = tmp;
        SLinkedPriorityQueue::enqueue(value, newPriority);
    }
}

void SLinkedPriorityQueue::clear() {
    while(front != NULL) {
        ListNode* nextNode = front->next;
        delete front;
        front = nextNode;
    }
    front = NULL;
}

string SLinkedPriorityQueue::dequeue() {
    if(front != NULL) {
        string myValue = front->value;
        ListNode* tmp = front->next;
        delete front;
        front = tmp;
        return myValue;
    } else {
        throw "Empty queue.";
    }
}

void SLinkedPriorityQueue::enqueue(string value, int priority) {
    if(front == NULL) {
        front = new ListNode(value, priority, NULL);
    } else {
        ListNode* newNode = new ListNode(value, priority);
        if(front->priority > newNode->priority ||
                (front->priority == newNode->priority &&
                 newNode->value <= front->value)) {
            newNode->next = front;
            front = newNode;
        } else {
            ListNode* pointerToNew = front;
            while(true) {
                if(pointerToNew->next == NULL ||
                        pointerToNew->next->priority > priority ||
                        (pointerToNew->next->priority == newNode->priority &&
                         newNode->value <= pointerToNew->next->value)) {
                    break;
                } else {
                    pointerToNew = pointerToNew->next;
                }
            }
            newNode->next = pointerToNew->next;
            pointerToNew->next = newNode;
        }
    }

}

bool SLinkedPriorityQueue::isEmpty() const {
    return front == NULL;
}

string SLinkedPriorityQueue::peek() const {
    if(front != NULL) {
        return front->value;
    } else {
        throw "Empty queue.";
    }
}

int SLinkedPriorityQueue::peekPriority() const {
    if(front != NULL) {
        return front->priority;
    } else {
        throw "Empty queue.";
    }
}

int SLinkedPriorityQueue::size() const {
    int counter = 0;
    ListNode* curNode = front;
    while(curNode != NULL) {
        counter++;
        curNode = curNode->next;
    }
    return counter;
}

ostream& operator<<(ostream& out, const SLinkedPriorityQueue& queue) {
    ListNode* curNode = queue.front;
    cout << "{";
    while(curNode != NULL) {
        cout << *curNode;
        if(curNode->next != NULL) {
            cout << ", ";
        }
        curNode = curNode->next;
    }
    cout << "}" << endl;
    return out;
}
