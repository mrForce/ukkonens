package ukkonen;

public class Edge {

	public SubString edge_label;
	public Node child_node;
	public Edge(SubString edge_label, Node child_node, int edge_label_length) {
		if(edge_label.length() != edge_label_length || edge_label.length() != edge_label.getEndIndex() - edge_label.getStartIndex()) {
			System.out.println("edge_label length is not the same as edge_label_length");
		}
		this.edge_label = edge_label;
		this.child_node = child_node;
	}
	
	public void applyRuleOne() {
		this.edge_label.applyRuleOne();
	}
	public int getEdgeLabelLength() {
		return this.edge_label.length();
	}

	public void setEdgeLabel(SubString first) {
		if(first.length() != first.getEndIndex() - first.getStartIndex()) {
			System.out.println("problem");
		}
		this.edge_label = first;
		
	}
}
