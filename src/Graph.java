import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Graph implements GraphADT {
	
//	Create an adjacency list or an adjacency matrix, a list is probably easier
	/**
	 *
	 * The nodes of the graph
	 */
	private Map<Integer, GraphNode> nodes = new HashMap<>();
	/**
	 * The edges of the graph
	 */
	private Map<Integer, List<GraphEdge>> edges = new HashMap<>();

	/**
	 * Constructor for the graph
	 * @param n the number of nodes
	 */
	public Graph(int n) {
//		initialize your representation with empty adjacency lists
		//initialize the nodes
		for (int i = 0; i < n; i++) {
			nodes.put(i, new GraphNode(i));
			edges.put(i, new ArrayList<>());
		}
	}
	/**
	 * Insert an edge into the graph
	 * @param nodeu the first node
	 * @param nodev the second node
	 * @param type the type of the edge
	 * @param label the label of the edge
	 * @throws GraphException if the edge is invalid
	 */
	@Override
	public void insertEdge(GraphNode nodeu, GraphNode nodev, int type, String label) throws GraphException {
//		create and insert the edge
//		REMEMBER, an edge is accessible from both endpoints, so make sure you add it as an edge for both end nodes
	//create the edge

		//check if the nodes exist
		if (!nodes.containsKey(nodeu.getName()) || !nodes.containsKey(nodev.getName())) {
			throw new GraphException("Node does not exist");
		}
		GraphEdge edge = new GraphEdge(nodeu, nodev, type, label);
		//add the edge to the list for the first node
		edges.get(nodeu.getName()).add(edge);
		//add the edge to the list for the second node
		edges.get(nodev.getName()).add(edge);

	}
	/**
	 * Get a node from the graph
	 * @param u the name of the node
	 * @return the node
	 * @throws GraphException if the node does not exist
	 */
	@Override
	public GraphNode getNode(int u) throws GraphException {
//		Return the node with the appropriate name
		if (!nodes.containsKey(u)) {
			throw new GraphException("Node does not exist");
		}
		return nodes.get(u);
	}

	/**
	 * Get the edges incident on a node
	 * @param u the node
	 * @return the edges incident on the node
	 * @throws GraphException if the node does not exist
	 */
	@Override
	public Iterator<GraphEdge> incidentEdges(GraphNode u) throws GraphException {
		if (!nodes.containsKey(u.getName())) {
			throw new GraphException("Node does not exist");
		}
		return edges.get(u.getName()).iterator();
	}

	/**
	 * Get an edge between two nodes
	 * @param u the first node
	 * @param v the second node
	 * @return the edge between the nodes
	 * @throws GraphException if the edge does not exist
	 */
	@Override
	public GraphEdge getEdge(GraphNode u, GraphNode v) throws GraphException {
		if (!nodes.containsKey(u.getName()) || !nodes.containsKey(v.getName())) {
			throw new GraphException("Node does not exist");
		}
		for (GraphEdge edge : edges.get(u.getName())) {
			if (edge.firstEndpoint().equals(v) || edge.secondEndpoint().equals(v)) {
				return edge;
			}
		}
		return null;
	}
	/**
	 * Check if two nodes are adjacent
	 * @param u the first node
	 * @param v the second node
	 * @return true if the nodes are adjacent, false otherwise
	 * @throws GraphException if the nodes do not exist
	 */

	@Override
	public boolean areAdjacent(GraphNode u, GraphNode v) throws GraphException {
//		maybe you could use a previously written method to solve this one quickly...
		if (!nodes.containsKey(u.getName()) || !nodes.containsKey(v.getName())) {
			throw new GraphException("Node does not exist");
		}
		for (GraphEdge edge : edges.get(u.getName())) {
			if (edge.firstEndpoint().equals(v) || edge.secondEndpoint().equals(v)) {
				return true;
			}
		}
		return false;
	}

}
