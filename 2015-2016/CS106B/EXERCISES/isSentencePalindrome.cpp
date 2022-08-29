#include <string>
#include <iostream>
#include "console.h"
#include <cctype>
#include <cstdlib>
#include "strlib.h"
using namespace std;

bool isSentencePalindrome(string s){
    if(s.length() <= 1) return true;

    int count1 = 0;
    int count2 = 0;
    while(count1 <= s.length() - 1 - count2){
        char c1 = s.at(count1);
        while(!(isalpha(c1)))
            c1 = s.at(++count1);

        char c2 = s.at(s.length() - 1 - count2);
        while(!(isalpha(c2)))
            c2 = s.at(s.length() - 1 - (++count2));

        if(tolower(c1) != tolower(c2)) return false;

        count1++;
        count2++;
    }
    return true;
}


int main(){
    string s;
    cin >> ws >> s;
    if(isSentencePalindrome(s))
        cout << "\"" << s << "\"" << " is a sentence palindrome. " << endl;
    else
        cout << "NOPE" << endl;
    return 0;
}
