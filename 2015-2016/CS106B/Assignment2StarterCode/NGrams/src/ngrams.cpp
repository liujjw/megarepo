/*
 * N-Grams Random Text Generator
 * -----------------------------
 * Given a text file, analyzes the probability of N
 * word chains to generate new text that sounds like
 * the author of the text file.
 *
 * Higher N should mean more similar new text to the original.
*/

#include <cctype>
#include <cmath>
#include <fstream>
#include <iostream>
#include <string>
#include "console.h"
#include "filelib.h"
#include "tokenscanner.h"
#include "random.h"
#include "map.h"
#include "vector.h"
#include "simpio.h"

using namespace std;

void        generateText(Map< Vector<string>, Vector<string> > &wordMap, int numWords);
void        initWordsInWindow(TokenScanner &scanner, Vector<string> &window, int nWords);
void        slideWindow(string word, Vector<string> &window);
void        mapKeyValueFromWindow(string word, Map< Vector<string>, Vector<string> > &wordMap, Vector<string> &window);

int main() {
    cout << "Welcome to CS 106B Random Writer (\'N-Grams\')." << endl;
    cout << "This program makes random text based on a document." << endl;
    cout << "Give me an input file and an 'N' value for groups" << endl;
    cout << "of words, and I'll create random text for you." << endl << endl;

    ifstream infile;
    promptUserForFile(infile, "Input file: ");
    int N = getInteger("Value of N-Gram: ");
    while(N < 2){
        N = getInteger("N-gram must be 2 or greater: ");
    }
    cout << endl;

    Map< Vector<string>, Vector<string> > wordMap;
    Vector<string> window(N);

    TokenScanner scanner(infile);
    scanner.addWordCharacters("'.-");
    initWordsInWindow(scanner, window, N);
    Vector<string> wrapAroundWords = window;

    while(true){
        if(scanner.hasMoreTokens()){
            mapKeyValueFromWindow(scanner.nextToken(), wordMap, window);
        }else{
            for(string s : wrapAroundWords){
                mapKeyValueFromWindow(s, wordMap, window);
            }
            break;
        }
    }

    int numWords;
    while(true){
        numWords = getInteger("# of random words to generate (0 to quit): ");
        if(numWords == 0) break;
        while(numWords < N){
            numWords = getInteger("# of random words must at least be the size of the N-gram: ");
        }
        cout << "... ";
        generateText(wordMap, numWords);
        cout << " ..." << endl << endl;
    }

    cout << "Exiting." << endl;
    return 0;
}

void generateText(Map< Vector<string>, Vector<string> > &wordMap, int numWords){
    Vector< Vector<string> > keys = wordMap.keys();
    int randKeyIndex = randomInteger(0, keys.size() - 1);
    Vector<string> window = keys[randKeyIndex];
    for(string s : window) cout << s << " ";

    for(int i = 0; i < numWords - window.size(); i++){
        Vector<string> possibleNextWords =  wordMap.get(window);
        int randPossibleWordIndex = randomInteger(0, possibleNextWords.size() - 1);
        string nextWord = possibleNextWords[randPossibleWordIndex];
        slideWindow(nextWord, window);
        cout << window[window.size() - 1] << " ";
    }

}

void initWordsInWindow(TokenScanner &scanner, Vector<string> &window, int nWords){
    for(int i = 0; i < nWords; i++){
        if(scanner.hasMoreTokens()){
            string token = scanner.nextToken();
            if(isalnum(token[0])){
                window[i] = token;
            }else{
                --i;
            }
        }else{
            error("Insufficient words to form N-gram");
        }
    }
}

void slideWindow(string word, Vector<string> &window){
    Vector<string> tmp = window;
    for(int i = 0; i < window.size() - 1; i++){
        window[i] = tmp[i + 1];
    }
    window[window.size() - 1] = word;
}

void mapKeyValueFromWindow(string word, Map< Vector<string>, Vector<string> > &wordMap, Vector<string> &window){
    if(isalnum(word[0])){
        if(!wordMap.containsKey(window)){
            Vector<string> tmp;
            tmp.add(word);
            wordMap.put(window, tmp);
        }else{
            Vector<string> extendedSuffix = wordMap.get(window);
            extendedSuffix.add(word);
            wordMap.put(window, extendedSuffix);
        }
        slideWindow(word, window);
    }
}
