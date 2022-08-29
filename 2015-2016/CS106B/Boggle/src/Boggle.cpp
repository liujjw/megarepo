/*
 * Boggle Class, CS106B JLiu
 * --------------------------
 * Maintains the Boggle board entity
 * that BogglePlay interacts with.
*/

#include "Boggle.h"
#include "bogglegui.h"

// letters on every cube in 5x5 "Big Boggle" version (extension)
static string BIG_BOGGLE_CUBES[25] = {
   "AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM",
   "AEEGMU", "AEGMNN", "AFIRSY", "BJKQXZ", "CCNSTW",
   "CEIILT", "CEILPT", "CEIPST", "DDLNOR", "DDHNOT",
   "DHHLOR", "DHLNOR", "EIIITT", "EMOTTT", "ENSSSU",
   "FIPRSY", "GORRVW", "HIPRRY", "NOOTUW", "OOOTTU"
};

// private method for constructor
Vector<string> Boggle::makeVectorOfCubes() {
    Vector<string> vec;
    vec.add("AAEEGN"); vec.add("ABBJOO"); vec.add("ACHOPS");
    vec.add("AFFKPS"); vec.add("AOOTTW"); vec.add("CIMOTU");
    vec.add("DEILRX"); vec.add("DELRVY"); vec.add("DISTTY");
    vec.add("EEGHNW"); vec.add("EEINSU"); vec.add("EHRTVW");
    vec.add("EIOSST"); vec.add("ELRTTY"); vec.add("HIMNQU");
    vec.add("HLNNRZ");
    return vec;
}

Boggle::Boggle(Lexicon& dictionary, string boardText) {
    Boggle::englishDict = dictionary;
    Boggle::boggleBoard.resize(Boggle::N_ROWS, Boggle::N_COLS);
    Boggle::humanPoints = 0;
    Boggle::computerPoints = 0;

    if(boardText == "") { // random
        Vector<string> cubesVector = makeVectorOfCubes();
        shuffle(cubesVector);
        for(int i = 0; i < cubesVector.size(); i++){
            string possLetters = cubesVector[i];
            char letter = possLetters[randomInteger(0, possLetters.size() - 1)];
            Boggle::boggleBoard[i / Boggle::N_ROWS][i % Boggle::N_COLS] = letter;
        }
    } else { // manual
        boardText = toUpperCase(boardText);
        int wordIndexor = 0;
        for(int i = 0; i < Boggle::N_ROWS; i++) {
            for(int k = 0; k < Boggle::N_COLS; k++) {
                Boggle::boggleBoard[i][k] = boardText[wordIndexor++];
            }
        }
    }

}

char Boggle::getLetter(int row, int col) {
    if(!Boggle::boggleBoard.inBounds(row, col)) {
        throw "Out of bounds at getLetter.";
    }
    char c = Boggle::boggleBoard[row][col];
    return c;
}

bool Boggle::checkWord(string word) {
    if(!(Boggle::englishDict.contains(word))
            || word.size() < Boggle::N_ROWS || Boggle::humanWords.contains(word)) {
        return false;
    }
    return true;
}

// use a word length that decrements not a word so far
// use vector for marked words and not extra set
// maybe for search modify existing board
bool Boggle::humanWordSearch(string word) {
    // GUI ANIMATION
    for(int i = 0; i < Boggle::boggleBoard.numRows(); i++) {
        for(int k = 0; k < Boggle::boggleBoard.numCols(); k++) {
            BoggleGUI::setHighlighted(i, k, false);
        }
    }
    BoggleGUI::setAnimationDelay(100);

    word = toUpperCase(word);
    Set<int> visitedCells;
    string wordSoFar = "";

    for(int startingRow = 0; startingRow < Boggle::boggleBoard.numRows(); startingRow++) {
        for(int startingCol = 0; startingCol < Boggle::boggleBoard.numCols(); startingCol++) {
            visitedCells.clear();

            if(word[0] == Boggle::boggleBoard[startingRow][startingCol]) {
                if(humanWordSearchHelper(word, visitedCells, startingRow,
                                         startingCol, wordSoFar)) {

                    if(!(Boggle::humanWords.contains(word))) {
                        Boggle::humanWords.add(word);
                        return true;
                    }
                }
            }
        }
    }
    return false;
}

