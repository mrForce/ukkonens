package ukkonen;

public class Pair<X> {

	private X first;
	private X second;
	public Pair(X first, X second) {
		this.setFirst(first);
		this.setSecond(second);
	}
	public X getFirst() {
		return first;
	}
	public void setFirst(X first) {
		this.first = first;
	}
	public X getSecond() {
		return second;
	}
	public void setSecond(X second) {
		this.second = second;
	}
}
