#include "simpio.h"
#include "console.h"
#include "random.h"
#include <iostream>
using namespace std;

int main(){
	int flips = 0;
	int heads = 0;
	while(true){
		if(randomChance(0.5)){
			cout << "heads" << endl;
			heads++;
		}else{
			cout << "tails" << endl;
			heads--;
		}
		if(heads == 3) break;
		flips++;
	}
	cout << flips << " flips were made" << endl;
	return 0;
}