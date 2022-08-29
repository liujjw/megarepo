
#include <iostream>
#include "simpio.h"
#include "console.h"


void convertToImperial(double meters, double & feet, double & inches);

int main(){
    double feet, inches;
    double meters = getReal("Provide meters: ");
    convertToImperial(meters, feet, inches);
    std::cout << feet << " feet, " << inches << " inches";
    return 0;
}

void convertToImperial(double meters, double & feet, double & inches){
    double inchesInMeter = 1.0/0.0254;
    feet = (inchesInMeter * meters) / 12;
    inches = (feet - int(feet)) * 12;
    feet = int(feet);
}
