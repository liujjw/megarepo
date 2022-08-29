#include <iostream>  // This is a true classic.
#include "console.h" // This is a Stanford Library!
#include "simpio.h"  // Another great library.
using namespace std; // The best namespace.

int main() {

    cout << "My name is Eliza" << endl;
    cout << "I am an artificial intelligent psychologist." << endl;
    cout << "Enter  a prompt and I will respond" << endl;

    while(true) {
        string prompt = getLine("> ");
        int promptLength = prompt.length();

        if(prompt == "hello") {
            cout << "hi!" << endl;
        } else if(prompt[promptLength - 1] == '?') {
            cout << "why do you ask " + " " << endl;
        } else {
            cout << "are you really sure?" << endl;
        }
    }


    // your code here...

    return 0;
}

