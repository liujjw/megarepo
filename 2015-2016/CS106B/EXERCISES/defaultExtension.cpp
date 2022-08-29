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

string defaultExtension(string filename, string ext){
    if(ext[0] == '*'){
        string root = getRoot(filename);
        return root += ext.substr(1);
    }else{
        bool extFlag = false;
        for(int i = 0; i < filename.length(); i++){
            if(filename[i] == '.')
                extFlag = true;
        }
        if(!extFlag){
            return filename += ext;
        }
    }

    return filename;
}

int main(){
    string filename = "Shakespeare.h";
    string ext = "*.txt";
    cout << defaultExtension(filename, ext) << endl;
    return 0;
}
