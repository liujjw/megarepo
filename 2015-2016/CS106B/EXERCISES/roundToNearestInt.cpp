#include <iostream>
#include "console.h"
#include "simpio.h"

int round(double roundee);

int main(){
	while(true){
		double roundee = getReal(" \n What number to round? ");
		std::cout << round(roundee) << std::endl;
	}
}

int round(double roundee){
	if (roundee > 0)
		return roundee + 0.5;
	else
		return roundee - 0.5;

	return -1;
}