import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Graph implements GraphADT {
	
//	Create an adjacency list or an adjacency matrix, a list is probably easier
	
	public Graph(int n) {
//		initialize your representation with empty adjacency lists
	}
	
	@Override
	public void insertEdge(GraphNode nodeu, GraphNode nodev, int type, String label) throws GraphException {
//		create and insert the edge
//		REMEMBER, an edge is accessible from both endpoints, so make sure you add it as an edge for both end nodes		
	}

	@Override
	public GraphNode getNode(int u) throws GraphException {
//		Return the node with the appropriate name
	}

	@Override
	public Iterator<GraphEdge> incidentEdges(GraphNode u) throws GraphException {
//		Select from your adjacency list the appropriate Node and return an iterator over the collection.
//		Usually a call to .iterator() should work, unless you do something really exotic
	}

	@Override
	public GraphEdge getEdge(GraphNode u, GraphNode v) throws GraphException {
//		check if those nodes exist, then check if they have edges, then who has the least number of edges.
//		find the appropriate edge and return it, if no such edge exists remember to return null 
//		there are faster ways too ;)
	}

	@Override
	public boolean areAdjacent(GraphNode u, GraphNode v) throws GraphException {
//		maybe you could use a previously written method to solve this one quickly...
	}

}
