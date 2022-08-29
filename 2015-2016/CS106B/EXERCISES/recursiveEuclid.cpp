#include "simpio.h"
#include "console.h"
#include <iostream>
#include <string>
using namespace std;

int rgcd(int x, int y){
    if(x % y == 0){
        return y;
    }else{
        return rgcd(y, x % y);
    }
}

int main(){
    cout << rgcd(10, 15) << endl;
    return 0;
}
