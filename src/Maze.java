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
	private int start = -1;
	 // The end node
	private int end = -1;
	 // The number of coins
	private int coins;
	//the path
	private List<GraphNode> path;
	/**
	 * Constructor for the maze
	 * @param inputFile the input file
	 * @throws MazeException if the maze is invalid
	 */

	public Maze(String inputFile) throws MazeException {
		//initialize the path
		path = new ArrayList<>();
		try {
			BufferedReader inputReader = new BufferedReader(new FileReader(new File(inputFile)));
			readInput(inputReader);
			if (start == -1 || end == -1) {
				throw new MazeException("Invalid maze: missing start or end");
			}

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
	 * Solve the maze and returns an iterator
	 * @return the path iterator or null if no path was found
	 */
	public Iterator<GraphNode> solve() {

		try {
			System.out.println("Starting Dfs from node " + start + " with " + coins + " coins");
			if(DFS(coins, graph.getNode(start))){
				System.out.println("Path found");
				return path.iterator();
			}
			System.out.println("No path found");
		} catch (GraphException e) {
			System.err.println("Error during solving: " + e.getMessage());
		}
		return null;
	}

	/**
	 * Depth-first search to find a path from start to end within the coin constraint.
	 * @param k the number of coins remaining
	 * @param currNode the current node
	 * @return true if a path is found, false otherwise
	 * @throws GraphException if graph operations fail
	 */
	private boolean DFS(int k, GraphNode currNode) throws GraphException {
		if (currNode.getName() == end) { // Base case: reached the end
			path.add(currNode);
			return true;
		}

		currNode.mark(true); // Mark the node as visited
		path.add(currNode); // Add node to the current path

		Iterator<GraphEdge> edges = graph.incidentEdges(currNode);
		while (edges != null && edges.hasNext()) {
			GraphEdge edge = edges.next();
			GraphNode nextNode = edge.firstEndpoint().equals(currNode) ? edge.secondEndpoint() : edge.firstEndpoint();

			if (!nextNode.isMarked()) {
				int coinsNeeded = edge.getType();
				System.out.println("Checking edge from " + nextNode.getName() + " with " + coinsNeeded + " coins");
				if (coinsNeeded <= k) {
					if (DFS(k - coinsNeeded, nextNode)) {
						return true; // Path found
					}
				}
			}
		}
		System.out.println("backtracking from node: " + currNode.getName());
		path.remove(path.size() - 1); // Backtrack
		currNode.mark(false); // Unmark the node

		return false; // No path found from this node
	}

	/**
	 * Reads the maze input file and constructs the graph.
	 * @param inputReader buffered reader for the input file
	 * @throws IOException if there is an error reading the file
	 * @throws GraphException if graph operations fail
	 */

	private void readInput(BufferedReader inputReader) throws IOException, GraphException {
		//read the number of nodes
		int width = Integer.parseInt(inputReader.readLine().trim());
		int length = Integer.parseInt(inputReader.readLine().trim());
		coins = Integer.parseInt(inputReader.readLine().trim());
		System.out.println("Width: " + width + ", Length: " + length + ", Coins: " + coins);

		// Read the rest of the maze lines
		List<String> mazeLinesList = new ArrayList<>();
		String line;
		while ((line = inputReader.readLine()) != null) {
			mazeLinesList.add(line);
		}

		String[] mazeLines = mazeLinesList.toArray(new String[0]);

		// Dynamically calculate the width from the first room line
		if (mazeLines.length > 0) {
			width = mazeLines[0].length();
		}
		System.out.println("dynamic width: " + width);

		graph = new Graph(width * length);

		for (int row = 0; row < mazeLines.length; row += 2) {
			String rooms = mazeLines[row];
			System.out.println("Rooms: " + rooms);
			if (row + 1 < mazeLines.length) {
				String walls = mazeLines[row + 1];
				System.out.println("Walls: " + walls);
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

	/**
	 * Processes a room character and identifies the start and end nodes.
	 * @param roomChar the character representing the room
	 * @param row the row of the room
	 * @param col the column of the room
	 * @param width the width of the maze
	 */
	private void handleRoom(char roomChar, int row, int col, int width) {
		int nodeIndex = row * width + col;
		System.out.println("Handling room: " + roomChar + " at node index " + nodeIndex);
		if (roomChar == 's') start = nodeIndex;
		if (roomChar == 'x') end = nodeIndex;
	}

	/**
	 * Handles edge creation based on the wall character.
	 * @param roomChar the character of the room
	 * @param wallChar the character of the wall or door
	 * @param row the row of the room
	 * @param col the column of the room
	 * @param width the width of the maze
	 * @throws GraphException if edge creation fails
	 */
	private void handleEdge(char roomChar, char wallChar, int row, int col, int width) throws GraphException {
		int nodeIndex = row * width + col;
		int neighbourIndex = nodeIndex + 1;
		//System.out.println("Handling edge between " + nodeIndex + " and " + neighbourIndex + " with wallChar: " + wallChar);
		if(wallChar == 'o'){
			//skip "o" as its not a valid wall/door
			System.out.println("Skipping edge with wallChar: " + wallChar);
			return;
		}

		if (wallChar == 'c') {
			insertEdge(nodeIndex, neighbourIndex, 0, "corridor");
		} else if (Character.isDigit(wallChar)) {
			insertEdge(nodeIndex, neighbourIndex, Character.getNumericValue(wallChar), "door");
		}
	}
	/**
	 * Inserts an edge between two nodes in the graph.
	 * @param node1 the first node
	 * @param node2 the second node
	 * @param edgeType the type of the edge
	 * @param label the label of the edge
	 * @throws GraphException if edge creation fails
	 */
	private void insertEdge(int node1, int node2, int edgeType, String label) throws GraphException {
		System.out.println("Inserting edge between " + node1 + " and " + node2 + " with type: " + edgeType);
		GraphNode u = graph.getNode(node1);
		GraphNode v = graph.getNode(node2);
		graph.insertEdge(u, v, edgeType, label);
	}
}
