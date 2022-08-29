#include <iostream>
using namespace std;


double convertToFeet(double meters){
	return (39.37007874 * meters / 12);
}

int convertToInches(double feet){
	double decimals = feet - double(int(feet));
	return (decimals * 12);
}

int main(){
	double meters;
	cin >> meters;
	double feet = convertToFeet(meters);
	int inches = convertToInches(feet);
	cout << int(feet) << " " << inches << endl;

}