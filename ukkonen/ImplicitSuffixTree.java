package ukkonen;

import java.util.HashMap;

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
		tree.setFullLeaf(first_leaf);
		return tree;
	}
	
	public Node getRoot() {
		return root;
	}
	/* This extends according to rules */
	public PathEnd extend(PathEnd path_end, char next_char) throws NotLeafException, OverwriteEdgeException {
		if(path_end.getType() == PathEndType.NODE) {
			//then path ends on a node
			if(path_end.getEndNode().getType() == NodeType.LEAF) {
				//then path ends at a leaf
				Node leaf = path_end.getEndNode();
				Node parent = leaf.getParent();
				if(leaf.getType() != NodeType.LEAF) {
					throw new NotLeafException();
				}
				String edge_label = leaf.getParentEdgeLabel();
				parent.getOutEdges().remove(edge_label);
				parent.addOutEdge(edge_label + next_char, leaf);
				leaf.setParentEdgeLabel(edge_label + next_char);
				
				return path_end;
			} else if(path_end.getEndNode().getType() == NodeType.INTERNAL) {
				//then it ends on an internal node.
				boolean extend = true;
				Node end_node = path_end.getEndNode();
				for (HashMap.Entry<String, Node> entry : end_node.getOutEdges().entrySet()) {
					if(entry.getKey().startsWith(Character.toString(next_char))) {
						extend = false;
						break;
					}
				}
				if(extend) {
					end_node.add_leaf(Character.toString(next_char));
				}
				return path_end;
			}
		} else if(path_end.getType() == PathEndType.EDGE) {
			Node end_node = path_end.getEndNode();
			Node parent_node = end_node.getParent();
			String fragment = path_end.getFragment();
			if(!end_node.getParentEdgeLabel().startsWith(fragment + Character.toString(next_char))) {
				Node middle_node = new Node(NodeType.INTERNAL, end_node.getParent(), path_end.getFragment());
				middle_node.add_leaf(Character.toString(next_char));
				parent_node.getOutEdges().remove(end_node.getParentEdgeLabel());
				parent_node.addOutEdge(fragment, middle_node);
				end_node.setParent(middle_node);
				
				end_node.setParentEdgeLabel(end_node.getParentEdgeLabel().substring(fragment.length()));
				middle_node.addOutEdge(end_node.getParentEdgeLabel(), end_node);
				return new PathEnd(middle_node);
			}else {
				//don't do anything, since the next character in the path is next_char 
				return path_end;
			}
			
		}
		System.out.println("Problem with applying suffix extension rules");
		return path_end;
	}
	
	public PathEnd singleExtension(PathEnd path_end, String alpha, char next_char) throws NotLeafException, OverwriteEdgeException, NoSuchEdgeException, MissingSuffixLinkException, SingleExtensionFailedException {
		if(path_end.getType() == PathEndType.NODE) {
			Node end_node = path_end.getEndNode();
			if(end_node.hasSuffixLink()) {
				Node link = end_node.getSuffixLink();
				PathEnd new_path = new PathEnd(link);
				return this.extend(new_path, next_char);
			}else {
				Node parent = end_node.getParent();
				if(parent.getType() == NodeType.ROOT) {
					PathEnd traversed_path = parent.traversePath(alpha);
					return this.extend(traversed_path, next_char);
				}
			}
		}else {
			Node end_node = path_end.getEndNode();
			String fragment = path_end.getFragment();
			Node parent_node = end_node.getParent();
			if(parent_node.getType() == NodeType.ROOT) {
				PathEnd traversed_path = parent_node.traversePath(fragment);
				return this.extend(traversed_path, next_char);
			}else {
				if(parent_node.hasSuffixLink()) {
					Node link = parent_node.getSuffixLink();
					PathEnd traversed_path = link.traversePath(fragment);
					return this.extend(traversed_path, next_char);
				}else {
					throw new MissingSuffixLinkException();
				}
			}
		}
		throw new SingleExtensionFailedException();
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
	public PathEnd suffixExtendRuleTwo(PathEnd path_end,  char next_char) throws EdgeDoesNotExistException, OverwriteEdgeException {
		Node end_node = path_end.getEndNode();
		if(path_end.getType() == PathEndType.NODE) {
			end_node.add_leaf(Character.toString(next_char));
			return path_end; 
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
					return new PathEnd(new_internal);
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
