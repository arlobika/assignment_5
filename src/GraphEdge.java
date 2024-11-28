
public class GraphEdge {

	private GraphNode origin;
	private GraphNode destination;
	private int type;
	private String label;
	//constructor
	public GraphEdge(GraphNode u, GraphNode v, int type, String label) {
		origin = u;
		destination = v;
		this.type = type;
		this.label = label;
	}
	
//getters and setters
	public GraphNode firstEndpoint() {
		return origin;
	}
	
	public GraphNode secondEndpoint() {
		return destination;
	}
	
	public int getType() {
		return this.type;
	}
	
	public void setType(int newType) {
		this.type = newType;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public void setLabel(String newLabel) {
		this.label = newLabel;
	}
	
}
