#include <fstream>
#include <iostream>
// #include "console.h"
using namespace std;

int main(){
	ifstream infile;
	infile.open("sample.txt");
	string longestString;
	if(!(infile.fail())){
		int longest = 0;
		string s;
		while(getline(infile, s)){
			if(s.length() > longest){
				longestString = s;
				longest = s.length();
			}
		}
	}
	cout << longestString << endl;
	return 0;
}