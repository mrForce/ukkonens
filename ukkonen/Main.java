package ukkonen;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter in string to build suffix tree for:");
		String s = scan.nextLine();
		try {
			ImplicitSuffixTree tree = ImplicitSuffixTree.firstTree(s);
		} catch (OverwriteEdgeException e) {
			// TODO Auto-generated catch block
			System.out.println("Problem");
			e.printStackTrace();
		}
		int length = s.length();
		for(int i = 0; i < length - 1; i++) {
			for(int j = 0; j < i + 1; j++) {
				if(j == 0) {
					
				}
			}
		}
		
	}

}
