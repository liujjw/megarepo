/*
* Word Ladder Program
* --------------------
* A word ladder is a connection from one word to another
* formed by changing one letter at a time with the constraint
* that at each step the sequence of letters still forms a valid word.
*
* This program finds the shortest of a set of two words' word ladder.
* For example, "code" and "data" has the word ladder:
* code -> cade -> cate -> date -> data
*
*/

#include <cctype>
#include <cmath>
#include <fstream>
#include <iostream>
#include <string>
#include "console.h"
#include "simpio.h"
#include "strlib.h"
#include "set.h"
#include "queue.h"
#include "stack.h"
using namespace std;

string      findNextNeighbor(string word, Set<string> &usedNeighborSet, Set<string> &dictionary);

int main() {
    cout << "This is CS106B Word Ladder." << endl <<
            "Enter two words, and, by changing one letter at a time," << endl <<
            "Word Ladder will change the first word into the second word." << endl <<
            "(Press enter to quit)" << endl << endl;

    ifstream infile;
    while(true) {
        string dictName = getLine("Enter dictionary filename: ");
        infile.open(dictName.c_str());
        if(!infile.fail()) {
            break;
        }else{
            cout << "Failed. Retry." << endl;
            infile.clear();
        }
    }

    Set<string> dictionary;
    string word;
    while(getline(infile, word)) {
        dictionary.add(word);
    }
    if(dictionary.isEmpty()) {
        error("Dictionary was empty.");
    }


    while(true) {
        bool quitFlag = false;

        string word1;
        string word2;
        while(true) {
            bool wordsAcceptedFlag = false;
            bool duplicateFlag = false;
            bool diffLengthFlag = false;

            word1 = getLine("Word #1: ");
            if(word1 != "") {
                word1 = toLowerCase(word1);
                word2 = getLine("Word #2: ");

                if(word2 != "") {
                    word2 = toLowerCase(word2);

                    if(word1 == word2) {
                        duplicateFlag = true;
                    } else if(word1.length() != word2.length()){
                        diffLengthFlag = true;
                    } else if (dictionary.contains(word1) && dictionary.contains(word2)) {
                        wordsAcceptedFlag = true;
                    }

                }else {
                    quitFlag = true;
                    break;
                }
            }else {
                quitFlag = true;
                break;
            }

            if(wordsAcceptedFlag) {
                break;
            } else if (duplicateFlag) {
                cout << "Words must be different." << endl << endl;
            } else if(diffLengthFlag){
                cout << "Words must be same length." << endl << endl;
            } else {
                cout << "Words must be in the dictionary." << endl << endl;
            }
        }
        if(quitFlag) {
            break;
        }

        Set<string> usedNeighborSet;
        Queue< Stack<string> > partialLadders;

        Stack<string> root;
        root.push(word1);
        partialLadders.enqueue(root);

        bool foundFlag = false;

        while(!partialLadders.isEmpty()){
            Stack<string> pathStack = partialLadders.dequeue();
            string neighbor = "";
            string curWord = pathStack.peek();
            while(true){
                neighbor = findNextNeighbor(curWord, usedNeighborSet, dictionary);
                if(neighbor == curWord){
                    break;
                }else if(neighbor == word2){
                    pathStack.push(neighbor);
                    cout << "Word ladder is: " << endl;
                    while(!pathStack.isEmpty()){
                        cout << pathStack.pop() << "   ";
                    }
                    cout << endl << endl;
                    foundFlag = true;
                    break;
                }else{
                    Stack<string> copy = pathStack;
                    copy.push(neighbor);
                    partialLadders.enqueue(copy);
                }
            }
            if(foundFlag){
                break;
            }
        }

        if(!foundFlag){
            cout << "No word ladder found." << endl << endl;
        }
    }

    cout << endl << "Exit." << endl;
    return 0;
}

string findNextNeighbor(string word, Set<string> &usedNeighborSet, Set<string> &dictionary){
    for(int index = 0; index < word.length(); index++) {

        for(int alpha = 0; alpha < 26; alpha++) {

            string possibleNeighbor = word;
            possibleNeighbor[index] = char(alpha + 'a');

            if(dictionary.contains(possibleNeighbor) && !(usedNeighborSet.contains(possibleNeighbor)) &&
                    possibleNeighbor != word) {

                usedNeighborSet.add(possibleNeighbor);
                return possibleNeighbor;
            }
        }
    }
    return word;
}


