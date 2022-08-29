#include "simpio.h"
#include "console.h"
#include <iostream>
#include <string>
using namespace std;

int fib(int n){
    int total = 0;
    int f1 = 1;
    int f2 = -1;
    for(int i = 0; i < n; i++){
        total = f1 + f2;
        f2 = f1;
        f1 = total;
    }
    return total;
}

int main(){
    cout << fib(10) << endl;
    return 0;
}
