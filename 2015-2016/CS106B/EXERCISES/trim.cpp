#include <string>
#include <iostream>
#include <cctype>
#include "console.h"
#include "simpio.h"
using namespace std;


string trim(string str){
    string newString = str;

    while(isspace(newString.at(0))){
        newString = newString.substr(1);
    }

    while(isspace(newString.at(newString.length() - 1))){
        newString = newString.substr(0, newString.length() - 1);
    }
    return newString;
}

int main(){
    string str = "                           hot dog       ";
        cout << trim(str)<< endl;
    return 0;
}
