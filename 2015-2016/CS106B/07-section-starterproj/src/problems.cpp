/*
 * CS 106B Section 07 Problems
 */
#include <iostream>
#include <string>
#include "queue.h"
#include "console.h"
#include "basicgraph.h"
using namespace std;

void testKthLevelFriends(); // section problem 5
void testIsReachable(); // section problem 6
void testIsConnected(); // section problem 7
void testFindMinimumVertexCover(); // section problem 8

Set<Vertex*> kthLevelFriends(BasicGraph& graph, Vertex* v, int k);
bool isReachableDFS(BasicGraph& graph, Vertex* v1, Vertex* v2);
bool isReachableBFS(BasicGraph& graph, Vertex* v1, Vertex* v2);
bool isConnected(BasicGraph& graph);
Set<Vertex*> findMinimumVertexCover(BasicGraph& graph);
void kthLevelFriends(BasicGraph& graph, int k, Set<Vertex*>& friends);

int main() {
    setConsoleSize(750, 500);
    setConsoleFont("Monospaced-Bold-14");
    setConsoleEcho(true);

    // run tests
    testKthLevelFriends();
    testIsReachable();
    testIsConnected();
    testFindMinimumVertexCover();

    return 0;
}


void testKthLevelFriends() {
    BasicGraph graph;
    graph.addVertex("Marty");
    graph.addVertex("Mehran");
    graph.addVertex("Cynthia");
    graph.addVertex("Eric");
    graph.addVertex("Keith");
    graph.addVertex("Patrick");
    graph.addEdge("Marty", "Cynthia");
    graph.addEdge("Marty", "Mehran");
    graph.addEdge("Eric", "Mehran");
    graph.addEdge("Cynthia", "Keith");

    cout << "kthLevelFriends:" << endl;
    Set<Vertex*> friends = kthLevelFriends(graph, graph.getVertex("Marty"), 1);
    Set<Vertex*> friendsOfFriends = kthLevelFriends(graph, graph.getVertex("Marty"), 2);
    for(Vertex* v : friends) {
        cout << "k = 1 friend: " << v->name << endl;
    }
    cout << "k = 1 friends above should be {Mehran, Cynthia}" << endl;
    for(Vertex* v : friendsOfFriends) {
        cout << "k = 2 friend: " << v->name << endl;
    }
    cout << "k = 2 friends above should be {Keith}" << endl;
}


void testIsReachable() {
    BasicGraph graph;
    graph.addVertex("a");
    graph.addVertex("b");
    graph.addVertex("c");
    graph.addVertex("d");
    graph.addEdge("a", "b");
    graph.addEdge("b", "d");
    graph.addEdge("d", "c");
    cout << "isReachable:" << endl;
    cout << boolalpha << isReachableBFS(graph, graph.getVertex("a"), graph.getVertex("d")) << " should be true" << endl;
    cout << boolalpha << isReachableBFS(graph, graph.getVertex("c"), graph.getVertex("a")) << " should be false" << endl;
    cout << boolalpha << isReachableBFS(graph, graph.getVertex("a"), graph.getVertex("a")) << " should be true" << endl;
}


void testIsConnected() {
    BasicGraph graph;
    cout << "isConnected:" << endl;
    cout << boolalpha << isConnected(graph) << " should be true" << endl;
    graph.addVertex("a");
    graph.addVertex("b");
    graph.addVertex("c");
    graph.addEdge("a", "b");
    graph.addEdge("b", "c");
    cout << boolalpha << isConnected(graph) << " should be false" << endl;
    graph.addEdge("c", "a");
    cout << boolalpha << isConnected(graph) << " should be true" << endl;
}


void testFindMinimumVertexCover() {
    BasicGraph graph;
    graph.addVertex("a");
    graph.addVertex("b");
    graph.addVertex("c");
    graph.addVertex("d");
    graph.addVertex("e");
    graph.addEdge("a", "b");
    graph.addEdge("b", "a");
    graph.addEdge("a", "c");
    graph.addEdge("c", "a");
    graph.addEdge("a", "d");
    graph.addEdge("d", "a");
    graph.addEdge("a", "e");
    graph.addEdge("e", "a");

    cout << "findMinimumVertexCover:" << endl;
    Set<Vertex*> cover1 = findMinimumVertexCover(graph);
    graph.addEdge("d", "e");
    graph.addEdge("e", "d");
    Set<Vertex*> cover2 = findMinimumVertexCover(graph);
    for (Vertex* v : cover1) {
        cout << v->name << endl;
    }
    cout << "vertices above should be {a}" << endl;
    for (Vertex* v : cover2) {
        cout << v->name << endl;
    }
    cout << "vertices above should be {a, e} or {a, d}" << endl;
}


Set<Vertex*> kthLevelFriends(BasicGraph& graph, Vertex* v, int k) {
    Set<Vertex*> friends;
    friends += v;
    kthLevelFriends(graph, k, friends);
    return friends;
}
void kthLevelFriends(BasicGraph& graph, int k, Set<Vertex*>& friends) {
    if(k == 0) {
        return;
    } else { // k > 1
        Set<Vertex*> tmp;
        tmp = friends;
        for(Vertex* person : tmp) {
            friends -= person;
            friends += graph.getNeighbors(person);
        }
        kthLevelFriends(graph, k - 1, friends);
    }
}


bool isReachableDFS(BasicGraph& graph, Vertex* v1, Vertex* v2) {
    Set<Vertex*> visited;
    visited += v1;
    if(v1 == v2) {
        return true;
    }
    Set<Vertex*> neighbors;
    neighbors += graph.getNeighbors(v1);
    for(Vertex* person : neighbors) {
        if(person->visited == false && isReachableDFS(graph, person, v2)) {
            return true;
        }
    }
    return false;
}

bool isReachableBFS(BasicGraph& graph, Vertex* v1, Vertex* v2) {
    Queue<Vertex*> toExplore;
    Set<Vertex*> visited;
    visited += v1;
    toExplore.enqueue(v1);
    while(!toExplore.isEmpty()) {
        Vertex* person = toExplore.dequeue();
        if(person == v2) {
            return true;
        }
        for(Vertex* n : graph.getNeighbors(person)) {
            if(!visited.contains(n)) {
                toExplore.enqueue(n);
                visited += n;
            }
        }
    }
    return false;
}


bool isConnected(BasicGraph& graph) {
    for(Vertex* vertex1 : graph.getVertexSet()) {
        for(Vertex* vertex2 : graph.getVertexSet()) {
            if(vertex1 != vertex2 && !(isReachableBFS(graph, vertex1, vertex2))) {
                return false;
            }
        }
    }
    return true;
}


Set<Vertex*> findMinimumVertexCover(BasicGraph& graph) {
    // TODO: finish me!
    return {};
}

