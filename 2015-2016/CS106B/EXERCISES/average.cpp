#include <iostream>
using namespace std;

const int sentinel = -1;
int main(){
	int sum = 0;
	int counter = 0;
	while(true){
		int in;
		cin >> in;
		if(in == sentinel) break;
		sum += in;
		counter++;
	}
	cout << double(sum) / counter << endl;
}
