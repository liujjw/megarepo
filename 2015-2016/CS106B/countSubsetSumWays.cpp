#include <iostream>
#include "set.h"
#include "console.h"
using namespace std;

int countSubsetSumWays(Set<int>& asdfg, int sum) {
    int numWays = 0;
    if(asdfg.isEmpty()) {
        if(sum == 0) {
            numWays++;
        }
        return numWays;
    }
    int value = asdfg.first();
    Set<int> rest = asdfg - value;
    return numWays + countSubsetSumWays(rest, sum - value) + countSubsetSumWays(rest, sum);
}

int main() {
    Set<int> asdfg;
    asdfg += 1, 3, 4, 5;
    cout << countSubsetSumWays(asdfg, 11) << endl;
    return 0;
}
