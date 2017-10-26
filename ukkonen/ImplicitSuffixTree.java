package ukkonen;

import java.util.HashMap;

public class ImplicitSuffixTree {
	private Node root;
	private Node full_leaf;
	private boolean need_suffix_link;
	private Node node_that_needs_suffix_link;
	private String full_string;
	private TrickThreeCounter trick_three_counter;
	ImplicitSuffixTree(String full_string, TrickThreeCounter e){
		Node root = new Node();
		this.full_string = full_string;
		this.setRoot(root);
		this.need_suffix_link = false;
		this.node_that_needs_suffix_link = null;
		this.trick_three_counter = e;
	}
	static ImplicitSuffixTree firstTree(String s, TrickThreeCounter e) throws OverwriteEdgeException {
		ImplicitSuffixTree tree = new ImplicitSuffixTree(s, e);
		Node root = tree.getRoot();
		SubString edge_label = new SubString(s, 0, e);
		Node first_leaf = new Node(NodeType.LEAF, root, edge_label);
		root.addOutEdge(edge_label, first_leaf);
		tree.setFullLeaf(first_leaf);
		return tree;
	}
	public String getFullString() {
		return this.full_string;
	}
	public TrickThreeCounter getTrickThreeCounter() {
		return this.trick_three_counter;
	}
	public void setFullString(String s) {
		this.full_string = s;
		this.need_suffix_link = false;
		this.node_that_needs_suffix_link = null;
	}
	public void setTrickThreeCounter(TrickThreeCounter e) {
		this.trick_three_counter = e;
	}
	public Node getRoot() {
		return root;
	}
	public void clearSuffixLinkSetup() {
		this.need_suffix_link = false;
		this.node_that_needs_suffix_link = null;
	}
	/* This extends according to rules */
	public Pair<ExtensionRule, PathEnd> extend(PathEnd path_end, char next_char, int phase) throws NotLeafException, OverwriteEdgeException, CouldNotExtendException {
		if(path_end.getType() == PathEndType.NODE) {
			Node end_node = path_end.getEndNode();
			//then path ends on a node
			if(end_node.getType() == NodeType.LEAF) {
				//then path ena leaf
				Node leaf = end_node;
				Node parent = leaf.getParent();
				if(leaf.getType() != NodeType.LEAF) {
					throw new NotLeafException();
				}
				String edge_label = leaf.getParentEdgeLabel().toString();
				//the TrickThreeCounter will be incremented in the next phase, so no need to do this.
				//	leaf.getParentEdgeLabel().applyRuleOne();
				return new Pair<ExtensionRule, PathEnd>(ExtensionRule.RuleOne, new PathEnd(leaf, edge_label));
			} else if(path_end.getEndNode().getType() == NodeType.INTERNAL && this.need_suffix_link) {
					this.node_that_needs_suffix_link.setSuffixLink(end_node);
					this.need_suffix_link = false;
					this.node_that_needs_suffix_link = null;
				
			}
			if(!end_node.hasOutEdgeStartsWith(next_char)) {
				end_node.add_leaf(new SubString(this.full_string, phase, this.trick_three_counter));
				return new Pair<ExtensionRule, PathEnd>(ExtensionRule.RuleTwo, path_end);
			}else {
				return new Pair<ExtensionRule, PathEnd>(ExtensionRule.RuleThree, path_end);
			}
		} else if(path_end.getType() == PathEndType.EDGE) {
			Node end_node = path_end.getEndNode();
			Node parent_node = end_node.getParent();
			String fragment = path_end.getFragment();
			if(!end_node.getParentEdgeLabel().startsWith(fragment + Character.toString(next_char))) {
				String edge_label = end_node.getParentEdgeLabel().toString();
				Pair<SubString, SubString> new_labels = end_node.getParentEdgeLabel().splitOnGamma(fragment);
				SubString first_substring = new_labels.getFirst();
				SubString second_substring = new_labels.getSecond();
				Node middle_node = new Node(NodeType.INTERNAL, end_node.getParent(), first_substring);
				if(this.need_suffix_link) {
					this.node_that_needs_suffix_link.setSuffixLink(middle_node);
					this.need_suffix_link = false;
					this.node_that_needs_suffix_link = null;
				}
				middle_node.addOutEdge(second_substring, end_node);
				
				middle_node.add_leaf(new SubString(this.full_string, phase, this.trick_three_counter));
				Edge parent_edge = parent_node.getOutEdges().get(edge_label.charAt(0));
				parent_edge.child_node = middle_node;
				parent_edge.setEdgeLabel(first_substring);
				end_node.setParent(middle_node);
				end_node.setParentEdgeLabel(second_substring);
				if(parent_node.getType() != NodeType.ROOT || middle_node.getParentEdgeLabel().length() != 1) {
					//set the new internal node, which has path S[j-1...i], to get a suffix link to S[j...i] on the next extension.
					this.need_suffix_link = true;
					this.node_that_needs_suffix_link = middle_node;
				}else {
					middle_node.setSuffixLink(this.root);
				}
				return new Pair<ExtensionRule, PathEnd>(ExtensionRule.RuleTwo, new PathEnd(middle_node));
			}else {
				//don't do anything, since the next character in the path is next_char 
				return new Pair<ExtensionRule, PathEnd>(ExtensionRule.RuleThree, path_end);
			}
			
		}
		System.out.println("Problem with applying suffix extension rules");
		throw new CouldNotExtendException();
	}
	
