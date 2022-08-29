#include "simpio.h"
#include "console.h"
#include "random.h"
#include <iostream>
using namespace std;

int main(){
    int darts = getInteger("darts: ");
    int insideCircle = 0;
    for(int i = 0; i < darts; i++){
        double x = randomReal(-1, 1);
        double y = randomReal(-1, 1);
        if(x * x + y * y < 1) insideCircle++;
    }
    cout << "ratio between darts inside circle to total darts is " << double(insideCircle) / darts <<
        " which is an approximation of the area of a circle to area of a square: pi/4" << endl;
    return 0;
}
