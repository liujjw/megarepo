#include <iostream>
#include "simpio.h"
#include "console.h"

bool isPerfect(int n){

	int sumOfDivisors = 0;
	for(int i = 1; i <= n / 2; i++){
		if(n % i == 0) sumOfDivisors += i;
	}
	if(sumOfDivisors == n) return true;

	return false;
}

int main(){
	for(int i = 1; i <= 10000; i++){
		if(isPerfect(i))
			std::cout << i << std::endl;
	}
	return 0;
}