#include <iostream>
#include <string>
using namespace std;

/* :O amazing */
string primeFactorize(int n){
	string result;
	for(int i = 2; i <= n; i++){
		while(n % i == 0){
			result += to_string(i) + " ";
			n /= i;
		}
	}
	
	return result;
}

int main(){
	int n;
	cin >> n;
	cout << primeFactorize(n) << endl;
}