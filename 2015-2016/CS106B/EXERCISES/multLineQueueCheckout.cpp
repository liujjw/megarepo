#include <iostream>
#include <iomanip>
#include "queue.h"
#include "random.h"
using namespace std;

const double ARRIVAL_PROBABILITY = 0.50;
const int MIN_SERV_TIME = 5;
const int MAX_SERV_TIME = 15;
const int SIMULATION_TIME = 2000;
const int CASHIERS = 10;

void runSimulation(int & nServed, int & totalWait, int & totalLength, int & totalCustomers);
void printReport(int nServed, int totalWait, int totalLength, int totalCustomers);
bool areLinesEmpty(Vector< Queue<int> > cashierLines);
bool haveFreeCashiers(Vector<int> busyTimes);

int main(){
    int nServed, totalWait, totalLength, totalCustomers;
    runSimulation(nServed, totalWait, totalLength, totalCustomers);
    printReport(nServed, totalWait, totalLength, totalCustomers);
    return 0;
}

void runSimulation(int & nServed, int & totalWait, int & totalLength, int & totalCustomers){
    Vector< Queue<int> > cashierLines(CASHIERS);
    Vector<int> busyTimes(CASHIERS); // busy times correspond to lines queue of cashiers index wise
    nServed = 0;
    totalWait = 0;
    totalLength = 0;
    totalCustomers = 0;
    for(int t = 0; t < SIMULATION_TIME; t++){

        if(randomChance(ARRIVAL_PROBABILITY)){
            int shortest = cashierLines[0].size();
            int shortestIndex = 0;
            for(int i = 0; i < cashierLines.size(); i++)
                if(cashierLines[i].size() < shortest){
                    shortestIndex = i;
                    shortest = cashierLines[i].size();
                }
            cashierLines[shortestIndex].enqueue(t);
            totalCustomers++;
        }

        for(int i = 0; i < busyTimes.size(); i++)
            if(busyTimes[i] != 0) busyTimes[i]--;

        if(!areLinesEmpty(cashierLines) && haveFreeCashiers(busyTimes)){
            for(int i = 0; i < cashierLines.size(); i++){
                if(busyTimes[i] == 0 && !(cashierLines[i].isEmpty())){
                    totalWait += t - cashierLines[i].dequeue();
                    nServed++;
                    int timeRemaining = randomInteger(MIN_SERV_TIME, MAX_SERV_TIME);
                    busyTimes[i] = timeRemaining;
                }
            }
        }
        for(int i = 0; i < cashierLines.size(); i++){
            totalLength += cashierLines[i].size();
        }
    }
}

bool areLinesEmpty(Vector< Queue<int> > cashierLines){
    for(int i = 0; i < cashierLines.size(); i++)
        if(!(cashierLines[i].isEmpty())) return false;
    return true;
}


bool haveFreeCashiers(Vector<int> busyTimes){
    for(int i = 0 ; i < busyTimes.size(); i++)
        if(busyTimes[i] == 0) return true;
    return false;
}


void printReport(int nServed, int totalWait, int totalLength, int totalCustomers){
    cout << "Simulation results given the following constants:"
         << endl;
    cout << fixed << setprecision(2);
    cout << "  SIMULATION TIME:  " << setw(4)
         << SIMULATION_TIME << endl;
    cout << "  ARRIVAL_PROBABILITY:  " << setw(7)
         << ARRIVAL_PROBABILITY << endl;
    cout << "  MIN_SERV_TIME:  " << setw(4)
         << MIN_SERV_TIME << endl;
    cout << "  MAX_SERV_TIME:  " << setw(4)
         << MAX_SERV_TIME << endl;
    cout << endl;
    cout << "Customers served:    " << setw(4) << nServed << endl;
    cout << "Total customers:     " << setw(4) << totalCustomers << endl;
    cout << "Average waiting time:" << setw(7) << double(totalWait) / nServed / CASHIERS << endl;
    cout << "Average queue length:" << setw(7) << double(totalLength) / SIMULATION_TIME / CASHIERS << endl;
}
