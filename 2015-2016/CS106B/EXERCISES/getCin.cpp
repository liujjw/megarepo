#include <string>
#include <iostream>
#include <sstream>
using namespace std;

double getReal(string prompt){
	double real;
	while(true){
		cout << prompt << endl;
		string sReal;
		getline(cin, sReal);
		istringstream stream(sReal);
		stream >> real >> ws;
		if(stream.eof()) break;
	}
	return real;
}

string getLine(){
	string s;
	getline(cin, s);
	return s;
}

int main(){
	double real = getReal("Real: ");
	cout << getLine() << endl;
	return 0;
}