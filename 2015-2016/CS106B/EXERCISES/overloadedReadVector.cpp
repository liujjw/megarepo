#include <iostream>
#include <fstream>
#include <sstream>
#include "console.h"
#include "vector.h"
#include "filelib.h"
#include "strlib.h"
using namespace std;

void readVector(istream & is, Vector<int> & vec){
    string line;
    while(getline(is, line)){
        if(line == "") break;
        vec.add(stringToInteger(line));
    }
}

int main(){
    string s = "1\n2\n1\n\n35";
    istringstream stream(s);
    Vector<int> vec;
    readVector(stream, vec);
    for(int out : vec){
        cout << out << endl;
    }
    return 0;
}
