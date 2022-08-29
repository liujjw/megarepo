bool isCyclic(BasicGraph& graph) {
	graph.resetData();
	Set<Vertex*> visited;
	for(Vertex* v : graph.getVertexSet()) {
		if(visit(graph, v, v, visited)) {
			return true;
		}
	}
	return false;
}

bool visit(BasicGraph& graph, Vertex* vertexToLoopBack, Vertex* cur, Set<Vertex*>& visited) {
	for(Vertex* neighbor : graph.getNeighbors(cur)) {
		if(!visited.contains(neighbor)) {
			if(vertexToLoopBack == neighbor) {
				return true;
			} else if(visit(graph, vertexToLoopBack, neighbor, visited)) {
				return true;
			}
		}
	}
	return false;
}


bool isCyclic(BasicGraph& graph) { graph.resetData();
Map<Node*, string> mark;
for (Node* v : graph.getNodeSet()) {
mark[v] = "UNVISITED"; }
for (Node* v : graph.getNodeSet()) {
if (isCyclicHelper(graph, mark, v)) {
return true; }
}
return false; }
bool isCyclicHelper(BasicGraph& graph, Map<Node*, string>& mark, Node* v) { mark[v] = "PARTIAL";
for (Arc* edge : graph.getArcSet(v)) {
if (!edge->visited) {
edge->visited = true;
Node* neighbor = edge->finish;
if (mark[neighbor] == "PARTIAL") {
return true;
} else if (mark[neighbor] == "UNVISITED") {
if (isCyclicHelper(graph, mark, neighbor)) { return true;
} }
= "VISITED"; return false;
}