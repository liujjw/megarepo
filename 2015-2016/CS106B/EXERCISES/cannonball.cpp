#include <iostream>
#include "console.h"
using namespace std;

int cannonball(int height){
    if(height == 1){
        return 1;
    }else{
        return (height * height) + cannonball(height - 1);
    }
}

int main(){
    cout << cannonball(10) << endl;
    return 0;
}
