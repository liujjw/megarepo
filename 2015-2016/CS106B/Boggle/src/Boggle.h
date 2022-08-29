/*
 * Boggle Header, CS106B JLiu
 * ----------------------------
 * Header for Boggle Class.
*/

#ifndef _boggle_h
#define _boggle_h

#include <iostream>
#include <string>
#include "lexicon.h"
#include "grid.h"
#include "vector.h"
#include "shuffle.h"
#include "random.h"
#include "strlib.h"
#include "bogglegui.h"
using namespace std;

class Boggle {
public:
    Boggle(Lexicon& dictionary, string boardText = "");

    char        getLetter(int row, int col);
    bool        checkWord(string word);
    bool        humanWordSearch(string word);
    Set<string> computerWordSearch();
    int         getScoreHuman();
    int         getScoreComputer();

    void        setComputerScore(Set<string> setOfWords);
    void        updateHumanScore(string word);
    Grid<char>  getBoggleBoard();
    Set<string> getHumanWords();

    int         getBoardDimensions();

    friend ostream& operator<<(ostream& out, Boggle& boggle);

private:
    Vector<string>  makeVectorOfCubes();
    bool            humanWordSearchHelper(string word, Set<int>& visitedCells, int startingRow,
                               int startingCol, string wordSoFar);
    void            computerWordSearchHelper(Set<string>& computerWords, Set<int>& visitedCells,
                                             int startingRow, int startingCol, string wordSoFar);
    bool            wordContainsPrefix(string word, string prefix);

    Grid<char>      boggleBoard;
    Set<string>     humanWords;

    Lexicon         englishDict;

    int             humanPoints;
    int             computerPoints;

    const static int N_ROWS = 4;
    const static int N_COLS = 4;
};

#endif // _boggle_h
