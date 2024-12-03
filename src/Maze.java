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
		return graph;
	}

	/**
	 * Solve the maze using DFS
	 * @return the path iterator or null if no path was found
	 */
	public Iterator<GraphNode> solve() {
		try {
			System.out.println("Starting solve...");
			Iterator<GraphNode> solution = dfs(coins, graph.getNode(start));
			if (solution != null) {
				System.out.println("Solution found!");
			} else {
				System.out.println("No solution exists.");
			}
			return solution;
		} catch (GraphException e) {
			System.out.println("GraphException occurred: " + e.getMessage());
			return null;
		}
	}


	/**
	 * Depth first search to solve the maze
	 */

	private Iterator<GraphNode> dfs(int remainingCoins, GraphNode currentNode) throws GraphException {
		// Debugging: Log the current state
		System.out.println("Visiting node: " + currentNode.getName() + ", Remaining coins: " + remainingCoins);
		System.out.println("Current path: " + path);

		// Base case: If the end node is reached
		if (currentNode.getName() == end) {
			path.add(currentNode);
			System.out.println("End node reached! Path: " + path);
			return path.iterator();
		}

		// Mark the current node as visited and add it to the path
		currentNode.mark(true);
		path.add(currentNode);

		// Iterate over all edges connected to the current node
		Iterator<GraphEdge> edges = graph.incidentEdges(currentNode);
		while (edges != null && edges.hasNext()) {
			GraphEdge edge = edges.next();

			// Determine the next node based on the edge
			GraphNode nextNode = edge.secondEndpoint();
			if (nextNode == currentNode) {
				nextNode = edge.firstEndpoint(); // Handle undirected edge
			}

			// Debugging: Log the edge and next node
			int coinsNeeded = edge.getType();
			System.out.println("Considering edge to node: " + nextNode.getName() + ", Coins needed: " + coinsNeeded);

			// Skip already visited nodes
			if (!nextNode.isMarked() && coinsNeeded <= remainingCoins) {
				System.out.println("Traversing edge to node: " + nextNode.getName() + ", Coins needed: " + coinsNeeded);

				// Recursively attempt to solve from the next node
				Iterator<GraphNode> result = DFS(remainingCoins - coinsNeeded, nextNode);
				if (result != null) return result;

				// Restore coins if backtracking
				System.out.println("Backtracking from node: " + nextNode.getName());
				remainingCoins += coinsNeeded;
			}
		}

		// Backtrack: remove the current node from the path and unmark it
		System.out.println("Removing node from path: " + currentNode.getName());
		path.remove(path.size() - 1);
		currentNode.mark(false);

		// If no solution is found, return null
		return null;
	}




	/**
	 * Read the input
	 * and initialize the graph
	 */

	private void readInput(BufferedReader inputReader) throws IOException, GraphException {
		int scaleFactor = Integer.parseInt(inputReader.readLine().trim());
		int width = Integer.parseInt(inputReader.readLine().trim());
		int length = Integer.parseInt(inputReader.readLine().trim());
		coins = Integer.parseInt(inputReader.readLine().trim());

		graph = new Graph(width * length);
		String[] mazeLines = inputReader.lines().toArray(String[]::new);

		System.out.println("Reading maze input...");

		for (int row = 0; row < mazeLines.length; row += 2) {
			String rooms = mazeLines[row];
			System.out.println("Processing row: " + row + " -> " + rooms); // Debugging

			if (row + 1 < mazeLines.length) { // Check for corresponding wall row
				String walls = mazeLines[row + 1];
				System.out.println("Processing walls: " + (row + 1) + " -> " + walls); // Debugging

				for (int col = 0; col < rooms.length(); col++) {
					char roomChar = rooms.charAt(col);
					handleRoom(roomChar, row / 2, col, width);

					if (col < walls.length()) {
						char wallChar = walls.charAt(col);
						handleEdge(roomChar, wallChar, row / 2, col, width);
					}
				}
			} else { // Handle the last row if it doesn't have walls
				for (int col = 0; col < rooms.length(); col++) {
					char roomChar = rooms.charAt(col);
					handleRoom(roomChar, row / 2, col, width); // Only process rooms
				}
			}
		}

		System.out.println("Final check: Start node = " + start + ", End node = " + end);
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
		int nodeIndex = row * width + col; // Calculate the node index based on grid position
		System.out.println("Parsing room: " + roomChar + " at index: " + nodeIndex); // Debugging

		switch (roomChar) {
			case 's': // Start node
				start = nodeIndex;
				System.out.println("Start node found at index: " + start);
				break;
			case 'x': // End node
				end = nodeIndex;
				System.out.println("End node found at index: " + end);
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
		int neighborIndex = nodeIndex + 1;

		System.out.println("Parsing edge: " + wallChar + " between nodes " + nodeIndex + " and " + neighborIndex);

		if (Character.isDigit(wallChar)) {
			int coinsNeeded = Character.getNumericValue(wallChar);
			insertEdge(nodeIndex, neighborIndex, coinsNeeded, "door");
			GraphEdge edge = graph.getEdge(graph.getNode(nodeIndex), graph.getNode(neighborIndex));
			edge.setType(coinsNeeded); // Set type for door edges
		} else if (wallChar == 'c') {
			insertEdge(nodeIndex, neighborIndex, 0, "corridor");
			GraphEdge edge = graph.getEdge(graph.getNode(nodeIndex), graph.getNode(neighborIndex));
			edge.setType(0); // Set type for corridor edges
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
