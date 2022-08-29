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

void reshape(Grid<int> & grid, int nRows, int nCols){
    Vector<int> gridBuffer;
    for(int buffer : grid){
        gridBuffer.add(buffer);
    }

    grid.resize(nRows, nCols);

    int index = 0;
    for(int i = 0; i < grid.numRows(); i++){
        for(int j = 0; j < grid.numCols(); j++){
            if(index >= gridBuffer.size()) break;
            grid[i][j] = gridBuffer.get(index);
            index++;
        }
    }
}

int main(){
    Grid<int> myGrid(2, 1);
    myGrid[0][0] = 42;
    myGrid[1][0] = 69;

    reshape(myGrid, 5, 5);

    int j = 0;
    for(int count : myGrid){
        if (j == 5){
            cout << endl;
            j = 0;
        }
        cout << right << setw(3) << count;
        j++;
    }

    return 0;
}
