#include "simpio.h"
#include "console.h"
#include "gwindow.h"
#include <iostream>
using namespace std;

int main(){
    GWindow gw(400, 400);
    double height = gw.getHeight();
    double width = gw.getWidth();

    double squareWidth = 1.0/8 * (gw.getWidth());
    double squareHeight = 1.0/8 * (gw.getHeight());

    for(int i = 0; i < 8; i++){
        for(int k = 0; k < 8; k++){

            gw.setColor("WHITE");
            if((k + i) % 2 != 0) gw.setColor("BLACK");

            gw.fillRect(squareWidth * i, squareHeight* k, squareWidth, squareHeight);

            if(!(k >= 3) && (k + i) % 2 != 0){
                gw.setColor("RED");
                gw.fillOval(squareWidth * i + (0.1 * squareWidth), squareHeight * k + (0.1 * squareHeight),
                            squareWidth * 0.7, squareHeight * 0.7);
            }
        }
    }

    return 0;
}
