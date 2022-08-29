#include "trailblazer.h"
#include "pqueue.h"
#include "queue.h"
#include "vector.h"
#include "hashset.h"
using namespace std;

/*
bool inSameCluster(Vertex* v1, Vertex* v2, HashMap<Vertex*, HashSet<Vertex*> >& clusters) {
    // eg a = {a,b,c,k} and k = {k,h,i,a}
    // is a in cluster with h?
    // take a loop through each elem in set
    // check if that elem's set contains a
    if(clusters.get(v1).contains(v2)) {
        return true;
    } else {
        for(Vertex* v : clusters.get(v1)) {
            if(v == v1) continue;
            return inSameCluster(v, v2, clusters);
        }
    }
    return false;
} crashes cuz of too much recursive calls probably*/

/*
bool inSameCluster(Vertex* start, Vertex* finish, Vector<HashSet<Vertex*> >& clusterSet,
                   HashMap<Vertex*, int>& vertexLookupMap) {
    // gets the cluster start is in given its index by the lookup map given the start key
    HashSet<Vertex*> startCluster = clusterSet.get(vertexLookupMap.get(start));
    return (startCluster.contains(finish));
}*/

void makePath(Vector<Vertex*>& pathToBe, Vertex* vertex) {
    Vertex* cur = vertex; // make vector of paths from prev, reverse using a stack
    Stack<Vertex*> reverse;
    while(cur != NULL) { // classic idiom for getting all vertices involved in the path
        reverse.add(cur);
        cur = cur->previous;
    }
    while(!reverse.isEmpty()) { // empty stack into our path vector
        pathToBe.add(reverse.pop());
    }
}

bool isReachableDFS(BasicGraph& graph, Vertex* v1, Vertex* goal,
                                 Vector<Vertex*>& path, Set<Vertex*>& visited) {
    visited.add(v1);
    path.add(v1);
    v1->setColor(GREEN);

    if(v1 == goal) {
        return true;
    } else {
        for(Vertex* neighbor : graph.getNeighbors(v1)) {
            if(!visited.contains(neighbor)) {
                if(isReachableDFS(graph, neighbor, goal, path, visited)) {
                    return true;
                } else {
                    path.remove(path.size() - 1);
                    neighbor->setColor(GRAY);
                }
            }
        }
    }
    return false;
}
Vector<Vertex*> depthFirstSearch(BasicGraph& graph, Vertex* start, Vertex* end) {
    graph.resetData(); // resets state so no leftover state from past searches
    Vector<Vertex*> path;
    Set<Vertex*> visisted;
    if(isReachableDFS(graph, start, end, path, visisted)) { // keeps track of path along the way
        return path;
    } else{
        Vector<Vertex*> noPath;
        return noPath;
    }
}

Vector<Vertex*> breadthFirstSearch(BasicGraph& graph, Vertex* start, Vertex* end) {
    graph.resetData();
    Queue<Vertex*> toExplore;
    Set<Vertex*> visited;
    toExplore.enqueue(start);
    visited.add(start);
    Vector<Vertex*> path;

    while(!toExplore.isEmpty()) {
        Vertex* vertex = toExplore.dequeue();
        vertex->setColor(GREEN);
        if(vertex == end) {
            makePath(path, vertex);
            break;
        }
        for(Vertex* n : graph.getNeighbors(vertex)) {
            if(!visited.contains(n)) {
                visited.add(n);
                toExplore.enqueue(n);
                n->setColor(YELLOW);
                n->previous = vertex;
            }
        }
    }
    return path;
}

