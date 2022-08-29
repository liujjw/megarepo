#include "lexicon.h"
#include <iostream>
#include <string>
using namespace std;


bool isPalindrome(string str) {
    int n = str.length();
    for (int i = 0; i < n / 2; i++) {
        if (str[i] != str[n - i - 1]) return false;
    }
    return true;
}

int main(){
    Lexicon english("EnglishWords.txt");
    Vector<string> palindromes;
    for(string word : english){
        if(isPalindrome(word))
            palindromes.add(word);
    }
    cout << palindromes.size() << endl;
    return 0;
}
