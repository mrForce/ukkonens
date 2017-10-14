package ukkonen;

import java.util.HashMap;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter in string to build suffix tree for:");
		String s = scan.nextLine();
		ImplicitSuffixTree tree = null;
		try {
			 tree = ImplicitSuffixTree.firstTree(s);
		} catch (OverwriteEdgeException e) {
			// TODO Auto-generated catch block
			System.out.println("Problem");
			e.printStackTrace();
		}
		int length = s.length();
		
		Node x_alpha_node = null;
		for(int i = 0; i < length - 1; i++) {
			for(int j = 0; j < i + 1; j++) {
				char next_char = s.charAt(i + 1);
				String alpha = s.substring(j, i + 1);
				if(j == 0) {
					try {
						tree.suffixExtendRuleOne(tree.getFullLeaf(), next_char);
					} catch (OverwriteEdgeException e) {
						System.out.println("Overwriting edge in extension " + Integer.toString(j) + " of phase: " + Integer.toString(i));
					} catch (NotLeafException e) {
						System.out.println("Not leaf exception in extension  " + Integer.toString(j) + " of phase: " + Integer.toString(i));
					}
					x_alpha_node = tree.getFullLeaf();
				} else if (j == 1) {
					String parent_edge_label = x_alpha_node.getParentEdgeLabel();
					Node parent = x_alpha_node.getParent();
					if(parent.hasSuffixLink()) {
						Node link = parent.getSuffixLink();
						PathEnd end = null;
						try {
							end = link.traversePath(parent_edge_label);
						} catch (NoSuchEdgeException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(end.getType() == PathEndType.NODE) {
							Node end_node = end.getEndNode();
							x_alpha_node = end_node;
							if(end_node.getType() == NodeType.LEAF) {
								try {
									tree.suffixExtendRuleOne(end_node, next_char);
								} catch (OverwriteEdgeException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (NotLeafException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}else {
								/* Then path ends at internal node */
								boolean extend = true;
								for (HashMap.Entry<String, Node> entry : end_node.getOutEdges().entrySet()) {
									if(entry.getKey().startsWith(Character.toString(next_char))) {
										extend = false;
										break;
									}
								}
								if(extend) {
									try {
										x_alpha_node = tree.suffixExtendRuleTwo(end, next_char);
									} catch (EdgeDoesNotExistException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (OverwriteEdgeException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
						} else if(end.getType() == PathEndType.EDGE){
							if(!end.getEndNode().getParentEdgeLabel().startsWith(end.getFragment() + Character.toString(next_char))) {
								try {
									x_alpha_node = tree.suffixExtendRuleTwo(end, next_char);
								} catch (EdgeDoesNotExistException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (OverwriteEdgeException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					} else {
						System.out.println("Problem! The parent doesn't have a suffix link");
					}
				} else {
					Node link = null;
					if(x_alpha_node.hasSuffixLink()) {
						link = x_alpha_node.getSuffixLink();
					}else if(x_alpha_node.getParent().getType() == NodeType.ROOT){
						Node root = x_alpha_node.getParent();
						PathEnd path = null;
						try {
							path = root.traversePath(alpha);
						} catch (NoSuchEdgeException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//do extension here
					} else if(x_alpha_node.getParent().hasSuffixLink()){
						
					}else {
						System.out.println("Sorry, the parent doesn't have a link");
						return;
					}
				}
			}
		}
		
	}

}