Vector<Vertex*> dijkstrasAlgorithm(BasicGraph& graph, Vertex* start, Vertex* end) {
    graph.resetData(); // reset stale state

    Vector<Vertex*> path; // the path if found

    for(Vertex* vertex : graph.getVertexSet()) {
        vertex->cost = POSITIVE_INFINITY; // set every vertex cost to infinity
    }

    start->cost = 0.0; // start cost to be 0
    start->setColor(YELLOW); // color start yellow

    PriorityQueue<Vertex*> pqueue; // a pqueue
    pqueue.enqueue(start, start->cost); // enqueue start with its cost

    Set<Vertex*> visited; // visited
    Set<Vertex*> enqueued; // pqueue not have .contains() method, and accepts duplicate values
    while(!pqueue.isEmpty()) {
        Vertex* vertex = pqueue.dequeue(); // dequeue an element according to its priority/cost
        visited.add(vertex); // add it to visited
        vertex->setColor(GREEN); // set its color to green
        enqueued += visited; // enqueued also keeps track of visited just in case, not necessary tho

        if(vertex == end) { // quit with path if found
            makePath(path, vertex);
            break;
        }

        for(Vertex* neighbor : graph.getNeighbors(vertex)) {
            if(!visited.contains(neighbor)) {
                Edge* edge = graph.getEdge(vertex, neighbor);
                double cost = vertex->cost + edge->cost;

                if(cost < neighbor->cost) { // if cost to neighbor is less than before
                    neighbor->cost = cost; // update cost
                    neighbor->previous = vertex; // set prev for retracing
                    neighbor->setColor(YELLOW);
                    if(!enqueued.contains(neighbor)) {
                        pqueue.add(neighbor, neighbor->cost);
                    } else {
                        pqueue.changePriority(neighbor, neighbor->cost);
                    }
                }
            }
        }
    }
    return path;
}

Vector<Vertex*> aStar(BasicGraph& graph, Vertex* start, Vertex* end) {
    graph.resetData(); // reset stale state

    Vector<Vertex*> path; // the path if found

    for(Vertex* vertex : graph.getVertexSet()) {
        vertex->cost = POSITIVE_INFINITY; // set every vertex cost to infinity
    }

    start->cost = 0.0; // start cost to be 0
    start->setColor(YELLOW); // color start yellow

    PriorityQueue<Vertex*> pqueue; // a pqueue
    pqueue.enqueue(start, heuristicFunction(start, end)); // enqueue start with its heuristic cost

    Set<Vertex*> visited; // visited
    Set<Vertex*> enqueued; // pqueue not have .contains() method, and accepts duplicate values
    while(!pqueue.isEmpty()) {
        Vertex* vertex = pqueue.dequeue(); // dequeue an element according to its priority/cost
        visited.add(vertex); // add it to visited
        vertex->setColor(GREEN); // set its color to green
        enqueued += visited; // enqueued also keeps track of visited just in case, not necessary tho

        if(vertex == end) { // quit with path if found
            makePath(path, vertex);
            break;
        }

        for(Vertex* neighbor : graph.getNeighbors(vertex)) {
            if(!visited.contains(neighbor)) {
                Edge* edge = graph.getEdge(vertex, neighbor);
                double cost = vertex->cost + edge->cost;

                if(cost < neighbor->cost) { // if cost to neighbor is less than before
                    neighbor->cost = cost; // update cost
                    neighbor->previous = vertex; // set prev for retracing
                    neighbor->setColor(YELLOW);
                    if(!enqueued.contains(neighbor)) {
                        pqueue.add(neighbor, neighbor->cost +
                                   heuristicFunction(neighbor, end));
                    } else {
                        pqueue.changePriority(neighbor, neighbor->cost +
                                              heuristicFunction(neighbor, end));
                    }
                }
            }
        }
    }
    return path;
}

