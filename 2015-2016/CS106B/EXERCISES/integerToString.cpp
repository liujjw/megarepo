#include <iostream>
#include <string>
#include "simpio.h"
#include "console.h"
using namespace std;

string integerToString(int n){
    if(n < 10){
        string s = " ";
        s[0] = (char('0' + n));
        return s;
    }else{
        char lastDig = char('0' + (n % 10));
        return (integerToString(n / 10) + lastDig);
    }
}

int main(){
    cout << integerToString(789) << endl;
    return 0;
}

