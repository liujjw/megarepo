#include <iostream>
#include <string>
#include <fstream>
#include <sstream>
#include <cstdlib>
#include <ctime>
//#include "console.h"
//#include "filelib.h"
using namespace std;

void promptUserForFile(ifstream & infile, string prompt){
    string filename;
    while(true){
        cout << prompt << endl;
        getline(cin, filename);
        infile.clear();
        infile.open(filename.c_str());
        if(!infile.fail()) break;
    }
}

void removeComments(istream & is, ostream & os){
    bool isComment = false;
    char c;
    char nextChar;
    while(is.get(c)){

        if(c == '/'){
            if(is.get(nextChar)){
                if(nextChar == '*'){
                    isComment = true;
                }else if(nextChar == '/'){
                    string toBeSkipped;
                    getline(is, toBeSkipped);
                }else{
                    if(!isComment)
                        os.put(c);
                    is.unget();
                }
            }else{
                if(!isComment)
                    os.put(c);
            }
        }else if(c == '*'){
            if(is.get(nextChar)){
                if(nextChar == '/'){
                    isComment = false;
                }else{
                    if(!isComment)
                        os.put(c);
                    is.unget();
                }
            }else{
                if(!isComment)
                    os.put(c);
            }
        }else{
            if(!isComment)
                os.put(c);
        }

    }
}

int main(){
    ifstream infile;
    promptUserForFile(infile, "File: ");
    removeComments(infile, cout);
    cout << endl;
    return 0;
}
