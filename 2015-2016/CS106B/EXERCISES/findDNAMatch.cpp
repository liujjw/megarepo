#include <string>
#include <iostream>
#include "console.h"
#include <cctype>
#include "strlib.h"
#include "simpio.h"
using namespace std;

int findDNAMatch(string dnaBase, string dnaStrand, int start = 0);
bool isBaseLink(char base1, char base2);

int main(){
    string dnaStrand = "TGC";
    string dnaBase = "TAACGGTACGTC";
    cout << findDNAMatch(dnaBase, dnaStrand, 3) << endl;
    return 0;
}

int findDNAMatch(string dnaBase, string dnaStrand, int start){

    int strandCounter = 0;

    for(int baseCounter = start;
        baseCounter < dnaBase.length(); baseCounter++){

        if(isBaseLink(dnaBase[baseCounter],
                      dnaStrand[strandCounter])){

            bool completeMatch = true;

            for(strandCounter = 1;
                strandCounter < dnaStrand.length(); strandCounter++){

                if(!isBaseLink(dnaBase[baseCounter + strandCounter],
                               dnaStrand[strandCounter])){

                    strandCounter = 0;
                    completeMatch = false;
                    break;

                }
            }

            if(completeMatch)
                return baseCounter;
        }

    }

    return -1;
}

bool isBaseLink(char base1, char base2){
    base1 = toupper(base1);
    base2 = toupper(base2);

    if((base2 == 'G' && base1 == 'C') ||
        (base1 == 'G' && base2 == 'C')) // only linkage if gaunine and cytosine, vice versa
            return true;

    if((base2 == 'A' && base1 == 'T') ||
        (base1 == 'A' && base2 == 'T')) // ^ adenosine and thymine
            return true;

    return false;
}
