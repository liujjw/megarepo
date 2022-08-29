#include <string>
#include <iostream>
#include "console.h"
#include "simpio.h"
using namespace std;


bool endsWith(string str, string suffix){
    if(str.length() == 0 || suffix.length() == 0) return true;

    int suffixCounter = 0;
    for(int i = int(str.length() - suffix.length()); i < int(str.length()); i++){
        if(str.at(i) != suffix.at(suffixCounter++)) return false;
    }
    return true;
}

bool endsWith(string str, char suffix){
    string charAsString = string("") + suffix;
    if(endsWith(str, charAsString))
        return true;
    else
        return false;
}


int main(){
    char suffix = 'g';
    string str = "hotdog";
    if(endsWith(str, suffix))
        cout << "yes" << endl;
    return 0;
}
