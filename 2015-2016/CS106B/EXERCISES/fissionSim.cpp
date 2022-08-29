#include <iostream>
#include "iomanip"
#include "grid.h"
#include "vector.h"
#include "random.h"
using namespace std;

const int BOUNCE_MAX = 4;
const int BOUNCE_MIN = 1;
const int BALLS_ON_TRAP = 2;
const int BOX_SIDE = 25;
const int INITIAL_BALLS = 1;

void runSimulation(int & timeUnits, double & percentSprung, int & maxBalls);
void printReport(int timeUnits, double percentSprung, int maxBalls);

int main(){
    int timeUnits;
    double percentSprung;
    int maxBalls;

    runSimulation(timeUnits, percentSprung, maxBalls);
    printReport(timeUnits, percentSprung, maxBalls);

    return 0;
}

void runSimulation(int & timeUnits, double & percentSprung, int & maxBalls){
    Grid<bool> mouseTraps(BOX_SIDE, BOX_SIDE);
    for(int i = 0; i < BOX_SIDE; i++){
        for(int j = 0; j < BOX_SIDE; j++){
            mouseTraps[i][j] = true;
        }
    }
    Vector<int> ballsInAir(INITIAL_BALLS);
    for(int i = 0; i < ballsInAir.size() ; i++) ballsInAir[i] = BOUNCE_MIN;

    timeUnits = 0;
    percentSprung = 0.0;
    maxBalls = INITIAL_BALLS;

    while(ballsInAir.size() != 0){
        timeUnits++;
        for(int i = 0; i < ballsInAir.size(); i++){
            if(ballsInAir[i] > 0) ballsInAir[i]--;
            if(ballsInAir[i] == 0){
                int gridX = randomInteger(0, BOX_SIDE - 1);
                int gridY = randomInteger(0, BOX_SIDE - 1);
                if(mouseTraps[gridX][gridY]){
                    for(int i = 0; i < BALLS_ON_TRAP; i++)
                        ballsInAir.add(randomInteger(BOUNCE_MIN, BOUNCE_MAX));
                }
                mouseTraps[gridX][gridY] = false;
                ballsInAir.remove(i);
            }
        }
        if(ballsInAir.size() > maxBalls) maxBalls = ballsInAir.size();
    }

    double trues = 0;
    for(int i = 0; i < BOX_SIDE; i++){
        for(int j = 0; j < BOX_SIDE; j++){
            if(mouseTraps[i][j]) trues++;
        }
    }
    percentSprung = (1.0 - (trues / (BOX_SIDE * BOX_SIDE)));

}

void printReport(int timeUnits, double percentSprung, int maxBalls){
    cout << "Time units:       " << setw(5) << timeUnits << endl;
    cout << "Percent sprung:   " << setw(5) << percentSprung * 100 << "%" << endl;
    cout << "Max balls in air: " << setw(5) << maxBalls << endl;
}
