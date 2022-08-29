#include <iomanip>
#include <iostream>
#include <fstream>
#include <sstream>
// #include "console.h"
using namespace std;


void reset(ifstream & infile, string filename){
	infile.close();
	infile.open(filename.c_str());
	if(infile.fail()){
		cout << "error" << endl;
	}
}

int findChars(ifstream & infile){
	int counter = 0;
	int c;
	while((c = infile.get()) != EOF)
		counter++;
	return counter;
}

int findWords(ifstream & infile){
	int counter = 0;
	int c;

	int isWord = false;
	while((c = infile.get()) != EOF){
		if(isalpha(char(c))){
			isWord = true;
		}else{
			if(isWord){
				counter++;
			}
			isWord = false;
		}
	}

	return counter;
}

int findLines(ifstream & infile){
	int counter = 0;
	string s;
	while(getline(infile, s))
		counter++;
	return counter;
}	

string integerToString(int integer){
	ostringstream s;
	s << integer;
	return s.str();
}

int main(){
	ifstream infile;
	infile.open("sample.txt");
	if(!infile.fail()){
		int chars = findChars(infile);
		reset(infile, "sample.txt");
		int words = findWords(infile);
		reset(infile, "sample.txt");
		int lines = findLines(infile);

		int greatest = -1;
		if(chars > words && chars > lines) greatest = chars;
		else if(words > chars && words > lines) greatest = words;
		else if(lines > chars && lines > words) greatest = lines;
		int spacing = integerToString(greatest).length(); 
		cout << "Chars: " << right << setw(spacing) << chars << endl
				<< "Words: " << setw(spacing) << words << endl
				<< "Lines: " << setw(spacing) << lines << endl;
	}
	infile.close();
}