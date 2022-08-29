#include <iostream>
#include "simpio.h"
#include "console.h"

bool isPrime(int n){
	for(int i = 1; i < n; i++){
		if(n % i == 0) return false;
	}
	return true;
}

int main(){
	for(int i = 2; i <= 100; i++){
		if(isPrime(i)) std::cout << i << std::endl;
	}
	return 0;
}