#include "random.h"
#include <iostream>
#include "simpio.h"
#include "console.h"

int main(){
	int trials = getInteger("How many trials? ");

	double sum = 0;
	for(int i = 0; i < trials; i++){
		sum += randomReal(0, 1);
	}

	cout << sum / trials << endl;
}