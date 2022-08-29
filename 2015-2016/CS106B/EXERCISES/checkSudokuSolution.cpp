#include <iostream>
#include <fstream>
#include <sstream>
#include "console.h"
#include "vector.h"
#include "grid.h"
#include <cstdlib>
#include "simpio.h"
#include "filelib.h"
#include "strlib.h"
#include "set.h"
using namespace std;

bool checkSudokuSolution(Grid<int> & puzzle){
    if(puzzle.numRows() != 9 || puzzle.numCols() != 9){
        error("not sudoku");
    }

    // vertical
    Set<int> digits;
    for(int i = 0; i < 9; i++){
        digits.clear();
        for(int itr = 0; itr < 9; itr++){
            int number = puzzle[itr][i]; // *
            if(digits.contains(number)){
                return false;
            }
            digits.add(number);
        }
    }

    // horizontals
    digits.clear();
    for(int i = 0; i < 9; i++){
        digits.clear();
        for(int itr = 0; itr < 9; itr++){
            int number = puzzle[i][itr]; // *
            if(digits.contains(number)){
                return false;
            }
            digits.add(number);
        }
    }

    // 3*3
    digits.clear();
    for(int l = 0; l < 9; l += 3){
        for(int j = 0; j < 9; j += 3){
            digits.clear();
            for(int i = 0; i < 3; i++){
                for(int k = 0; k < 3; k++){
                    int number = puzzle[i + l][k + j]; // *
                    if(digits.contains(number)){
                        return false;
                    }
                }
            }
        }
    }

    return true;
}

int main(){
    Grid<int> puzzle(9,9);

    ifstream infile;
    promptUserForFile(infile, "Sudoku solution file: ");

    string number;
    Vector<int> buffer;
    while(getline(infile, number)){
        buffer.add(stringToInteger(number));
    }
    infile.close();

    int i = 0, k = 0;
    for(int number : buffer){
        if(k == 9){
            i++;
            k = 0;
        }
        puzzle[i][k] = number;
        k++;
    }

    if(checkSudokuSolution(puzzle))
        cout << "yep" << endl;

    return 0;
}
