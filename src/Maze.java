import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Maze {

	
//	instance variables you may need
//	a variable storing the graph, a variable storing the id of the starting node, a variable storing the id of the end node
//	a variable storing the read number of coins, maybe even a variable storing the path so far so that you don't perform accidental
//	(and unnecessary cycles).
//	if you maintain nodes on a path in a list, be careful to make a list of GraphNodes,
//	otherwise removal from the list is going to behave in a weird way. 
//	REMEMBER your nodes have a field mark.. maybe that field could be useful to avoid cycles...
//	you may also want to have a variable storing the number of coins you have collected so far

	// the graph of the maze
	private Graph graph;
	 // The starting node
	private int start;
	 // The end node
	private int end;
	 // The number of coins
	private int coins;
	//the path
	private List<GraphNode> path;
	//the number of coins collected
	private int coinsCollected;

	/**
	 * Constructor for the maze
	 * @param inputFile the input file
	 * @throws MazeException if the maze is invalid
	 */

	public Maze(String inputFile) throws MazeException {
//		initialize your graph variable by reading the input file!
//		to maintain your code as clean and easy to debug as possible use the provided private helper method
//		readInput to read the input file and initialize your graph variable
		//initialize the path
		path = new ArrayList<>();
		//initialize the coins collected
		coinsCollected = 0;
		//initialize the graph
		graph = new Graph(1);
		//initialize the start
		start = 0;
		//initialize the end
		end = 0;
		//initialize the coins
		coins = 0;
		//read the input file
		try {
			BufferedReader inputReader = new BufferedReader(new FileReader(new File(inputFile)));
			readInput(inputReader);
		} catch (IOException | GraphException e) {
			throw new MazeException("Error reading maze file");
		}

	}

	/**
	 * Get the graph
	 * @return the graph
	 */
	public Graph getGraph() {
//		return your graph
		return graph;
	}

	/**
	 * Solve the maze
	 * @return the path
	 */
	public Iterator<GraphNode> solve() {
//		simply call your private DFS. If you come up with a different approach that's ok too.
//		remember to always return an Iterator or null
		try {
			return DFS(coins, graph.getNode(start));
		} catch (GraphException e) {
			return null;
		}
	}

	/**
	 * Get the number of coins
	 * @return the number of coins
	 */

	private Iterator<GraphNode> DFS(int k, GraphNode go) throws GraphException {
		//base case
		if (go.getName() == end) {
			path.add(go);
			return path.iterator();
		}
		//mark the node
		go.mark(true);
		//add the node to the path
		path.add(go);
		//get the edges
		Iterator<GraphEdge> edges = graph.incidentEdges(go);
		//iterate over the edges
		while (edges.hasNext()) {
			GraphEdge edge = edges.next();
			//get the node
			GraphNode node = edge.secondEndpoint();
			//check if the node is marked
			if (!node.isMarked()) {
				//check if the node has coins
				if (node.getName() == coins) {
					//collect the coins
					coinsCollected++;
				}
				//check if the node has been visited
				if (!path.contains(node)) {
					//check if the node has been visited
					if (coinsCollected < k) {
						//recursive call
						Iterator<GraphNode> path = DFS(k, node);
						//check if the path is not null
						if (path != null) {
							//return the path
							return path;
						}
					}
				}
			}
		}
		//remove the node from the path
		path.remove(go);
		//unmark the node
		go.mark(false);
		//return null
		return null;
	}

	/**
	 * Get the number of coins
	 * @return the number of coins
	 */

	private void readInput(BufferedReader inputReader) throws IOException, GraphException {
//		Read the values S, A, L, and k
//		pay attention when iterating over the input.. All testing input will be correctly formatted
//		remember to identify the starting and ending rooms
//		The input will have size A + A-1 and L + L-1 because every pair of nodes has its relationship inbetween them in the textual representation!
//		To maintain this method cleaner, you may use the private helper method insertEdge

		//read the number of nodes
		int numNodes = Integer.parseInt(inputReader.readLine());
		//initialize the graph
		graph = new Graph(numNodes);
		//read the starting node
		start = Integer.parseInt(inputReader.readLine());
		//read the ending node
		end = Integer.parseInt(inputReader.readLine());
		//read the number of coins
		coins = Integer.parseInt(inputReader.readLine());
		//read the edges
		for (int i = 0; i < numNodes; i++) {
			//read the line
			String line = inputReader.readLine();
			//split the line
			String[] values = line.split(" ");
			//iterate over the values
			for (int j = 0; j < values.length; j++) {
				//check if the value is not 0
				if (!values[j].equals("0")) {
					//insert the edge
					insertEdge(i, j, Integer.parseInt(values[j]), "");
				}
			}
		}

		//close the reader
		inputReader.close();
	}

	/**
	 * Insert an edge
	 * @param node1 the first node
	 * @param node2 the second node
	 * @param linkType the type of the edge
	 * @param label the label of the edge
	 * @throws GraphException if the edge is invalid
	 */
	private void insertEdge(int node1, int node2, int linkType, String label) throws GraphException {
//		select the nodes and insert the appropriate edge.
//		Remember that the graph is undirected, so you have to insert the edge for both nodes
		//get the first node
		GraphNode firstNode = graph.getNode(node1);
		//get the second node
		GraphNode secondNode = graph.getNode(node2);
		//insert the edge
		graph.insertEdge(firstNode, secondNode, linkType, label);
		//label the edge
		graph.insertEdge(secondNode, firstNode, linkType, label);
		//check if the edge is a coin
		if (linkType == coins) {
			//increment the number of coins
			coinsCollected++;
		}
		//check if the edge is the end
		if (linkType == end) {
			//increment the number of coins
			coinsCollected++;
		}
		//check if the edge is the start
		if (linkType == start) {
			//increment the number of coins
			coinsCollected++;
		}
	}
}
