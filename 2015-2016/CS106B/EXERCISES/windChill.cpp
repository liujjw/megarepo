#include <iostream>
#include <iomanip>
#include "simpio.h"
#include "console.h"
#include "error.h"
#include <cmath>
#include <cstdlib>
using namespace std;

double windChill(double temp, double windSpeed);

int main(){
    cout << "Temperature (Right)" << endl <<
            "Wind Speed (Down)" << endl << endl
            << setw(8) << "" << "|";
    for(int i = 40; i >= -45; i -= 5){
        cout << setw(4) << i << "|";
    }
    cout << endl << "--------+";
    for(int i = 40; i >= -45; i -= 5){
        cout << "----+";
    }

    for(int i = 5; i <= 60; i += 5){
        cout << endl <<setw(8) << i << "|";
        for(int temp = 40; temp >= -45; temp -= 5){
            cout << setw(4) << setprecision(0) << fixed << windChill(temp, i) << "|";
        }
    }

    return 0;
}

double windChill(double temp, double windSpeed){
    if(temp > 40){
        error("Undefined");
        exit(EXIT_FAILURE);
    }

    if(windSpeed == 0)
        return temp;
    else
        return (35.74 + (0.6215 * temp) -
                (35.75 * pow(windSpeed, 0.16))
                + 0.4275 * temp * pow(windSpeed, 0.16));
}
