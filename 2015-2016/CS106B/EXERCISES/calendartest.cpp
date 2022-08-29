#include <iostream>
#include "simpio.h"
#include "console.h"

int main(){
	int year = getInteger("Enter a year: ");
	for(int i = 1; i <= 12; i++){
		Month month = i;
		cout << monthToString(month) << daysInMonth(month, year) << endl;
	}
	return 0;
}