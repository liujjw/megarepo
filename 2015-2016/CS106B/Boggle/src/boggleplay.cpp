/*
 * BogglePlay CS106B JLiu
 * ------------------------------------
 * Plays one game of Boggle between a human and the computer.
*/

#include <cctype>
#include <iostream>
#include <string>
#include "console.h"
#include "lexicon.h"
#include "random.h"
#include "simpio.h"
#include "strlib.h"
#include "bogglegui.h"
#include "Boggle.h"
#include "bogglegui.h"
using namespace std;

bool    isValidForBoardString(const string s);

void playOneGame(Lexicon& dictionary) {
    /*Boggle board intialization*/
    if(BoggleGUI::isInitialized()) {
        BoggleGUI::reset();
    }
    string boardStringInit = "";
    if(!getYesOrNo("Generate a random board (Y/N)?")) {
        while(true) {
            boardStringInit = getLine("Manual board string(16 consecutive letters): ");
            if(isValidForBoardString(boardStringInit)) {
                break;
            } else {
                cout << "Invalid input." << endl;
            }
        }
    }
    Boggle boggleBoardObject(dictionary, boardStringInit);

    // makes a 4x4 board in gui
    int boardSize = boggleBoardObject.getBoardDimensions();
    BoggleGUI::initialize(boardSize, boardSize);
    for(int i = 0; i < boardSize; i++) {
        for(int k = 0; k < boardSize; k++) {
            BoggleGUI::labelCube(i, k, boggleBoardObject.getLetter(i, k));
        }
    }

    clearConsole();
    cout << "It's your turn!" << endl;
    BoggleGUI::setStatusMessage("It's your turn!");

    /*User's turn*/
    while(true) {
        cout << boggleBoardObject << endl << endl;

        cout << "Your words(" << boggleBoardObject.getHumanWords().size() << ")"
             << ": " << boggleBoardObject.getHumanWords() << endl;
        cout << "Your score: " << boggleBoardObject.getScoreHuman() << endl;

        string word = getLine("Type a word (or Enter to stop): ");
        clearConsole();
        if(word == "") {
            break;
        }
        if(boggleBoardObject.checkWord(word) && boggleBoardObject.humanWordSearch(word)) {
            boggleBoardObject.updateHumanScore(word);
            cout << "You found a new word!  \"" << word << "\"" << endl;
            BoggleGUI::setStatusMessage("You found a new word!");
            BoggleGUI::recordWord(word, BoggleGUI::HUMAN);
            BoggleGUI::setScore(boggleBoardObject.getScoreHuman(), BoggleGUI::HUMAN);
        } else {
            cout << word << " :You must enter an unfound 4+ letter word from the dictionary."<< endl;
            BoggleGUI::setStatusMessage("Invalid word!");
        }
    }

    /*Computer's turn*/
    cout << "It's my turn!" << endl;
    BoggleGUI::setStatusMessage("Computer's turn!");

    Set<string> computerWords = boggleBoardObject.computerWordSearch();
    for(string s : computerWords) {
        BoggleGUI::recordWord(s, BoggleGUI::COMPUTER);
    }

    boggleBoardObject.setComputerScore(computerWords);
    BoggleGUI::setScore(boggleBoardObject.getScoreComputer(), BoggleGUI::COMPUTER);

    cout << boggleBoardObject << endl;
    cout << "My words(" << computerWords.size() << ")"
         << ": " << computerWords << endl;
    cout << "My score: " << boggleBoardObject.getScoreComputer() << endl;

    /*Decide who wins*/
    int computerScore = boggleBoardObject.getScoreComputer();
    int humanScore = boggleBoardObject.getScoreHuman();

    if(computerScore > humanScore) {
        cout << "I WIN lmao ofc u had no chance" << endl;
        BoggleGUI::setStatusMessage("Computer WINS");
    } else {
        cout << "You win. For now [|_|]" << endl;
        BoggleGUI::setStatusMessage("Human WINS");
    }
}

bool isValidForBoardString(const string s) {
    if(s.size() != 16) {
        return false;
    }
    for(int i = 0; i < s.size(); i++) {
        if(!isalpha(s[i])) {
            return false;
        }
    }
    return true;
}

