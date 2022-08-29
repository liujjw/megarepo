/*
 * Recursion Warm-Ups, CS106B
 * --------------------------
 * Implmentation of the human pyramid and
 * Sierpinski triangle recursive helper functions from main.
*/

// Please feel free to add any other #includes you need!
#include "recursionproblems.h"
#include <cmath>
#include <iostream>
#include "hashmap.h"
#include "map.h"
#include "random.h"
using namespace std;

/*
 * Weight on a given person's knees in row, col is defined as their weight
 * plus half of 2 people's weight above them.
*/
double weightOnKnees(int row, int col, const Vector<Vector<double> >& weights) {
    if(row > weights.size() - 1 || row < 0) return 0.0;
    if(col > weights[row].size() - 1 || col < 0) return 0.0;
    if(row == 0 && col == 0){
        return weights[row][col];
    }else{
        int aboveRow = row - 1;
        int aboveCol1 = col - 1;
        int aboveCol2 = col;
        double combinedWeight = weights[row][col] +
                (0.5 * (weightOnKnees(aboveRow, aboveCol1, weights)
                + weightOnKnees(aboveRow, aboveCol2, weights)));
        return combinedWeight;
    }
}

/*
 * Draws a Sierpinski triangle on a given GWindow.
 * Starts from the smallest triangles to largest.
*/
void drawSierpinskiTriangle(GWindow& gw, double x, double y, int size, int order) {
    if(x < 0 || y < 0 || size < 0 || order < 0) throw "Negative numbers not allowed!";
    if(order != 0){
        if(order == 1){
            // base case: draws ONE triangle
            gw.drawLine(x, y, x + size, y);
            gw.drawLine(x + size, y, x + (size / 2), y + (size / 2 * sqrt(3)));
            gw.drawLine(x + size / 2, y + (size / 2 * sqrt(3)), x, y);
        }else{
            // recursive decomposition: draws three triangles, with each successive call drawing its own three
            // triangles
            double lowerOrderSize = double(size) / 2;
            drawSierpinskiTriangle(gw, x, y, lowerOrderSize, order - 1);
            drawSierpinskiTriangle(gw, x + lowerOrderSize, y, lowerOrderSize, order - 1);
            drawSierpinskiTriangle(gw, x + (lowerOrderSize / 2.0), y + (lowerOrderSize / 2 * sqrt(3)), lowerOrderSize, order - 1);
        }

    }

}
/*Outside-in sierpinski*/
/*
    if(order != 0){
        gw.drawLine(x, y, x + size, y);
        gw.drawLine(x + size, y, x + (size / 2), y + (size / 2 * sqrt(3)));
        gw.drawLine(x + size / 2, y + (size / 2 * sqrt(3)), x, y);

        drawSierpinskiTriangle(gw, x + size, y, size / 2, order - 1);
    }

 */
