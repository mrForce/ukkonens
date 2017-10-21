package ukkonen;

public class Edge {

	public SubString edge_label;
	public Node child_node;
	public int edge_label_length;
	public Edge(SubString edge_label, Node child_node, int edge_label_length) {
		this.edge_label = edge_label;
		this.child_node = child_node;
		this.edge_label_length = edge_label_length;
	}
	
	public void applyRuleOne() {
		this.edge_label.applyRuleOne();
		this.edge_label_length++;
	}

	public void setEdgeLabel(SubString first) {
		this.edge_label = first;
		this.edge_label_length = first.length();
		
	}
}
