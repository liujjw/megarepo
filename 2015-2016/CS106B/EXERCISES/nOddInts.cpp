#include <iostream>
using namespace std;


int caculateNOdds(int n){
	int sum = 0;
	int oddCounter = 1;
	for(int i = 0; i < n; i++){
		sum += oddCounter;
		oddCounter += 2;
	}
	return sum;
}

int main(){
	int n;
	cin >> n;

	cout << caculateNOdds(n) << endl;
}