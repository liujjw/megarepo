#include <iostream>
#include <fstream>
#include <sstream>
#include "console.h"
#include "vector.h"
#include "filelib.h"
#include "strlib.h"
using namespace std;

double mean(Vector<double> & data){
    double sum;
    for(double dataPoint : data){
        sum += dataPoint;
    }
    return sum / data.size();
}

int main(){
    Vector<double> data = { 1.5, 1.6, 1.7};
    cout << mean(data) << endl;
    return 0;
}
