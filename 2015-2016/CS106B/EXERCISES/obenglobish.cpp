#include <string>
#include <iostream>
#include "console.h"
#include <cctype>
#include "strlib.h"
using namespace std;

bool isVowel(char c){
   c = toupper(c);
   switch(c){
   case 'A':case'E':case'I':case'O':case'U':
       return true;
   default:
       return false;
   }
}

string obenglobish(string s){
    string myString = s;
    bool wasVowel = false;
    for(int i = 0; i < myString.length() - 1; i++){ /*disregards last vowel usually silent*/
        if(isVowel(myString.at(i)) && !(wasVowel)){
            string start = myString.substr(0, i);
            string end = myString.substr(i);
            string append = "ob";
            myString = start + append + end;
            wasVowel = true;
            i += 2;
        }else{
            wasVowel = false;
        }
    }
    return myString;
}

int main(){
    cout << "This is Obenglobish. Enter text: " << endl;
    string s = "rot";
    //getline(cin, s);
    cout << obenglobish(s) << endl;
    return 0;
}
