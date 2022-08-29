#include <iostream>
#include "set.h"
#include "console.h"
#include "lexicon.h"
#include "vector.h"
using namespace std;

bool isMeasureable(int target, Vector<int>& weights) {
    int weightsSum = 0;
    for(int i : weights) {
        weightsSum += i;
    }
    if(target == weightsSum) {
        return true;
    }
    if(weights.isEmpty()) {
        return target == 0;
    }
    Vector<int> rest = weights;
    int w1 = rest[0];
    rest.remove(0);
    return (isMeasureable(target - w1, rest) || /* move to one side*/
            isMeasureable(target, rest)); /*move to other side and not interfere with target)*/
}

int main() {
    Vector<int> weights;
    weights += 1, 3;
    if(isMeasureable(5, weights)) {
        cout << "yea" << endl;
    }
    return 0;
}
