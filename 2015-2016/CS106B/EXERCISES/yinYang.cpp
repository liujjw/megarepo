#include "simpio.h"
#include "console.h"
#include "gwindow.h"
#include <iostream>
using namespace std;

int main(){
    GWindow gw;
    double height = gw.getHeight();
    double width = gw.getWidth();
    
    gw.setColor("BLACK");
    gw.fillOval(gw.getWidth() / 2, gw.getHeight() / 2);
    return 0;
}
