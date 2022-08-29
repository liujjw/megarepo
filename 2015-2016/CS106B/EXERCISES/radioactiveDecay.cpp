#include "simpio.h"
#include "console.h"
#include "random.h"
#include <iostream>
using namespace std;

int main(){
    int remainingAtoms = getInteger("sample size: ");
    double decayProbabilityPerYear = getReal("probability to decay per year:  ");

    int yearCounter = 0;
    while(remainingAtoms > 0){
        int decayed = 0;
        for(int i = 0; i < remainingAtoms; i++){
            if(randomChance(decayProbabilityPerYear)) decayed++;
        }
        remainingAtoms -= decayed;
        yearCounter++;
        cout << "At the end of year " << yearCounter <<
            ", there are " << remainingAtoms << " left." << endl;
    }
    return 0;
}
