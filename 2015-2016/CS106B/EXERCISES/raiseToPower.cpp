#include <iostream>
#include "console.h"
using namespace std;

int raiseToPower(int n, int k){
    if(k == 0){
        return 1;
    }else{
        return n * raiseToPower(n, k - 1);
    }
}

int main(){
    cout << raiseToPower(10, 5) << endl;
    return 0;
}
