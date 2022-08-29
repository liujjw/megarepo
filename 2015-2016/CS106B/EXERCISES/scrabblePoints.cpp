#include <string>
#include <iostream>
#include "console.h"
#include <cctype>
#include <cstdlib>
using namespace std;

int score(char letter){
    switch(letter){
        case 'A':case 'E':case 'I':case 'O':
        case 'U':case 'L':case 'N':case 'R':
        case 'S':case 'T': return 1;

        case 'D':case 'G': return 2;

        case 'B':case 'C':case 'M':case 'P': return 3;

        case 'F':case 'H':case 'V':case 'Y':
        case 'W': return 4;

        case 'K': return 5;

        case 'J':case 'X': return 8;

        case 'Q':case 'Z': return 10;

        default: return -100;
    }
}

int points(string s){
    int total = 0;
    for(int i = 0; i < s.length(); i++){
        if(isupper(s.at(i)))
            total += score(s.at(i));
    }
    return total;
}

int main(){
    string s = "FARM";
    cout << points(s) << endl;
    return 0;
}
