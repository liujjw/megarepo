#include <iostream>
#include "simpio.h"
#include <cmath>
#include "console.h"
using namespace std;

double mySQRT(double x){
	double g = x / 2.0;
	while((g + (x / g)) / 2 != g){
		g = (g + (x / g)) / 2;
	}
	return g;

}

int main(){
	double n = getReal("What number to sqrt? ");
	cout << "Correct sqrt is: " << sqrt(n) << endl;
	cout << "My sqrt is: " << mySQRT(n) << endl;
	return 0;
}