package ukkonen;

public class SubString {
	private String full_string;
	private int start_index;
	private int end_index;
	private TrickThreeCounter e;
	private boolean isLeafEdge;
	public SubString(String full_string, int start_index, int end_index) {
		this.full_string = full_string;
		this.setStartIndex(start_index);
		this.isLeafEdge = false;
		try {
			this.setEndIndex(end_index);
		}catch(DontSetEndIndexOnLeafException ex) {
			System.out.println("Something went seriously wrong");
		}
	}
	public SubString(String full_string, int start_index, TrickThreeCounter e) {
		this.isLeafEdge = true;
		this.full_string = full_string;
		this.setStartIndex(start_index);
		this.e = e;
	}
	public int getStartIndex() {
		return start_index;
	}
	public void setStartIndex(int start_index) {
		this.start_index = start_index;
	}
	public int getEndIndex() {
		if(this.isLeafEdge) {
			return this.e.getCounter();
		}else {
			return end_index;
		}
	}
	public void setEndIndex(int end_index) throws DontSetEndIndexOnLeafException{
		if(this.isLeafEdge) {
			throw new DontSetEndIndexOnLeafException();
		}
		this.end_index = end_index;
	}
	public String toString() {
		return this.full_string.substring(this.getStartIndex(), this.getEndIndex());
	}
	public void applyRuleOne() {
		System.out.println("You shouldn't be applying rule 1");
		this.end_index++;
	}
	public char getFirstCharacter() {
		return this.full_string.charAt(start_index);
	}
	public boolean startsWith(String prefix) {
		return this.toString().startsWith(prefix);
	}
	public char charAt(int i) {
		// TODO Auto-generated method stub
		return this.toString().charAt(i);
	}
	public int length() {
		return this.getEndIndex() - start_index;
	}
	public String getFullString() {
		return this.full_string;
	}
	public Pair<SubString, SubString> splitOnGamma(String gamma){
		SubString first = new SubString(this.full_string, this.start_index, this.start_index + gamma.length());
		SubString second = null;
		if(this.isLeafEdge) {
			 second = new SubString(this.full_string, this.start_index + gamma.length(), this.e);
		}else {
			 second = new SubString(this.full_string, this.start_index + gamma.length(), this.getEndIndex());
		}
		return new Pair<SubString, SubString>(first, second);
	}
}
