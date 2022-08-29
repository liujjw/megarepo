#include <iostream>
#include <sstream>
#include <string>
using namespace std;

double stringToReal(string s){
	double real;
	istringstream in(s);
	in >> real >> ws;
	if(!in.eof()){
		exit(EXIT_FAILURE);
	}
	return real;
}

string realToString(double d){
	ostringstream s;
	s << d;
	return s.str();
}

int main(){
	string s = realToString(9.5);
	double d = stringToReal("67.43");
	cout << d << endl;
	return 0;
}