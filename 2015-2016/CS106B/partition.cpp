#include "console.h"
#include "vector.h"
#include <iostream>
using namespace std;

/*
bool partitionableHelper(Vector<int>& vec2, Vector<int>& vec3, int vec2Sum, int vec3Sum) {
    if(vec3.size() == 0) {
        return false;
    }
    if(vec2Sum == vec3Sum) {
        return true;
    } else {
        int vec3ToVec2 = vec3[0];
        vec2.add(vec3ToVec2);
        vec3.remove(0);
        vec2Sum += vec3ToVec2;
        vec3Sum -= vec3ToVec2;
        return partitionableHelper(vec2, vec3, vec2Sum, vec3Sum);
    }
}
*/

/*
bool helper(Vector<int>& vec, Vector<int>& vec2, int vecSum, int vec2Sum) {
    if(vecSum == vec2Sum) {
        return true;
    } else if (vec.isEmpty()) {
        return false;
    } else {
        for(int i = 0; i < vec.size(); i++) {
            int n = vec[i];
            vec2.add(n);
            vec2Sum += n;

            vec.remove(i);
            vecSum -= n;

            helper(vec, vec2, vecSum, vec2Sum);

            vec2.remove(vec2.size() - 1);
            vec2Sum -= n;
            vec.add(n);
            vec += n;
        }
    }
    return false;
}
*/

// vector 1 has all the elements
// we want some of those elements in vector 2 so that the sums of both are ==
// vector1 's sum is already the max, vector2 sum is 0
bool helper(Vector<int>& vector1, Vector<int>& vector2, int vector1Sum, int vector2Sum) {
    if(vector1Sum == vector2Sum) {
        return true;
    }
    // for each element in vector 1,choose explore unchoose
    for(int index = 0; index < vector1.size(); index++) {
        int element = vector1[index];

        vector2.add(element);
        vector2Sum += element;
        vector1.remove(index);
        vector1Sum -= element;

        if(helper(vector1, vector2, vector1Sum, vector2Sum)) {
            return true;
        } else {
            vector2.remove(vector2.size() - 1);
            vector2Sum -= element;
            vector1.add(element);
            vector1Sum += element;
        }

    }
    return false;
}

// find out if there's a way to separate a vector into two groups with the same sum of integers
bool partitionable(Vector<int>& vec) {
    // {1,1,2,3,5}
    if(vec.size() == 0) {
        return true;
    }
    // make another vector whose sum is 0
    Vector<int> vec2;
    int vec2Sum = 0;
    // the orignal vector has a sum of all its elements
    int vecSum = 0;
    for(int n : vec) {
        vecSum += n;
    }
    // helper function that returns actual answer
    return helper(vec, vec2, vecSum, vec2Sum);

}

bool helper2(Vector<int>& rest, int sum1, int sum2) {
    if(rest.isEmpty()) {
        return sum1 == sum2;
    } else {
        int value = rest[0];
        rest.remove(0);
        bool answer = helper2(rest, sum1 + value, sum2) ||
                        helper2(rest, sum1, sum2 + value);
        rest.insert(0, value);
        return answer;
    }
}
bool partitionable2(Vector<int>& vec) {
    return helper2(vec, 0, 0);
}

int main() {
    Vector<int> vec;
    vec.add(1);
    vec.add(4);
    vec.add(5);
    vec.add(6);
    //vec.add(5);

    if(partitionable2(vec)) {
        cout << "yes" << endl;
    }
    return 0;
}   