Set<Edge*> kruskal(BasicGraph& graph) {
    /*1. Place each vertex into its own "cluster" (group of reachable vertices).*/
    // use a graph and kthlevelfriends (section problem from section 7, week 8)
    // a graph is just an adjacency list/map that maps vertices to neighbors
    // find all kth level friends (deepest level friends) to see if they are in the same cluster
    // this way, we can only need to store neighbors in graph, then kthlvel will
    // deal with clusters
    // then in our map, all we need to do is add the start to finish and
    // finish to start and now since they share each other as neighbors
    // so now wont literally store neighbors but still should work when set<edge> processed??
    // WAIT? do we even need kth level friends? trace things thru boi
    // yes: say we wan to  merge Vertex a = {a, b, c} with Vertex k = {k, h, i}
    // we'll add a to k and k to a, so that a = {a,b,c,k} and k = {k,h,i,a}, now if we run
    // kth level friends, we'll look at ... every element and see if there's connectivity <- O(N)
    // between the start and the elems of the elems in the list
    // but wait since we use hashset, only one constant search is performed! <- actually deserved excalamation
    // two solutions to still problem of imporper cluster representation:
    // either copy every start's "neighbors" etc etc.. copying me no like
    // recursive kthlevelfriends like function like we talked about
    // but hey isnt copying faster and simpler than trying to recurse and loop over everything?
    // nope we just completely derailed and went back to flawed adt at start where
    // improper clustering
    // back to sqaure 1 recrusion is too inefficient cuz it recurs for each elem of each elem, etc etc
    // huge problems arise when we use vertices as the means to which we access clusters
    // every elem of the cluster will have to have realtionship cluster with other one, and its elems,
    // and their elems and so on, even with a minute allowed for run time  th eprogram will crash
    // maybe move away from vertices as names and just use generic groupings of clusters
    // and iterate through them:

    // make a vector of sets of vertices
    // add each elem to the set then add that set to the vector (possible huge vector btdubs)
        // maybe a set of maps where we can hash to a vertex that tells us where it lives in a cluster
    // look for sets in which start lives
    // if finish isnt in that set
    // prepare to MERGE
    // find set where finsih lives and combine
    /*
    HashMap<Vertex*, HashSet<Vertex*> > clusters;
    for(Vertex* v : graph.getVertexSet()) {
        HashSet<Vertex*> cluster;
        cluster.add(v); // each cluster contains itself
        clusters.put(v, cluster);
    }*/

    HashMap<Vertex*, int> vertexLookupMap; // speeds up lookup gives index of where vector is
    Vector<HashSet<Vertex*> > clusterSets; // vector type gives instanteous lookup given an index
    for(Vertex* v : graph.getVertexSet()) {
        HashSet<Vertex*> cluster;
        cluster.add(v);
        clusterSets.add(cluster);
        vertexLookupMap.put(v, clusterSets.size() - 1);
    }


    /*2. Put all edges into a priority queue, using weights as priorities.*/
    PriorityQueue<Edge*> edgeQueue;
    for(Edge* edge : graph.getEdgeSet()) {
        edgeQueue.enqueue(edge, edge->cost);
    }
    /*3. While there are two or more separate clusters remaining:
        o Dequeue an edge e from the priority queue.
        o If the start and finish vertices of e are not in the same cluster:
             Merge the clusters containing the start and finish vertices of e.
             Add e to your spanning tree.
        o Else:
             Do not add e to your spanning tree.*/
    Set<Edge*> MSTedges;
    int remainingClusters = clusterSets.size();
    while(remainingClusters > 1) { // merging should reduce cluster vector each time
        Edge* edge = edgeQueue.dequeue();
        Vertex* start = edge->start;
        Vertex* finish = edge->finish;

        if(vertexLookupMap.get(start) != vertexLookupMap.get(finish)) { // if start and finish arent in same index in vector
            remainingClusters -= 1;
            MSTedges += edge;
            // merge set that finish is in to start's set
            HashSet<Vertex*> mergedCluster = clusterSets.get(vertexLookupMap.get(start));
            mergedCluster += clusterSets.get(vertexLookupMap.get(finish));
            clusterSets.set(vertexLookupMap.get(start), mergedCluster);
            // update lookup map, all elements within it must also be changed
            // save finish's index so can be changed later
            int finishIndex = vertexLookupMap.get(finish);
            for(Vertex* n : clusterSets.get(vertexLookupMap.get(finish))) {
                vertexLookupMap.put(n, vertexLookupMap.get(start));
            }
            // remove takes O(N) so using .size() of vector means many o(N) operations to remove things
            // faster would be to set it to empty, same effect almost
            HashSet<Vertex*> emptyCluster;
            clusterSets.set(finishIndex, emptyCluster);

        }
    }
    /*4. Once the while loop terminates, your spanning tree is complete.*/
    return MSTedges;
}
