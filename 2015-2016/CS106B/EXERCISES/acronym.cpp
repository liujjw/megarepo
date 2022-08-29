#include <string>
#include <iostream>
#include "console.h"
#include <cctype>
#include <cstdlib>
#include "strlib.h"
using namespace std;

string acronym(string str){
    str = trim(str);
    string myString = "";
    bool inAcronym = true;
    for(int i = 0; i < str.length(); i++){
        if(inAcronym){
            myString += toupper(str.at(i));
            inAcronym = false;
        }
        if(!isalpha(str.at(i))){
            inAcronym = true;
        }
    }
    return myString;
}

int main(){
    string s = "Acquired Immuno-Deficiency Syndrome  ";
    cout << acronym(s) << endl;
    return 0;
}
