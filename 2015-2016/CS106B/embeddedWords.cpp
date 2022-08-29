#include <iostream>
#include "set.h"
#include "console.h"
#include "lexicon.h"
using namespace std;

void embeddedWords(string word, Lexicon& dict, string wordSoFar) {
    if(dict.contains(wordSoFar)) {
        cout << wordSoFar << endl;
    } else if(!(wordSoFar.length() > word.length()) && dict.containsPrefix(wordSoFar)) {
        for(int i = 0; i < word.size(); i++) {
            wordSoFar += word[i];
            embeddedWords(word, dict, wordSoFar);
            wordSoFar.erase(wordSoFar.size() - 1);
        }
    }
}

int main() {
    Lexicon dict;
    dict.add("a");
    dict.add("hay");
    dict.add("happy");
    embeddedWords("happy", dict, "");
    return 0;
}
