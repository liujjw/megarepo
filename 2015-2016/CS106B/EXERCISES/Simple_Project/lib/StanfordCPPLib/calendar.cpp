#include "calendar.h"
#include <string>
using namespace std;

int daysInMonth(Month month, int year) {
    switch (month) {
    case April:
    case June:
    case September:
    case November:
        return 30;
    case February:  
        return (isLeapYear(year)) ? 29 : 28;
    default:
        return 31; 
    }
}

bool isLeapYear(int year) {
  return ((year % 4 == 0) && (year % 100 != 0))
       || (year % 400 == 0);
}

string monthToString(Month month){
    switch(month){
    case 1:
        return string("January");
    case 2:
        return string("February");
    default:
        return "didn't feel like writing them ";
    }
}