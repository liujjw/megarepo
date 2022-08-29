#include "console.h"
#include <iostream>
#include <string>
#include <iomanip>
using namespace std;

string reverse(string str){
    if(str.length() == 1){
        return str;
    }else{
        string rStr = str.substr(str.length() - 1);
        return rStr + reverse(str.substr(0, str.length() - 1));
    }
}

int main(){
    cout << reverse("dog") << endl;
    return 0;
}
