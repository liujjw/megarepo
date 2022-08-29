#include "console.h"
#include <iostream>
#include <string>
#include <iomanip>
using namespace std;

int digitSum(int digit){
    if(digit < 10){
        return digit;
    }else{
        int sum = digit % 10;
        return sum + digitSum(digit / 10);
    }
}

int digitalRoot(int digit){
    if(digit < 10){
        return digit;
    }else{
        return digitalRoot(digitSum(digit));
    }
}

int main(){
    cout << digitalRoot(90909888) << endl;
    return 0;
}
