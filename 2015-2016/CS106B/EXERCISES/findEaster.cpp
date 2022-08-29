#include <iostream>
#include <string>
#include "simpio.h"
#include "console.h"
using namespace std;

void findEaster(int YEAR, string & month, int & day){
	// Anonymous easter alogorithm a la wikipedia
	int a = YEAR % 19;
	int b = YEAR / 100;
	int c = YEAR % 100;
	int d = b / 4;
	int e = b % 4;
	int f = (b + 8) / 25;
	int g = (b - f + 1) / 3;
	int h = ((19 * a) + b - d - g + 15) % 30;
	int i = c / 4;
	int k = c % 4;
	int l = (32 + (2 * e) + (2 * i) - h - k) % 7;
	int m = (a + (11 * h) + (22 * l)) / 451;
	int intMonth = (h + l - (7 * m) + 114) / 31;
	int intDay = ((h + l - (7 * m) + 114) % 31) + 1;

	if(intMonth == 3)
		month = "March";
	else if(intMonth == 4)
		month = "April";
	else
		month = "error";

	day = intDay;
}

const int YEAR = 2017;

int main(){
	string month;
	int day;
	findEaster(YEAR, month, day);
	
	cout << "Easter in " << YEAR << " is " << month << " " << day << endl;
	return 0;
}