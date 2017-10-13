package ukkonen;

import java.util.HashMap;

public class Node {

	private Node suffix_link;
	private HashMap<String, Node> out_edges;
	private NodeType type;
	private String parent_edge_label;
	private Node parent;
	Node(){
		//create root
		setType(NodeType.ROOT);
	}
	Node(NodeType type, Node parent, String parent_edge_label){
		 setType(type);
		 setParent(parent);
		 setParentEdgeLabel(parent_edge_label);
	}
	public void add_leaf(String s) throws OverwriteEdgeException {
		if(this.out_edges.containsKey(s)) {
			throw new OverwriteEdgeException(s);
		}else {
			Node leaf = new Node(NodeType.LEAF, this, s);
			this.out_edges.put(s, leaf);
		}
	}
	public Node getSuffixLink() {
		return suffix_link;
	}
	public void setSuffixLink(Node suffix_link) {
		this.suffix_link = suffix_link;
	}
	public HashMap<String, Node> getOutEdges() {
		return out_edges;
	}
	public void setOutEdges(HashMap<String, Node> out_edges) {
		this.out_edges = out_edges;
	}
	public NodeType getType() {
		return type;
	}
	public void setType(NodeType type) {
		this.type = type;
	}
	public void addOutEdge(String string, Node child) throws OverwriteEdgeException {
		if(this.out_edges.containsKey(string)) {
			throw new OverwriteEdgeException(string);
		}else {
			this.out_edges.put(string, child);
		}
	}
	public String getParentEdgeLabel() {
		return parent_edge_label;
	}
	public void setParentEdgeLabel(String parent_edge_label) {
		this.parent_edge_label = parent_edge_label;
	}
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
}
