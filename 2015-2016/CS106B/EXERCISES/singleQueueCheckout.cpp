#include <iostream>
#include <iomanip>
#include "queue.h"
#include "random.h"
using namespace std;

const double ARRIVAL_PROBABILITY = 0.50;
const int MIN_SERV_TIME = 5;
const int MAX_SERV_TIME = 15;
const int SIMULATION_TIME = 2000;
const int CASHIERS = 6;

void runSimulation(int & nServed, int & totalWait, int & totalLength, int & totalCustomers);
void printReport(int nServed, int totalWait, int totalLength, int totalCustomers);
bool ifAllBusy(Vector<int> cashiers);

int main(){
    int nServed, totalWait, totalLength, totalCustomers;
    runSimulation(nServed, totalWait, totalLength, totalCustomers);
    printReport(nServed, totalWait, totalLength, totalCustomers);
    return 0;
}

bool ifAllBusy(Vector<int> cashiers){
    for(int i = 0; i < cashiers.size(); i++)
        if(cashiers[i] == 0) return false;
    return true;
}

void runSimulation(int & nServed, int & totalWait, int & totalLength, int & totalCustomers){
    Queue<int> queue;
    nServed = 0;
    totalWait = 0;
    totalLength = 0;
    totalCustomers = 0;
    Vector<int> cashiers(CASHIERS);

    for(int t = 0; t < SIMULATION_TIME; t++){
        if(randomChance(ARRIVAL_PROBABILITY)){
            queue.enqueue(t);
            totalCustomers++;
        } 
        for(int i = 0; i < cashiers.size(); i++){
            if(cashiers[i] != 0) cashiers[i]--;
        }
        if(!queue.isEmpty() && !(ifAllBusy(cashiers))){
            totalWait += t - queue.dequeue();
            nServed++;
            int timeRemaining = randomInteger(MIN_SERV_TIME, MAX_SERV_TIME);
            for(int i = 0; i < cashiers.size(); i++){
                if(cashiers[i] == 0){
                    cashiers[i] = timeRemaining;
                    break;
                }
            }
        }
        totalLength += queue.size();
    }
}



void printReport(int nServed, int totalWait, int totalLength, int totalCustomers){
    cout << "Simulation results given the following constants:"
         << endl;
    cout << fixed << setprecision(2);
    cout << "  SIMULATION TIME:  " << setw(4)
         << SIMULATION_TIME << endl;
    cout << "  CASHIERS:  " << setw(4)
         << CASHIERS << endl;
    cout << "  ARRIVAL_PROBABILITY:  " << setw(7)
         << ARRIVAL_PROBABILITY << endl;
    cout << "  MIN_SERV_TIME:  " << setw(4)
         << MIN_SERV_TIME << endl;
    cout << "  MAX_SERV_TIME:  " << setw(4)
         << MAX_SERV_TIME << endl;
    cout << endl;
    cout << "Customers served:    " << setw(4) << nServed << endl;
    cout << "Total customers:     " << setw(4) << totalCustomers << endl; // might be off by one since simulation ends at a certain time
    cout << "Average waiting time:" << setw(7) << double(totalWait) / nServed << endl;
    cout << "Average queue length:" << setw(7) << double(totalLength) / SIMULATION_TIME << endl;
}
