#include <iostream>
#include <fstream>
using namespace std;

bool notBanished(char c, string banishment){
	for(int i = 0; i < banishment.length(); i++){
		if(c == banishment[i]){
			return false;
		}
	}
	return true;
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
			cout << "Elimination string: " << endl;
			string banishment;
			getline(cin, banishment);
			char c;
			while(infile.get(c)){
				if(notBanished(c, banishment)){
					outfile.put(c);
				}
			}
		}
	} 
	infile.close();
	outfile.close();
	return 0;
}