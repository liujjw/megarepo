#include "console.h"
#include <iostream>
#include <string>
#include <iomanip>
using namespace std;

int countFib1(int n, int  &counter);
int countFib2(int n, int t0, int t1, int &counter);

int main(){
    cout << "n    fib1    fib2" << endl;
    cout << "--   ----    ----" << endl;
    for(int i = 0; i <= 12; i++){
        int counter = 0;
        int counter2 = 0;
        countFib1(i, counter);
        countFib2(i, 0, 1, counter2);
        cout << right << setw(2) << i <<
                setw(7) << counter <<
                setw(8) << counter2 << endl;
    }
    return 0;
}

int countFib1(int n, int & counter){
    counter++;
    if(n < 2){
        return n;
    }else{
        return countFib1(n - 1, counter) + countFib1(n - 2, counter);
    }
}

int countFib2(int n, int t0, int t1, int & counter){
    counter++;
    if(n == 0) return t0;
    if(n == 1) return t1;
    return countFib2(n - 1, t1, t0 + t1, counter);
}
