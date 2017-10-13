package ukkonen;

public class OverwriteEdgeException extends Exception{
	   public OverwriteEdgeException(String s) {
	        super("There is already an edge with the label: " + s);
	    }

}
