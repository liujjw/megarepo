#include <string>
#include <iostream>
#include "console.h"
#include <cctype>
#include "strlib.h"
#include "simpio.h"
using namespace std;

string encodeCaesarCipher(string s, int shift){
    string myString;
    for(int i = 0; i < s.length(); i++){
        char toEncode = s.at(i);
        if(isalpha(s.at(i))){
            if(shift < 0){
                myString += 'A' +
                        ((26 - ((-shift) % 26)) + (toupper(toEncode) - 'A')) % 26;
            }else{
                myString += 'A' + ((toupper(toEncode) - 'A' + (shift % 26)) % 26);
            }
        }else{
            myString += toEncode;
        }
    }
    return myString;
}

int main(){
    string s = "This is a secret message.";
    int shift = getInteger(": ");
    cout << encodeCaesarCipher(s, shift) << endl;
    return 0;
}
