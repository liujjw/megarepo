#include <iostream>
#include <fstream>
#include <sstream>
#include <cstdlib>
#include <string>
#include "console.h"
#include "vector.h"
#include "grid.h"
#include "simpio.h"
#include "filelib.h"
#include "strlib.h"
#include "set.h"
#include "stack.h"
#include "queue.h"
#include "map.h"
#include "iomanip.h"
using namespace std;

bool isSameType(char c1, char c2){
    if(c1 == '(' && c2 == ')')
        return true;
    if(c1 == '{' && c2 == '}')
        return true;
    if(c1 == '[' && c2 == ']')
        return true;

    return false;
}

bool bracketingMatch(string s){

    Stack<char> groupCharacters;
    for(int i = 0; i < s.length(); i++){
        char c = s.at(i);
        if(c == '(' || c == '{' || c == '['){
            groupCharacters.push(c);
        }else if(c == ')' || c == '}' || c == ']'){
            if(groupCharacters.isEmpty()) return false;
            char c2 = groupCharacters.peek();
            if(isSameType(c2, c))
                groupCharacters.pop();
            else
                return false;
        }
    }
    if(!groupCharacters.isEmpty())
        return false;

    return true;
}

int main(){
    string s = "{ s = 2 * (a[2] + 3); x = (1 + (2)); }";
    if(bracketingMatch(s))
        cout << "yep" << endl;
    return 0;

}


