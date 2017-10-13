package ukkonen;

public class ImplicitSuffixTree {
	private Node root;
	private Node longest_leaf;
	
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
	 *  */
	public void suffixExtendRuleTwo(Node end_node, char next_char) throws OverwriteEdgeException {
		end_node.add_leaf(Character.toString(next_char));
	}
	public void suffixExtendRuleTwo(Node end_node, char next_char, String beta) throws EdgeDoesNotExistException, OverwriteEdgeException {
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
			}
		} else {
			throw new EdgeDoesNotExistException();
		}
	}

	public void setRoot(Node root) {
		this.root = root;
	}

	public Node getLongestLeaf() {
		return longest_leaf;
	}

	public void setLongestLeaf(Node longest_leaf) {
		this.longest_leaf = longest_leaf;
	}
	
}