	public Pair<ExtensionRule, PathEnd> singleExtension(PathEnd path_end, String alpha, char next_char, int phase) throws NotLeafException, OverwriteEdgeException, NoSuchEdgeException, MissingSuffixLinkException, SingleExtensionFailedException, CouldNotExtendException {
		if(path_end.getType() == PathEndType.NODE) {
			Node end_node = path_end.getEndNode();
			if(end_node.hasSuffixLink()) {
				Node link = end_node.getSuffixLink();
				PathEnd new_path = new PathEnd(link);
				return this.extend(new_path, next_char, phase);
			}else {
				Node parent = end_node.getParent();
				if(parent.getType() == NodeType.ROOT) {
					PathEnd traversed_path = parent.traversePath(alpha);
					return this.extend(traversed_path, next_char, phase);
				} else if(parent.hasSuffixLink()){
					Node link = parent.getSuffixLink();
					String parent_edge_label = end_node.getParentEdgeLabel().toString();
					String gamma = parent_edge_label.substring(0, parent_edge_label.length() - 1);
					PathEnd sv = link.traversePath(gamma);
					return this.extend(sv, next_char, phase);
				}
			}
		}else {
			Node end_node = path_end.getEndNode();
			String fragment = path_end.getFragment();
			Node parent_node = end_node.getParent();
			if(parent_node.getType() == NodeType.ROOT) {
				PathEnd traversed_path = parent_node.traversePath(alpha);
				return this.extend(traversed_path, next_char, phase);
			}else {
				if(parent_node.hasSuffixLink()) {
					Node link = parent_node.getSuffixLink();
					PathEnd traversed_path = link.traversePath(fragment);
					return this.extend(traversed_path, next_char, phase);
				}else {
					throw new MissingSuffixLinkException();
				}
			}
		}
		throw new SingleExtensionFailedException();
	}
	/* path beta ends at a leaf, so pass the leaf, and next character*/
/*	public void suffixExtendRuleOne(Node leaf, char next_char) throws OverwriteEdgeException, NotLeafException {
		if(leaf.getType() != NodeType.LEAF) {
			throw new NotLeafException();
		}
		
		
		 //The parent's edge and child's parent_edge_label share a SubString for the label, so we only need to modify one of them.
		 
		//leaf.getParentEdgeLabel().applyRuleOne();
	}*/
	
	/* No path from the end of string beta starts with character S(i + 1), but at least one labeled path continues from the end of beta.
	 * This creates a new leaf edge starting from the end of beta, and labeled with character S(i + 1). 
	 * The string path ends inside an edge.
	 * end_node should be where this edge points to. 
	 * 
	 * This returns the node that the path ends on (if the path ends on an edge, this returns the newly created node)
	 */
	public PathEnd suffixExtendRuleTwo(PathEnd path_end,  char next_char, int phase) throws EdgeDoesNotExistException, OverwriteEdgeException {
		return null;
		/*
		Node end_node = path_end.getEndNode();
		if(path_end.getType() == PathEndType.NODE) {
			end_node.add_leaf(new SubString(this.full_string, phase, phase + 1));
			return path_end; 
		}else {
			String beta = path_end.getFragment();
			String parent_edge_label = end_node.getParentEdgeLabel().toString();
			Node parent = end_node.getParent();
			if(parent_edge_label.startsWith(beta)) {
				String suffix = end_node.getParentEdgeLabel().substring(beta.length());
				Node new_internal = new Node(NodeType.INTERNAL, end_node.getParent(), beta);
				new_internal.setParent(parent);
				parent.addOutEdge(beta, new_internal);
				if(parent.getOutEdges().remove(parent_edge_label.charAt(0)) == null) {
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
		}*/
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
