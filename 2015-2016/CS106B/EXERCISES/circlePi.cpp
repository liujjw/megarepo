#include <iostream>
#include <cmath>
using namespace std;

const double numCircles = 10000.0, quarterCircleRadius = 2.0;

int main(){
	double area = 0;

	for(int i = 0; i < numCircles; i++){
		double width = quarterCircleRadius / numCircles;
		double midPointX = (width / 2) + (width * i);
		double height_y = sqrt((quarterCircleRadius * quarterCircleRadius) - (midPointX * midPointX));
		area += width * height_y;
	}
	cout << area << endl;
}