package ukkonen;

public class SubString {
	private String full_string;
	private int start_index;
	private int end_index;
	public SubString(String full_string, int start_index, int end_index) {
		this.full_string = full_string;
		this.setStartIndex(start_index);
		this.setEndIndex(end_index);
	}
	public int getStartIndex() {
		return start_index;
	}
	public void setStartIndex(int start_index) {
		this.start_index = start_index;
	}
	public int getEndIndex() {
		return end_index;
	}
	public void setEndIndex(int end_index) {
		this.end_index = end_index;
	}
	public String toString() {
		return this.full_string.substring(this.start_index, this.end_index);
	}
	public void applyRuleOne() {
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
		return end_index - start_index;
	}
	public String getFullString() {
		return this.full_string;
	}
	public Pair<SubString> splitOnGamma(String gamma){
		SubString first = new SubString(this.full_string, this.start_index, this.start_index + gamma.length());
		SubString second = new SubString(this.full_string, this.start_index + gamma.length(), this.end_index);
		return new Pair<SubString>(first, second);
	}
}
