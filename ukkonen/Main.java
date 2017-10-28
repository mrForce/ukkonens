package ukkonen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scan = new Scanner(System.in);
		/*
		System.out.println("Enter in strings to build suffix tree for:");
		List<String> proteins = new ArrayList<String>();
		while(scan.hasNextLine()) {
			String next_line = scan.nextLine();
			if(next_line.length() == 0) {
				break;
			}
			String protein = next_line + "$";
			protein.intern();
			proteins.add(protein);
		}*/
		List<String> proteins = new ArrayList<String>();
		proteins.add("babxba$");
		proteins.add("xabxa$");
		ImplicitSuffixTree tree = null;
		TrickThreeCounter trick_three_counter = new TrickThreeCounter(1);
		try {
			 tree = ImplicitSuffixTree.firstTree(proteins.get(0), trick_three_counter);
		} catch (OverwriteEdgeException e) {
			// TODO Auto-generated catch block
			System.out.println("Problem");
			e.printStackTrace();
		}
		Iterator protein_iter = proteins.iterator();
		addToTree(tree, (String) protein_iter.next(), true);
		while(protein_iter.hasNext()) {
			addToTree(tree, (String) protein_iter.next(), false);
		}
		System.out.println(getTreeString(tree));

	}
	
	public static int stringOverlap(String one, int index_one, String two) {
		/* 
		 * Suppose one = ABBCDD, and index_one = 1, and two = BBQ
		 * 
		 * It overlaps BBCDD with BBQ, and returns 2, since BB overlaps. It returns the number of characters that overlap.
		 * 
		 */
		int j = 0;
		while(index_one + j < one.length() && j < two.length() && one.charAt(index_one + j) == two.charAt(j)) {
			j++;
		}
		return j;
	}
	
	public static int matchStringAgainstTree(ImplicitSuffixTree tree, String s) {
		Node node = tree.getRoot();
		int i = 0;
		boolean keepGoing = true;
		while(keepGoing) {
			if(node.hasOutEdgeStartsWith(s.charAt(i))) {
				String edge_label = node.getOutEdges().get(s.charAt(i)).edge_label.toString();
				int overlap = stringOverlap(s, i, edge_label);
				i += overlap;
				if(overlap < edge_label.length()) {
					return i;
				}
			}else {
				return i;
			}
		}
		return -1;
		
	}
	public static void addToTree(ImplicitSuffixTree tree, String s, boolean first_string){
		int start_phase = 1;
		TrickThreeCounter trick_three_counter = null;
		if(first_string) {
			trick_three_counter = tree.getTrickThreeCounter();
			start_phase = 1;
		}else {
			tree.setFullString(s);
			trick_three_counter = new TrickThreeCounter(1);
			tree.setTrickThreeCounter(trick_three_counter);
			if(tree.getRoot().hasOutEdgeStartsWith(s.charAt(0))) {
				start_phase  = matchStringAgainstTree(tree, s);
			}else {
				try {
					tree.getRoot().add_leaf(new SubString(s, 0, trick_three_counter));
				} catch (OverwriteEdgeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				start_phase = 1;
			}
		}
				int length = s.length();
		System.out.println(getTreeString(tree));
		PathEnd x_alpha_end = null;
		String gamma;
		if(first_string) {
			 gamma = tree.getFullLeaf().getParentEdgeLabel().toString();

			 x_alpha_end = new PathEnd(tree.getFullLeaf(), gamma);
		}else {
			 x_alpha_end = new PathEnd(tree.getRoot());
		}
		int start_extension_number = 0;
		boolean no_suffix_traversal = false;
		for(int i = start_phase; i < length; i++) {
			if(i == length - 1) {
				System.out.println("Hello");
			}
			tree.clearSuffixLinkSetup();
			trick_three_counter.setCounter(i + 1);
			char next_char = s.charAt(i);
			int j;
			if(!first_string)
			{
				System.out.println("things");
			}
			if(i == length - 1) {
				System.out.println("hello");
			}
			/* Suppose S[j...i + 1] is in the tree, and x_alpha_end points to the end of it.
			 * 
			 * This is the case if the last phase ended with rule 3. We don't need to traverse any suffix links,
			 * since we are currently trying to add S[i + 2] to S[j... i + 1].
			 */
			for(j = start_extension_number; j < i + 1; j++) {
				String alpha = s.substring(j, i);
				if(!first_string && alpha.equals("abx") && next_char == 'a') {
					System.out.println("equals");
				}
				System.out.println(alpha);
				System.out.println("i: " + Integer.toString(i));
				System.out.println("j: " + Integer.toString(j));
				if(alpha.length() == 0) {
					if(!tree.getRoot().hasOutEdgeStartsWith(next_char)) {
						no_suffix_traversal = false;
						try {
							x_alpha_end = new PathEnd(tree.getRoot().add_leaf(new SubString(s, i, trick_three_counter)));
						} catch (OverwriteEdgeException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else {
						//this is basically RuleThree
						
						start_extension_number = j;
						Node x_alpha_end_node = tree.getRoot().getOutEdges().get(next_char).child_node;
						if(x_alpha_end_node.getParentEdgeLabel().length() == 1 && x_alpha_end_node.getType() == NodeType.INTERNAL) {
							x_alpha_end = new PathEnd(x_alpha_end_node);
						}else {
							x_alpha_end = new PathEnd(x_alpha_end_node, Character.toString(next_char));
						}
						
						if(i != length - 1) {
							System.out.println("Rule Three triggered");
							no_suffix_traversal = true;
							break;
						}
					}
					continue;
				}
				if(j == 0 && first_string) {
					 gamma = tree.getFullLeaf().getParentEdgeLabel().toString();
					/*
					try {
						tree.suffixExtendRuleOne(tree.getFullLeaf(), next_char);
					} catch (OverwriteEdgeException e) {
						System.out.println("Overwriting edge in extension " + Integer.toString(j) + " of phase: " + Integer.toString(i));
					} catch (NotLeafException e) {
						System.out.println("NotLeafException in extension  " + Integer.toString(j) + " of phase: " + Integer.toString(i));
					}*/
					x_alpha_end = new PathEnd(tree.getFullLeaf(), gamma);
					no_suffix_traversal = false;
					//x_alpha_end = new PathEnd(tree.getFullLeaf());
				} else if (j == 1 && first_string) {
					Node x_alpha_node = x_alpha_end.getEndNode();
					gamma = x_alpha_end.getFragment();
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
					try {
						Pair<ExtensionRule, PathEnd> a =  tree.extend(end, next_char, i);
						x_alpha_end = a.getSecond();
						no_suffix_traversal = false;
						if(x_alpha_end.getEndNode().getType() == NodeType.ROOT) {
							System.out.println("Why does it end at root?");
						}
						if(a.getFirst() == ExtensionRule.RuleThree && i < length - 1) {
							start_extension_number = j;
							no_suffix_traversal = true;	
							System.out.println("Rule three triggered");
							break;
						}
					} catch (NotLeafException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (OverwriteEdgeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch(CouldNotExtendException e) {
						e.printStackTrace();
					}
					/*
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
							}else {*/
								/* Then path ends at internal node */
								/*try {
									x_alpha_end = tree.extend(x_alpha_end, next_char, j);
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
						}*/ catch (InvalidPathEndException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						
					
				} else {
					try {
						Pair<ExtensionRule, PathEnd> a = tree.singleExtension(x_alpha_end, alpha, next_char, i, no_suffix_traversal);
						x_alpha_end = a.getSecond();
						no_suffix_traversal = false;
						if(x_alpha_end.getEndNode().getType() == NodeType.ROOT) {
							System.out.println("Why does it end at root?");
						}
						if(a.getFirst() == ExtensionRule.RuleThree && i < length - 1) {
							start_extension_number = j;
							System.out.println("Rule Three triggered");
							no_suffix_traversal = true;
							break;
						}
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
					} catch (CouldNotExtendException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvalidPathEndException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			System.out.println("I " + Integer.toString(i + 1));
			System.out.println(getTreeString(tree));
			start_extension_number = j;
		}
		System.out.println("done");
		System.out.println(getTreeString(tree));
	}
	public static String DFSPrint(Node parent, Counter counter) {
		String string = "";
		String parent_name = Integer.toString(counter.counter);
		counter.counter++;
		for(Edge edge : parent.getOutEdges().values()) {
			Node child = edge.child_node;
			String label = edge.edge_label.toString();
			string += "L" + parent_name + " -> " + "L" + Integer.toString(counter.counter) + " [ label = \"" + label + "\" ];\n";
			string += DFSPrint(child, counter);
			counter.counter++;
		}
		return string;
		
	}
	public static String getTreeString(ImplicitSuffixTree tree) {
		//yes, I know, StringBuilder is better. But the trees here aren't big enough for that to become a concern (this is only a prototype)
		String string = "";
		string += "digraph tree {";
		Node root = tree.getRoot();
		string += DFSPrint(root, new Counter());
		string += "\n}";
		return string;
	}

}
