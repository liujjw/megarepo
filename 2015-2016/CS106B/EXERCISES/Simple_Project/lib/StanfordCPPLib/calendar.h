#ifndef _calendar_h
#define _calendar_h

#include <string>

enum Month{
	January = 1,
	February,
	March,
	April,
	May, 
	June, 
	July,
	August,
	September,
	October,
	November,
	December
};

/*
* Function: daysInMonth
* Usage: int days = daysInMonth(February, 2012)
*/
int daysInMonth(Month month, int year);

/*
* Function: isLeapYear
* Usage: if(isLeapYear(2012))
*/
bool isLeapYear(int year);

/*
* Function: monthToString
* Usage: string month = monthToString(February)
*/
std::string monthToString(Month month);

#endif