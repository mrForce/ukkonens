package ukkonen;

public class PathEnd {
	private PathEndType type;
	private Node end_node;
	private String fragment;
	public PathEnd(Node node) {
		this.type = PathEndType.NODE;
		this.end_node = node;
	}
	public PathEnd(Node node, String fragment) {
		this.type = PathEndType.EDGE;
		this.end_node = node;
		this.fragment = fragment;
	}
	public Node getEndNode() {
		return this.end_node;
	}
	public PathEndType getType() {
		return this.type;
	}
	public String getFragment() {
		return this.fragment;
	}
	
}
