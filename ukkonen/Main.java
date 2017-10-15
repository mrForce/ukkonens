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
		
		PathEnd x_alpha_end = null;
		for(int i = 1; i < length; i++) {
			char next_char = s.charAt(i);
			for(int j = 0; j < i + 1; j++) {
				String alpha = s.substring(j, i);
				System.out.println(alpha);
				if(alpha.equals("x") && next_char == 'c') {
					System.out.println("hi");
				}
				if(tree.getRoot().getOutEdges().containsKey("axabb")) {
					System.out.println("Hello");
				}
				if(alpha.length() == 0) {
					if(!tree.getRoot().hasOutEdgeStartsWith(next_char)) {
						try {
							tree.getRoot().add_leaf(Character.toString(next_char));
						} catch (OverwriteEdgeException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					continue;
				}
				if(j == 0) {
					String gamma = tree.getFullLeaf().getParentEdgeLabel();
					try {
						tree.suffixExtendRuleOne(tree.getFullLeaf(), next_char);
					} catch (OverwriteEdgeException e) {
						System.out.println("Overwriting edge in extension " + Integer.toString(j) + " of phase: " + Integer.toString(i));
					} catch (NotLeafException e) {
						System.out.println("Not leaf exception in extension  " + Integer.toString(j) + " of phase: " + Integer.toString(i));
					}
					//the path actually ends at 
					x_alpha_end = new PathEnd(tree.getFullLeaf(), gamma);
					//x_alpha_end = new PathEnd(tree.getFullLeaf());
				} else if (j == 1) {
					Node x_alpha_node = x_alpha_end.getEndNode();
					String gamma = x_alpha_end.getFragment();
					Node parent = x_alpha_node.getParent();
					
					PathEnd end = null;
					if(parent.hasSuffixLink()) {
						Node link = parent.getSuffixLink();
						try {
							end = link.traversePath(gamma);
						} catch (NoSuchEdgeException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if(parent.getType() == NodeType.ROOT){
						try {
							 end = parent.traversePath(alpha);
						} catch (NoSuchEdgeException e) {
							e.printStackTrace();
							
						}
					} else {
						System.out.println("Problem! Parent is not root, and doesn't have a suffix link");
					}
						if(end.getType() == PathEndType.NODE) {
							Node end_node = end.getEndNode();
							x_alpha_node = end_node;
							if(end_node.getType() == NodeType.LEAF) {
								x_alpha_end = new PathEnd(end_node);
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
								try {
									x_alpha_end = tree.extend(x_alpha_end, next_char);
								} catch (NotLeafException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (OverwriteEdgeException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						} else if(end.getType() == PathEndType.EDGE){
							if(!end.getEndNode().getParentEdgeLabel().startsWith(end.getFragment() + Character.toString(next_char))) {
								try {
									x_alpha_end = tree.suffixExtendRuleTwo(end, next_char);
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
					try {
						x_alpha_end = tree.singleExtension(x_alpha_end, alpha, next_char);
					} catch (NotLeafException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (OverwriteEdgeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchEdgeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MissingSuffixLinkException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SingleExtensionFailedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		System.out.println("done");
	}

}
