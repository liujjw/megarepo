#include <iostream>
#include <fstream>
using namespace std;

void tabsWithSpaces(ifstream & infile, ofstream & outfile){
	int tabSize = 4;
	int nextTabStop = 0;
	
	char c;
	while(infile.get(c)){

		if(c == '\t'){
			int spaces = tabSize - (nextTabStop % 4);
			nextTabStop = 0;
			
			for(int i = 0; i < spaces; i++){
				outfile.put(' ');
			}
		}else{
			outfile.put(c);
			nextTabStop++;
		}

	}
}

int main(){
	ifstream infile;
	ofstream outfile;
	cout << "Input: " << endl;
	string filename;
	getline(cin, filename);
	infile.open(filename.c_str());
	if(!infile.fail()){
		cout << "Outfile: " << endl;
		getline(cin, filename);
		outfile.open(filename.c_str());
		if(!outfile.fail()){
			tabsWithSpaces(infile, outfile);			
		}
	} 
	infile.close();
	outfile.close();
	return 0;
}