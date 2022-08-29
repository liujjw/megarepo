#include "combinatorics.h"
#include <iostream>
#include <cstdlib>

/*
* Implementation of permutations function.
* Recursive implmentation that is more efficient than large factorials.
*/
int permutationsRecursive(int n, int k){
	if(k > n){
        error("K can't be bigger than N");
        exit(EXIT_FAILURE);
    }
    if(k == 0) return 1;
    return ((n - k) + 1) * permutations(n, k - 1);
}

/*
* Implementation of permutations function.
* Does not call a factorial as explicitly as in mathemetically correct formula.
*/
int permutations(int n, int k){
	if(k > n || k == 0){	
		error("undefined k");
		exit(EXIT_FAILURE);
	}
	// using stricly facotorial definition, numbers get quite large, 
	// this implmentation reasons that we are only interested in computing 
	// the factorial from ((n - k) + 1) up to n, we are still sort of using a factorial definition
	// and we get the same anwser, but unecessary calculation is discarded
	// we can do this because of division properties, of which i cannot possibly describe well here
	int total = 1;
	int startAt = (n - k) + 1;
	for(int i = startAt; i <= n; i++){
		total *= i;
	}
	return total;
	
}

/*
* Implementation of combinations function.
* Derived from textbook definition, but using effieicency enhanced implmentation, but barely
*/
int combinations(int n, int k){
    int nMinusK = n - k;
    int numeratorStart = nMinusK > k ? nMinusK + 1 : k + 1;

    long numeratorProduct = 1; // these can get large and mess up calculation
    for(int i = numeratorStart; i <= n; i++){
        numeratorProduct *= i;
    }

    int lesserDenominator = nMinusK < k ? nMinusK : k;
    long denominatorProduct = 1; // can get too large
    for(int i = 1; i <= lesserDenominator; i++){
        denominatorProduct *= i;
    }

    return (numeratorProduct / denominatorProduct);

}