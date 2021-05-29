// reference Assignment2 MovieDatabase ListInterface.java
public interface SubStringListInterface<T> extends Iterable<T>, Comparable<T> {

	public boolean isEmpty();
	public int size();
	public void add(T item);
	public T first();
	public void removeAll();
	public int compareTo(T key);
	public T getKey();
}