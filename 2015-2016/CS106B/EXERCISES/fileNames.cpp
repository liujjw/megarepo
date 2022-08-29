#include <iostream>
#include <string>
#include "console.h"
using namespace std;

string getRoot(string fullName){
	string root;
	for(int i = 0; i < fullName.length(); i++){
		if(fullName[i] == '.')
			break;
		root += fullName[i];
	}
	return root;
}

string getExtension(string fullName){
	for(int i = 0; i < fullName.length(); i++){
		if((fullName[i] == '.') && (i + 1 != fullName.length())
			return fullName.substr(i);
	}
	return fullName;
}

int main(){
	string file = "sample.txt";
	cout << getRoot(file) << endl;
	cout << getExtension(file) << endl;
}
