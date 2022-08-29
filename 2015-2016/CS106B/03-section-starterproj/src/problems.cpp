/*
 * CS 106B Section 03 Problems
 */

#include <cmath>
#include <iostream>
#include <string>
#include "console.h"
#include "stack.h"
#include "vector.h"
using namespace std;

void stutter(Stack<int>& s); // section problem 1
string starString(int n); // section problem 2
void writeChars(int n); // section problem 3
bool isMeasurable(int target, Vector<int>& weights); // section problem 4
int waysToClimb(int steps); // section problem 5
bool isSubsequence(string big, string small); // section problem 6

int main() {
    setConsoleSize(750, 500);
    setConsoleFont("Monospaced-Bold-14");
    setConsoleEcho(true);

    // test stutter
    Stack<int> s1, s2;
    s1.push(1);
    s2.push(1);
    s2.push(2);
    s2.push(3);
    stutter(s1);
    stutter(s2);
    cout << "stutter({1}): " << s1.toString() << " should be {1, 1}" << endl;
    cout << "stutter({1, 2, 3}): " << s2.toString() << " should be {1, 1, 2, 2, 3, 3}" << endl;

    // test starString
    cout << "starString(1): " << starString(1) << " should be **" << endl;
    cout << "starString(2): " << starString(2) << " should be ****" << endl;
    cout << "starString(4): " << starString(4) << " should be ****************" << endl;

    // test writeChars
    cout << "writeChars(1) output below should be *" << endl;
    writeChars(1);
    cout << endl << "writeChars(2) output below should be **" << endl;
    writeChars(2);
    cout << endl << "writeChars(4) output below should be <**>" << endl;
    writeChars(4);
    cout << endl << "writeChars(9) output below should be <<<<*>>>>" << endl;
    writeChars(9);
    cout << endl;

    // test isMeasurable
    Vector<int> v1, v2;
    v1 += 1, 3;
    v2 += 2, 3, 7;
    string test1 = isMeasurable(2, v1) ? "true" : "false";
    string test2 = isMeasurable(5, v1) ? "true" : "false";
    string test3 = isMeasurable(6, v2) ? "true" : "false";
    cout << "isMeasurable(2, {1, 3}): " << test1 << " should be true" << endl;
    cout << "isMeasurable(5, {1, 3}): " << test2 << " should be false" << endl;
    cout << "isMeasurable(6, {2, 3, 7}): " << test3 << " should be true" << endl;

    // test waysToClimb
    cout << "waysToClimb(1): " << waysToClimb(1) << " should be 1" << endl;
    cout << "waysToClimb(2): " << waysToClimb(2) << " should be 2" << endl;
    cout << "waysToClimb(4): " << waysToClimb(4) << " should be 5" << endl;

    // test isSubsequence
    test1 = isSubsequence("computer", "core") ? "true" : "false";
    test2 = isSubsequence("computer", "cope") ? "true" : "false";
    test3 = isSubsequence("computer", "computer") ? "true" : "false";

    cout << "isSubsequence(\"computer\", \"core\"): " << test1 << " should be false" << endl;
    cout << "isSubsequence(\"computer\", \"cope\"): " << test2 << " should be true" << endl;
    cout << "isSubsequence(\"computer\", \"computer\"): " << test3 << " should be true" << endl;
    return 0;
}


void stutter(Stack<int>& s) {
    if(!s.isEmpty()) {
        int i = s.pop();
        stutter(s);
        s.push(i);
        s.push(i);
    }
}


string starString(int n) {
    if(n < 0) throw "N cannot be negative.";
    if(n == 0) return "*";
    string stars = starString(n - 1);
    return stars + stars;
}


void writeChars(int n) {
    if(n < 0) throw "N must be positive.";
    if(n == 1) {
        cout << "*";
    } else if(n == 2){
        cout << "**";
    } else {
        cout << "<";
        writeChars(n - 2);
        cout << ">";
    }

}


bool isMeasurable(int target, Vector<int>& weights) {
    // inclusion/exclusion pattern:
    // take one element from the vector of weights
    // example problem: (3, {5, 2})
    // now weights is smaller by one, now check
    // we want weights to be size of 0 to check if target also turns into 0
    // we could start subtracting off the target with our removed element
    // which gets us -2, and we can subtract and/or add until 0...

    // well this is trying to mold our thought process towards the solution
    // i'm vaguely recallling, but i guess we can try to look into the why does the work? question
    // the process of subtracting or adding weights is not "literal", like simulation modeling, we only want
    // to model putting the weights on left or right etc
    // so putting on the left or right all sort of fits the notion of balancing out our weights evenly
    // until we are done (weights is empty) and if target is 0, we are balanced
    // this idea can be accomplished by exploring one of two choices: adding to one side or subtracting to one side sort of
    // each choice in the decsion tree explores these two possibilities so as to enumerate all possible solutions that could
    // occur

    if(weights.isEmpty()) {
        return target == 0;
    }else {
        int oneWeight = weights[0];
        Vector<int> rest = weights;
        rest.remove(0);
        return (isMeasurable(target + oneWeight, rest)
                || isMeasurable(target - oneWeight, rest)
                || isMeasurable(target, rest));
    }
}


int waysToClimb(int steps) {
    if(steps < 0) throw "Steps must be positive.";
    if(steps < 3) {
        return steps;
    } else {
        // how many ways can we make a set of vectors sum to steps with only 1s and 2s
        // these are permutations
        // 1 step has 1 way {1}, 2 steps has 2 ways {2} {11}, 3 has {1,1,1} {2,1} {1,2} has 3
        // 4 steps {1111} {22} {211} {121} {112} has 5 ways,
        // 5 steps {11111} {1112} {122} {221} {212} {2111} {1211} {1121} has 8 ways
        // looks like always increasing like a fib function
        // always the number of steps before it cause plus one to each of previous set gets us a way
        // plsu permutations of all its previous cases
        // doesnt really feel like solving the problem, but it isnt really asking us for the actual permutations so yea
        return waysToClimb(steps - 1) + waysToClimb(steps - 2);
    }
}


bool isSubsequence(string big, string small) {
    // computer, core
    if(small == "") {
        return true;
    } else if(big == "") {
        return false;
    } else {
        if(big[0] == small[0]) {
            return isSubsequence(big.substr(1), small.substr(1));
        } else {
            return isSubsequence(big.substr(1), small);
        }
    }
    return false;
}
