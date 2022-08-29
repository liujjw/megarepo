#include <string>
#include <iostream>
#include <cctype>
#include "console.h"
#include "simpio.h"
using namespace std;


string substr(string str, int pos, int n);
string substr(string str, int pos);

int main(){
    string str = "hotdog";
        cout << substr(str, 1)<< endl;
    return 0;
}

string substr(string str, int pos, int n){
    if(pos > str.length() - 1) exit("EXIT_FAILURE");
    string substring = "";
    for(int i = pos; i < n + pos; i++){
        if(i > str.length() - 1) break;
        substring += str[i];
    }
    return substring;
}

string substr(string str, int pos){
    if(pos > str.length() - 1) exit("EXIT_FAILURE");
    string substring = "";
    for(int i = pos; i < str.length(); i++){
        if(i > str.length() - 1) break;
        substring += str[i];
    }
    return substring;
}
