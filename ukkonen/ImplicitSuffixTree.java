package ukkonen;

public class ImplicitSuffixTree {
	private Node root;
	private Node full_leaf;
	
	ImplicitSuffixTree(){
		Node root = new Node();
		this.setRoot(root);
	}
	static ImplicitSuffixTree firstTree(String s) throws OverwriteEdgeException {
		ImplicitSuffixTree tree = new ImplicitSuffixTree();
		Node root = tree.getRoot();
		Node first_leaf = new Node(NodeType.LEAF, root, Character.toString(s.charAt(0)));
		root.addOutEdge(Character.toString(s.charAt(0)), first_leaf);
		return tree;
	}
	
	public Node getRoot() {
		return root;
	}
	/* path beta ends at a leaf, so pass the leaf, and next character*/
	public void suffixExtendRuleOne(Node leaf, char next_char) throws OverwriteEdgeException, NotLeafException {
		Node parent = leaf.getParent();
		if(leaf.getType() != NodeType.LEAF) {
			throw new NotLeafException();
		}
		String edge_label = leaf.getParentEdgeLabel();
		parent.getOutEdges().remove(edge_label);
		parent.addOutEdge(edge_label + next_char, leaf);
		leaf.setParentEdgeLabel(edge_label + next_char);
	}
	
	/* No path from the end of string beta starts with character S(i + 1), but at least one labeled path continues from the end of beta.
	 * This creates a new leaf edge starting from the end of beta, and labeled with character S(i + 1). 
	 * The string path ends inside an edge.
	 * end_node should be where this edge points to. 
	 * 
	 * This returns the node that the path ends on (if the path ends on an edge, this returns the newly created node)
	 */
	public Node suffixExtendRuleTwo(PathEnd path_end,  char next_char) throws EdgeDoesNotExistException, OverwriteEdgeException {
		Node end_node = path_end.getEndNode();
		if(path_end.getType() == PathEndType.NODE) {
			end_node.add_leaf(Character.toString(next_char));
			return end_node;
		}else {
			String beta = path_end.getFragment();
			String parent_edge_label = end_node.getParentEdgeLabel();
			Node parent = end_node.getParent();
			if(parent_edge_label.startsWith(beta)) {
				String suffix = end_node.getParentEdgeLabel().substring(beta.length());
				Node new_internal = new Node(NodeType.INTERNAL, end_node.getParent(), beta);
				new_internal.setParent(parent);
				parent.getOutEdges().put(beta, new_internal);
				if(parent.getOutEdges().remove(parent_edge_label) == null) {
					throw new EdgeDoesNotExistException();
				}else {
					new_internal.addOutEdge(suffix, end_node);
					end_node.setParent(new_internal);
					end_node.setParentEdgeLabel(suffix);
					new_internal.add_leaf(Character.toString(next_char));
					return new_internal;
				}
			} else {
				throw new EdgeDoesNotExistException();
			}
		}
	}

	public void setRoot(Node root) {
		this.root = root;
	}

	public Node getFullLeaf() {
		return this.full_leaf;
	}

	public void setFullLeaf(Node full_leaf) {
		this.full_leaf = full_leaf;
	}
	
}
