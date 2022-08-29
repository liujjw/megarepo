#include "simpio.h"
#include "console.h"
#include "gwindow.h"
#include <iostream>
using namespace std;

int main(){
    GWindow gw;
    double height = gw.getHeight();
    double width = gw.getWidth();

    // background color
    gw.setColor("CYAN");
    gw.fillRect(0, 0, gw.getWidth(), gw.getHeight());

    // rainbow arcs
    gw.setColor("RED");
    gw.fillOval(width * -0.27, 0.1 * height, 1.5 * width, 1.5 * height);
    gw.setColor("ORANGE");
    gw.fillOval(width * -0.27, 0.2 * height, 1.5 * width, 1.5 * height);
    gw.setColor("YELLOW");
    gw.fillOval(width * -0.27, 0.3 * height, 1.5 * width, 1.5 * height);
    gw.setColor("GREEN");
    gw.fillOval(width * -0.27, 0.4 * height, 1.5 * width, 1.5 * height);
    gw.setColor("BLUE");
    gw.fillOval(width * -0.27, 0.5 * height, 1.5 * width, 1.5 * height);
    gw.setColor("MAGENTA");
    gw.fillOval(width * -0.27, 0.6 * height, 1.5 * width, 1.5 * height);
    gw.setColor("CYAN");
    gw.fillOval(width * -0.27, 0.7 * height, 1.5 * width, 1.5 * height);

    return 0;
}
