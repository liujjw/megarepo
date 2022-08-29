#include "console.h"
#include <iostream>
#include <string>
#include <iomanip>
using namespace std;

int digitSum(int digit){
    if(digit < 0){
        throw "WTF dude";
    }
    if(digit < 10){
        return digit;
    }else{
        int sum = digit % 10;
        return sum + digitSum(digit / 10);
    }
}

int main(){
    cout << digitSum(17294821) << endl;
    return 0;
}
