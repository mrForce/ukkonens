package ukkonen;

import java.util.HashMap;

public class Node {

	private Node suffix_link;
	private boolean has_suffix_link;
	private HashMap<String, Node> out_edges;
	private NodeType type;
	private String parent_edge_label;
	private Node parent;
	Node(){
		//create root
		setType(NodeType.ROOT);
		this.has_suffix_link = false;
		this.out_edges = new HashMap<String, Node>();
	}
	Node(NodeType type, Node parent, String parent_edge_label){
		 setType(type);
		 setParent(parent);
		 setParentEdgeLabel(parent_edge_label);
		 this.has_suffix_link = false;
		 this.out_edges = new HashMap<String, Node>();
	}
	
	public boolean hasOutEdgeStartsWith(char c) {
		String cs = Character.toString(c);
		for (HashMap.Entry<String, Node> entry : getOutEdges().entrySet()) {
			if(entry.getKey().startsWith(cs)) {
				return true;
			}
		}
		return false;
	}
	public Node add_leaf(String s) throws OverwriteEdgeException {
		if(this.out_edges.containsKey(s)) {
			throw new OverwriteEdgeException(s);
		}else {
			Node leaf = new Node(NodeType.LEAF, this, s);
			this.out_edges.put(s, leaf);
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
	public boolean hasSuffixLink() {
		return has_suffix_link;
	}
	public PathEnd traversePath(String s) throws NoSuchEdgeException{
		for (HashMap.Entry<String, Node> entry : getOutEdges().entrySet()) {
			String key = entry.getKey();
			if(s.startsWith(key)) {
				String substring = s.substring(key.length());
				if(substring.length() == 0) {
					//then we end on the child 
					PathEnd end = new PathEnd(entry.getValue());
					return end;
				}else {
					return entry.getValue().traversePath(s.substring(key.length()));
				}
			}else if(key.startsWith(s)){
				//then the path ends on an edge.
				PathEnd end = new PathEnd(entry.getValue(), s);
				return end;
			}
		}
		throw new NoSuchEdgeException();
	}
	
}
