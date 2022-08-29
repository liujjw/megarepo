#include <string>
#include <iostream>
#include "console.h"
#include <cctype>
#include <cstdlib>
#include "strlib.h"
using namespace std;

string replaceAll(string str, char c1, char c2){
    string myString;
    for(int i = 0; i < str.length(); i++){
        if(str.at(i) == c1)
            myString += c2;
        else
            myString += str.at(i);
    }
    return myString;
}

string replaceAll(string str, string s1, string s2){
    
    string myString = "";
    int found = str.find(s1, 0);
    while(found != string::npos){
        string start = str.substr(0, found);
        string end = str.substr(found + s1.length());
        myString = start + s2 + end;
        found = str.find(s1, found + s1.length());
    }
    if(myString != "") return myString;
    return str;
}

int main(){
    string s = "deadpool";
    cout << replaceAll(s, "oo", "ee") << endl;
    return 0;
}
