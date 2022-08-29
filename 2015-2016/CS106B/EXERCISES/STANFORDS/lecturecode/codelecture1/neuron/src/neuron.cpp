#include <iostream>  // This is a true classic.
#include <cmath>     // Another great
#include "console.h" // This is a Stanford Library!
using namespace std; // The best namespace.

double neuron(double x1, double x2);
double sigmoid(double x);

const double WEIGHT1 = 9.5;
const double WEIGHT2 = -5.5;

int main() {

    double x1 = 0.00;
    double x2 = 0.99;

    double output = neuron(x1, x2);

    cout << "output: " << output << endl;

    return 0;
}

double neuron(double x1, double x2) {
    double inputSum = 0;
    inputSum += x1 * WEIGHT1;
    inputSum += x2 * WEIGHT2;
    return sigmoid(inputSum);
}

double sigmoid(double x){
    return 1 / (1 + exp(-x));
}


