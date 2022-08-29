#include "console.h"
#include <iostream>
#include <string>
#include <iomanip>
using namespace std;

int combinations(int n, int k){
    if(n == k || k == 0){
        return 1;
    }else{
        return combinations(n - 1, k - 1) + combinations(n - 1, k);
    }
}

int main(){
    cout << combinations(6, 2) << endl;
    return 0;
}
