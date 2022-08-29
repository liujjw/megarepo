#include <string>
#include <iostream>
#include "console.h"
#include <cctype>
#include <cstdlib>
#include "strlib.h"
using namespace std;

string removeDoubledLetters(string teemo){
    if(teemo.length() < 3) return teemo;

    string myTeemo;
    for(int i = 1; i < teemo.length(); i++){
        if(!(teemo.at(i - 1) == teemo.at(i))){
            myTeemo += teemo.at(i - 1);
        }
    }
    myTeemo += teemo.at(teemo.length() - 1);
    return myTeemo;
}

int main(){
    string s = "commitee";
    cout << removeDoubledLetters(s) << endl;
    return 0;
}
