/*
* Header for a combinatorics library, including implmentations of permutations and 
* combinations functions.
*/

#ifndef _combinatorics_h
#define _combinatorics_h

/*
* Function: permutations(n, k)
* Usage: int i = permutations(n, k)
* ----------------------------------
* Calculates the permutations of an n set of elements with k as the size of the subset, recursively.
* Permutations are the possibilities of a set of elements where differing orders mean different possibilities.
*/
int permutationsRecursive(int n, int k);

/*
* Function: permutations(n, k)
* Usage: int i = permutations(n, k)
* ----------------------------------
* Calculates the permutations of an n set of elements with k as the size of the subset with efficiency 
* improvements by not calling a factorial function. Different implmentation-wise from permutationsRecurive(n, k) function.
*/
int permutations(int n, int k);

/*
* Function: combinations(n, k)
* Usage: int i = combinations(n, k)
* ----------------------------------
* Calculates the combinations of an n set of elements with k as the size of the subset.
* Combinations do not take ordering of elements into account in each possibility.
*/
int combinations(int n, int k);

#endif