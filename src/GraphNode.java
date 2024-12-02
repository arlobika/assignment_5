
public class GraphNode {

	private int name;
	private boolean marked;
	
	public GraphNode(int name) {
		this.name = name;
		this.marked = false;
	}

	
//	setters and getters
	public void mark(boolean mark) {
		this.marked = mark;
	}
	
	public boolean isMarked() {
		return this.marked;
	}
	
	public int getName() {
		return this.name;
	}
	
}
