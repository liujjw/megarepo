#include <iostream>
using namespace std;

const int n = 10000;
int main(){
	double pi = 1;
	double odds = 1;
	for(int i = 1; i <= 10000; i++){
		if(i % 2 != 0) pi -= (1.0 / (odds += 2));
		else pi += (1.0 / (odds += 2));
	}
	cout << pi << " = pi/4 "<< endl;
}