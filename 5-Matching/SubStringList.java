import java.util.Iterator;
import java.util.NoSuchElementException;

// reference : Assignment2 MovieDatabase MyLinkedList.java
class SubStringList<T extends Comparable<T>> implements SubStringListInterface<T> {

	public ListNode<T> head;
	public int numItems;
	private T key;

	public SubStringList(T subString) {
		this.head = new ListNode<T>(null);
		this.numItems = 0;
		this.key = subString;
	}
	
    public final SubStringListIterator<T> iterator() {
        return new SubStringListIterator<T>(this);
    }
	
	public T getKey() {
		return key;
	}

	public boolean isEmpty() {
		return head.getNext() == null;
	}

	public int size() {
		return numItems;
	}

	public T first() {
		return head.getNext().getItem();
	}

	public void add(T item) {
		ListNode<T> last = head;
		while (last.getNext() != null) {
			last = last.getNext();
		}
		last.insertNext(item);
		numItems += 1;
	}

	public void removeAll() {
		head.setNext(null);
		numItems = 0;
	}

	@Override
	public int compareTo(T compareKey) {
		return key.compareTo(compareKey);
	}
}

// reference : Assignment2 MovieDatabase MyLinkedList.java
class SubStringListIterator<T extends Comparable<T>> implements Iterator<T> {
	
	private SubStringList<T> list;
	private ListNode<T> curr;
	private ListNode<T> prev;

	public SubStringListIterator(SubStringList<T> list) {
		this.list = list;
		this.curr = list.head;
		this.prev = null;
	}

	public boolean hasNext() {
		return curr.getNext() != null;
	}

	public T next() {
		if (!hasNext())
			throw new NoSuchElementException();

		prev = curr;
		curr = curr.getNext();

		return curr.getItem();
	}


	public void remove() {
		if (prev == null)
			throw new IllegalStateException("next() should be called first");
		if (curr == null)
			throw new NoSuchElementException();
		prev.removeNext();
		list.numItems -= 1;
		curr = prev;
		prev = null;
	}
}