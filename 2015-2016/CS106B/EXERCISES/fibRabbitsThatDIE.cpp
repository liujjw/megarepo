#include <iostream>
using namespace std;

int iterativeFib(int n){
	if(n == 1) return n;
	else
		return iterativeFib(n - 1) + iterativeFib(n - 3);
}

int main(){
	cout << iterativeFib(12) << endl;
}

int fib(int n){
	return additiveSequence(n, 0, 1);
}

int additiveSequence(int n, int t0, int t1){
	if(n == 0) return t0;
	if(n == 1) return t1;
	return additiveSequence(n - 1, t1, t0 + t1);
}

void rabbitsSim(){
	Vector<int> curRabbits;
	Vector<int> pastRabbits;
	curRabbits.add(1);
	for(int i = 1; i <= 12; i++){
		curRabbits.add();
	}	
}