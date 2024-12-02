import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Maze {
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
		//initialize the path
		path = new ArrayList<>();
		//initialize the coins collected
		coinsCollected = 0;
		try {
			BufferedReader inputReader = new BufferedReader(new FileReader(new File(inputFile)));
			readInput(inputReader);
		} catch (IOException | GraphException e) {
			throw new MazeException("Error reading maze file");
		}

	}

	/**
	 * returns the graph
	 * @return the graph
	 */
	public Graph getGraph() {
//		return your graph
		return graph;
	}

	/**
	 * Solve the maze using DFS
	 * @return the path iterator or null if no path was found
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
	 * Depth first search to solve the maze
	 */

	private Iterator<GraphNode> DFS(int k, GraphNode currNode) throws GraphException {
		//base case
		if (currNode.getName() == end) {
			path.add(currNode);
			return path.iterator();
		}
		//mark the node
		currNode.mark(true);
		//add the node to the path
		path.add(currNode);
		//get the edges
		Iterator<GraphEdge> edges = graph.incidentEdges(currNode);
		//iterate over the edges
		while (edges != null && edges.hasNext()) {
			GraphEdge edge = edges.next();
			//get the nextNode
			GraphNode nextNode = edge.secondEndpoint();
			//check if the nextNode is marked
			if (!nextNode.isMarked()) {
				int coinsNeeded = edge.getType();
				if (coinsNeeded <= k) {
					//recursive call
					Iterator<GraphNode> result = DFS(k - coinsNeeded, nextNode);
					if (result != null) {
						return result;
					}
					k += coins; //add the coins back when backtracking
				}
			}
		}
		//remove the node from the path
		path.remove(path.size() - 1);
		//unmark the node
		currNode.mark(false);
		//return null
		return null;
	}

	/**
	 * Read the input
	 * and initialize the graph
	 */

	private void readInput(BufferedReader inputReader) throws IOException, GraphException {
//		Read the values S, A, L, and k
//		pay attention when iterating over the input.. All testing input will be correctly formatted
//		remember to identify the starting and ending rooms
//		The input will have size A + A-1 and L + L-1 because every pair of nodes has its relationship inbetween them in the textual representation!
//		To maintain this method cleaner, you may use the private helper method insertEdge
		//read the number of nodes
		int scaleFactor = Integer.parseInt(inputReader.readLine().trim());
		int width = Integer.parseInt(inputReader.readLine().trim());
		int length = Integer.parseInt(inputReader.readLine().trim());
		coins = Integer.parseInt(inputReader.readLine().trim());

		graph = new Graph(width * length);
		String[] mazeLines = inputReader.lines().toArray(String[]::new);

		for (int row = 0; row < mazeLines.length; row += 2) {
			String rooms = mazeLines[row];
			if (row + 1 < mazeLines.length) {
				String walls = mazeLines[row + 1];

				for (int col = 0; col < rooms.length(); col++) {
					char roomChar = rooms.charAt(col);
					handleRoom(roomChar, row / 2, col, width);

					if (col < walls.length()) {
						char wallChar = walls.charAt(col);
						handleEdge(roomChar, wallChar, row / 2, col, width);
					}
				}
			}
		}
	}

	/*
	 * Handle the room
	 * @param roomChar the room character
	 * @param row the row
	 * @param col the column
	 * @param width the width
	 * @throws GraphException if the room is invalid
	 */
	private void handleRoom(char roomChar, int row, int col, int width) throws GraphException {
		int nodeIndex = row * width + col;
		switch (roomChar) {
			case 's':
				start = nodeIndex;
				break;
			case 'x':
				end = nodeIndex;
				break;
			default:
				break;
		}
	}

	/**
	 * Handle the edge
	 * @param roomChar the room character
	 * @param wallChar the wall character
	 * @param row the row
	 * @param col the column
	 * @param width the width
	 * @throws GraphException if the edge is invalid
	 */
	private void handleEdge(char roomChar, char wallChar, int row, int col, int width) throws GraphException {
		int nodeIndex = row * width + col;
		int neighbourIndex = nodeIndex + 1;
		if (Character.isDigit(wallChar)) {
			int coinsNeeded = Character.getNumericValue(wallChar);
			insertEdge(nodeIndex, neighbourIndex,coinsNeeded,"door");
		}
		else {
			insertEdge(nodeIndex, neighbourIndex,0,"corridor");
		}
	}
	/**
	 * Insert an edge
	 * @param node1 the first node
	 * @param node2 the second node
	 * @param edgeType the type of the edge
	 * @param label the label of the edge
	 * @throws GraphException if the edge is invalid
	 */
	private void insertEdge(int node1, int node2, int edgeType, String label) throws GraphException {
		GraphNode u = graph.getNode(node1);
		GraphNode v = graph.getNode(node2);
		graph.insertEdge(u, v, edgeType, label);
	}
}
