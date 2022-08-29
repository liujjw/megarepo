#include <iostream>
#include <string>
#include <fstream>
#include <cstdlib>
#include <ctime>
#include <cmath>
//#include "console.h"
//#include "filelib.h"
using namespace std;

static void initRandomSeed() {
    static bool initialized = false;
    if (!initialized) {
        srand(int(time(NULL)));
        rand();   // BUGFIX: throwaway call to get randomness going
        initialized = true;
    }
}

int randomInteger(int low, int high) {
    initRandomSeed();
    double d = rand() / (double(RAND_MAX) + 1);
    double s = d * (double(high) - low + 1);
    return int(floor(low + s));
}

void promptUserForFile(ifstream & infile, string prompt){
    string filename;
    while(true){
        cout << prompt << endl;
        getline(cin, filename);
        infile.clear();
        infile.open(filename.c_str());
        if(!infile.fail()) break;
    }
}

char randomizeChar(char c){
    if(isupper(c)){
        return ('A' + (((c - 'A') + randomInteger(0, 26)) % 26));
    }else{
        return ('a' + (((c - 'a') + randomInteger(0, 26)) % 26));
    }
}

int main(){
    ifstream infile;
    promptUserForFile(infile, "Input file: ");

    int c;
    while((c = infile.get()) != EOF){
        if(isalpha(char(c)))
            cout << randomizeChar(char(c));
        else
            cout << char(c);
    }
    cout << endl;

    infile.close();
}