bool Boggle::humanWordSearchHelper(string word, Set<int>& visitedCells, int startingRow,
                                   int startingCol, string wordSoFar) {
    Set<int> myRecentCells;
    if(wordSoFar == word) {
        return true;
    }

    for(int i = -1; i < 2; i++) {
        for(int k = -1; k < 2; k++) {

            int adjacentRow = startingRow + i;
            int adjacentCol = startingCol + k;

            if(Boggle::boggleBoard.inBounds(adjacentRow, adjacentCol)
                    && Boggle::wordContainsPrefix(word, wordSoFar)) {

                int cellMarker = adjacentCol + (adjacentRow * Boggle::N_ROWS);

                if(!visitedCells.contains(cellMarker)) {

                    myRecentCells.add(cellMarker);
                    visitedCells.add(cellMarker);
                    wordSoFar += Boggle::boggleBoard[adjacentRow][adjacentCol];
                    BoggleGUI::setHighlighted(adjacentRow, adjacentCol, true);
                    if(humanWordSearchHelper(word, visitedCells, adjacentRow,
                                                       adjacentCol, wordSoFar)) {
                        return true;
                    }
                    wordSoFar.erase(wordSoFar.size() - 1);
                    BoggleGUI::setHighlighted(adjacentRow, adjacentCol, false);
                    visitedCells -= myRecentCells;
                }
            }
        }
    }
    return false;
}

bool Boggle::wordContainsPrefix(string word, string prefix) {
    if(prefix.size() > word.size()) {
        return false;
    }
    for(int i = 0; i < prefix.size(); i++) {
        if(prefix[i] != word[i]) {
            return false;
        }
    }
    return true;
}

int Boggle::getScoreHuman() {
    return Boggle::humanPoints;
}

Set<string> Boggle::computerWordSearch() {
    Set<int> visitedCells;
    string wordSoFar = "";
    Set<string> computerWords;

    for(int startingRow = 0; startingRow < Boggle::boggleBoard.numRows(); startingRow++) {
        for(int startingCol = 0; startingCol < Boggle::boggleBoard.numCols(); startingCol++) {
            visitedCells.clear();
            computerWordSearchHelper(computerWords, visitedCells, startingRow,
                                                 startingCol, wordSoFar);
        }
    }

    return computerWords;
}

void Boggle::computerWordSearchHelper(Set<string>& computerWords, Set<int>& visitedCells, int startingRow,
                                   int startingCol, string wordSoFar) {
    Set<int> myRecentCells;
    if(Boggle::checkWord(wordSoFar)) {
        computerWords.add(wordSoFar);
    }
    for(int i = -1; i < 2; i++) {
        for(int k = -1; k < 2; k++) {
            int adjacentRow = startingRow + i;
            int adjacentCol = startingCol + k;
            if(Boggle::boggleBoard.inBounds(adjacentRow, adjacentCol)
                    && Boggle::englishDict.containsPrefix(wordSoFar)) {
                int cellMarker = adjacentCol + (adjacentRow * Boggle::N_ROWS);
                if(!visitedCells.contains(cellMarker)) {
                    myRecentCells.add(cellMarker);
                    visitedCells.add(cellMarker);
                    wordSoFar += Boggle::boggleBoard[adjacentRow][adjacentCol];
                    computerWordSearchHelper(computerWords, visitedCells, adjacentRow,
                                                       adjacentCol, wordSoFar);
                    wordSoFar.erase(wordSoFar.size() - 1);
                    visitedCells -= myRecentCells;
                }
            }
        }
    }
}

int Boggle::getScoreComputer() {
    return Boggle::computerPoints;
}

ostream& operator<<(ostream& out, Boggle& boggle) {
    Grid<char> board = boggle.boggleBoard;
    for(int i = 0; i < board.numRows(); i++) {
        for(int k = 0; k < board.numCols(); k++) {
            cout << board[i][k] << " ";
        }
        cout << endl;
    }
    return out;
}

void Boggle::setComputerScore(Set<string> setOfWords) {
    for(string word : setOfWords) {
        if(word.size() < Boggle::N_ROWS) {
            throw "Unpexpected word length is less than 4.";
        }
        Boggle::computerPoints +=((word.size() - Boggle::N_ROWS) + 1);
    }
}

void Boggle::updateHumanScore(string word){
    if(word.size() < Boggle::N_ROWS) {
        throw "Unexpected word length is less than 4.";
    }
    Boggle::humanPoints += ((word.size() - Boggle::N_ROWS) + 1);
}

Set<string> Boggle::getHumanWords() {
    return Boggle::humanWords;
}

int Boggle::getBoardDimensions() {
    return Boggle::N_ROWS;
}

