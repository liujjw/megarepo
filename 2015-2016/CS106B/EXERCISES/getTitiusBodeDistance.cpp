#include <iostream>
#include "console.h"
using namespace std;

int sequence(int k) {
    if(k == 1){
        return 1;
    }else if(k == 2) {
        return 3;
    } else {
        return (sequence(k - 1) * 2);
    }
}

double getTitiusBodeDistance(int planet) {
    return double(4 + sequence(planet)) / 10;
}

int main(){
    cout << getTitiusBodeDistance(5) << endl;
    return 0;
}
