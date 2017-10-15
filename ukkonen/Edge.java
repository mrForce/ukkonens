package ukkonen;

public class Edge {

	public String edge_label;
	public Node child_node;
	public int edge_label_length;
	public Edge(String edge_label, Node child_node, int edge_label_length) {
		this.edge_label = edge_label;
		this.child_node = child_node;
		this.edge_label_length = edge_label_length;
	}
}
