#include <iostream>
using namespace std;

const int sentinel = 0;
int main(){
	int largest = 0;
	int secondLargest = 0;
	while(true){
		int in;
		cin >> in;
		if(in == sentinel) break;
		if(in > largest){
			largest = in;
		}else if(in > secondLargest){
			secondLargest = in;
		}
	}
	cout << "largest is " << largest << endl;
	cout << "second is " << secondLargest << endl;
}