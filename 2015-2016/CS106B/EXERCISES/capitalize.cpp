#include <string>
#include <iostream>
#include "console.h"
#include <cctype>
#include <cstdlib>
using namespace std;

string capitalize(string str){
    string myString = "";

    int start = 0;
    while(!(isalpha(str.at(start)))){
        if(start >= int(str.length()) - 1) exit(EXIT_FAILURE);
        start++;
    }
    int end = start;
    while(!(isspace(str.at(end)))){
        if(end >= int(str.length()) - 1) break;
        end++;
    }

    for(int i = start; i <= end; i++){
        myString += tolower(str.at(i));
    }
    myString.at(0) = toupper(myString.at(0));

    return myString;
}

int main(){
    string s = "a";
    cout << capitalize(s) << endl;
    return 0;
}
