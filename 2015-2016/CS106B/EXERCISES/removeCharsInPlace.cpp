#include <string>
#include <iostream>
#include "console.h"
#include <cctype>
#include <cstdlib>
#include "strlib.h"
using namespace std;

bool matches(char letter, string remove){
    for(int i = 0; i < remove.length(); i++){
        if(letter == remove.at(i))
            return true;
    }
    return false;
}

string removeCharacters(string str, string remove){
    string myString;
    for(int i = 0; i < str.length(); i++){
        if(!matches(str.at(i), remove))
            myString += str.at(i);
    }
    return myString;
}

void removeCharsInPlace(string & s, string remove){
    s = removeCharacters(*s, remove);
}

int main(){
    string s = "Acquired Immuno-Deficiency Syndrome  ";
    removeCharsInPlace(s, "aeiou");
    cout << s << endl;
    return 0;
}
