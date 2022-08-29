#include <iostream>
#include "console.h"
#include "simpio.h"
#include "error.h"
using namespace std;

double factorial(int n){
    double sum = 1;
    for(int i = 1; i <= n; i++){
        sum *= i;
    }
    return sum;
}

double permute(int n, int k){
    // P(n, k) = n! / (n - k)!
    // factorial implmentation, not very efficient, see combinatorics library for recursive one
    if(k > n){
        error("K can't be bigger than N");
        exit(EXIT_FAILURE);
    }
    return (factorial(n) / (factorial(n - k)));

}

int main(){
    while(true){
        int n = getInteger("n = ");
        int k = getInteger("k = ");
        cout << permute(n, k) << endl << endl;
    }

    return 0;
}
