package ukkonen;

public class OverwriteEdgeException extends Exception{
	   public OverwriteEdgeException(SubString string) {
	        super("There is already an edge with the label: " + string);
	    }

}
