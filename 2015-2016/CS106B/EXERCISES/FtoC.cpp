#include <iostream>
using namespace std;

double convert(double fahrenheit){
	return ((fahrenheit - 32) * 5.0/9.0);
}

int main(){
	double fahrenheit;
	cin >> fahrenheit;
	double celsius = convert(fahrenheit);
	cout << celsius << endl;
	return 0;
}