#include <iostream>
#include "map.h"
#include "simpio.h"
#include "strlib.h"
#include <cctype>
#include <string>
#include <fstream>
using namespace std;

int main(){
	Map<int, string> areaCodesMap;
	Map<string, Vector<int> > invertedAreaCodesMap;

	ifstream infile;
	infile.open("AreaCodes.txt");
	if(!infile.fail()){
		string line;
		
		while(getline(infile, line)){
			areaCodesMap.put(stringToInteger(line.substr(0, 3)),
								line.substr(4));

			invertedAreaCodesMap.put(line.substr(4), 
				invertedAreaCodesMap.get(line).add(line.substr(0, 3)));
		}

	}
	while(true){
		string input = getLine("Enter area code or state name: ");
		if(!isalpha(input.at(0))){
			int areaCode = stringToInteger(input);
			cout << areaCodesMap.get(areaCode) << endl;
		}else{
			cout << invertedAreaCodesMap.get(input) << endl;
		}
	}
	return 0;
}