#include "console.h"
#include <iostream>
#include <string>
#include <iomanip>
using namespace std;

string integerToString(int n){
    if(n < 10){
        string empty = "";
        return (empty + n);
    }else{
        string empty = "";
        string lastDig = empty + (n % 10);
        return integerToString(n / 10) + lastDig;
    }
}

int main(){
    string s = integerToString(789);
    cout << s << endl;
    return 0;
}

