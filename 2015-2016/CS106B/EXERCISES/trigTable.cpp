#include <iomanip>
#include <iostream>
#include <cmath>
#include "simpio.h"
#include "console.h"
#include "gmath.h"
using namespace std;

int main(){
    cout << " theta | sin(theta) | cos(theta) | " << endl;
    cout << "-------+------------+------------+" << endl;
    for(int i = -90; i <= 90; i += 15){
        cout << fixed <<right << setw(7) << i
                << "|" << setw(12) << setprecision(7)
                << sinDegrees(i) << "|" << setw(12) << setprecision(7)
                << cosDegrees(i) << "|" << endl;
    }
    return 0;
}
