#include <string>
#include <iostream>
#include "console.h"
#include <cctype>
#include "strlib.h"
using namespace std;

string capitalize(string s){
   string myString = s;
   char head = toupper(myString.at(0));
   string tail = myString.substr(1);
   myString = head + tail;
   return myString;
}

string decapitalize(string s){
    string myString = s;
    char head = tolower(myString.at(0));
    string tail = myString.substr(1);
    myString = head + tail;
    return myString;
}

bool isVowel(char c){
   c = toupper(c);
   switch(c){
   case 'A':case'E':case'I':case'O':case'U':
       return true;
   default:
       return false;
   }
}

int findFirstVowel(string s){
   for(int i = 0; i < s.length(); i++){
       if(isVowel(s.at(i))) return i;
   }
   return -1;
}

string wordToPigLatin(string word){
   int vp = findFirstVowel(word);
   if(vp == -1){
       return word;
   } else if (vp == 0){
       return word + "way";
   } else{
       string head = word.substr(0, vp);
       head = decapitalize(head);
       string tail = word.substr(vp);
       tail = capitalize(tail);
       return tail + head + "ay";
   }
}
string lineToPigLatin(string s){
   string result;
   int start = -1;
   for(int i = 0; i < s.length(); i++){
       char ch = s[i];
       if(isalpha(ch)){
           if (start == -1) start = i;
       } else {
           if(start >= 0){
               result += wordToPigLatin(s.substr(start, i - start));
               start = -1;
           }
           result += ch;
       }
   }
   if(start >= 0) result += wordToPigLatin(s.substr(start));
   return result;
}


int main(){
   cout << "This is Pig Latin. Enter text: " << endl;
   string s;
   getline(cin, s);
   cout << lineToPigLatin(s) << endl;
   return 0;
}
