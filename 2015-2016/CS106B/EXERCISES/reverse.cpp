#include <iostream>
using namespace std;

int reverseDigits(int digits){
	if(digits < 10) return digits;
	if(digits == 10) return 01; 
	int reversed = 0;
	while(digits / 10 > 0){
		int powerOfTen = 1;
		while(!(digits / powerOfTen < 10)){
			powerOfTen *= 10;
		}

		int lastmostDigit = digits % 10;
		digits /= 10;
		reversed += lastmostDigit * powerOfTen;

	}
	return reversed + (digits % 10);
}

int main(){
	int digits;
	cin >> digits;
	cout << reverseDigits(digits) << endl;
}