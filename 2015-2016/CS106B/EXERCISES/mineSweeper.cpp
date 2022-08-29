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
#include "iomanip.h"
using namespace std;

int minesInHood(Grid<bool> & mines, int row, int col){
    int numMines = 0;
    for(int i = -1; i < 2; i++){
        for(int j = -1; j < 2; j++){
            int rowIndex = row + i;
            int colIndex = col + j;
            if(!(rowIndex < 0 || rowIndex >= mines.numRows()
                || colIndex < 0 || colIndex >= mines.numCols())){
                if(mines[rowIndex][colIndex])
                    numMines++;
            }
        }
    }
    return numMines;
}

void fixCounts(Grid<bool> & mines, Grid<int> & counts){
    counts.resize(mines.numRows(), mines.numCols());
    for(int i = 0; i < counts.numRows(); i++){
        for(int j = 0; j < counts.numCols(); j++){
            counts[i][j] = minesInHood(mines, i, j);
        }
    }
}

int main(){
    Grid<bool> mines(6,6);

    ifstream infile;
    promptUserForFile(infile, "Grid file: ");

    string boolean;
    Vector<bool> buffer;
    while(getline(infile, boolean)){
        int number = stringToInteger(boolean);
        if(number == 0)
            buffer.add(true);
        else
            buffer.add(false);
    }
    infile.close();

    int i = 0, k = 0;
    for(bool isMine : buffer){
        if(k == 6){
            i++;
            k = 0;
        }
        mines[i][k] = isMine;
        k++;
    }

    Grid<int> counts;
    fixCounts(mines, counts);

    int j = 0;
    for(int count : counts){
        if (j == 6){
            cout << endl;
            j = 0;
        }
        cout << right << setw(2) << count;
        j++;
    }

    return 0;
}
