package ukkonen;

import java.util.HashMap;

public class Node {

	private Node suffix_link;
	private boolean has_suffix_link;
	private HashMap<Character, Edge> out_edges;
	private NodeType type;
	private SubString parent_edge_label;
	private Node parent;
	Node(){
		//create root
		setType(NodeType.ROOT);
		this.has_suffix_link = false;
		this.out_edges = new HashMap<Character, Edge>();
	}
	Node(NodeType type, Node parent, SubString parent_edge_label){
		 setType(type);
		 setParent(parent);
		 setParentEdgeLabel(parent_edge_label);
		 this.has_suffix_link = false;
		 this.out_edges = new HashMap<Character, Edge>();
	}
	
	public boolean hasOutEdgeStartsWith(char c) {
		if(getOutEdges().containsKey(new Character(c))) {
			return true;
		}else {
			return false;
		}
	}
	public Node add_leaf(SubString s) throws OverwriteEdgeException {
		if(this.out_edges.containsKey(s.charAt(0))) {
			throw new OverwriteEdgeException(s);
		}else {
			Node leaf = new Node(NodeType.LEAF, this, s);
			Edge edge = new Edge(s, leaf, s.length());
			this.out_edges.put(s.charAt(0), edge);
			return leaf;
		}
	}
	public Node getSuffixLink() {
		return suffix_link;
	}
	public void setSuffixLink(Node suffix_link) {
		this.suffix_link = suffix_link;
		this.has_suffix_link = true;
	}
	public HashMap<Character, Edge> getOutEdges() {
		return out_edges;
	}
	public void setOutEdges(HashMap<Character, Edge> out_edges) {
		this.out_edges = out_edges;
	}
	public NodeType getType() {
		return type;
	}
	public void setType(NodeType type) {
		this.type = type;
	}
	public void addOutEdge(SubString string, Node child) throws OverwriteEdgeException {
		if(this.hasOutEdgeStartsWith(string.charAt(0))) {
			throw new OverwriteEdgeException(string);
		}else {
			Edge edge = new Edge(string, child, string.length());
			this.out_edges.put(string.charAt(0), edge);
		}
	}
	public SubString getParentEdgeLabel() {
		return parent_edge_label;
	}
	public void setParentEdgeLabel(SubString parent_edge_label) {
		this.parent_edge_label = parent_edge_label;
	}
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	public boolean hasSuffixLink() {
		return has_suffix_link;
	}
	public PathEnd traversePath(String s) throws NoSuchEdgeException{
		char first_char = s.charAt(0);
		if(getOutEdges().containsKey(first_char)) {
			Edge edge = getOutEdges().get(first_char);
			if(edge.edge_label_length < s.length()) {
				return edge.child_node.traversePath(s.substring(edge.edge_label_length));
			}else if(edge.edge_label_length == s.length()){
				//then it ends on the child.
				PathEnd end = new PathEnd(edge.child_node);
				return end;
			} else {
				PathEnd end = new PathEnd(edge.child_node, s);
				return end;
			}
		}else {
			throw new NoSuchEdgeException();
		}
	
	}
}
