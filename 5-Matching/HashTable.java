interface ItemInterface<T> {

    public int hash();
    public AVLNode<T> search(T item);
	public void insert(T item);
	public void delete(T item);
    public boolean isEmpty();
	public void removeAll();	
}


public class HashTable<T> {
    private T table[];
    int numItems;
    static final Integer DEFAULT_SIZE = 100;
    
    @SuppressWarnings("unchecked")
    public HashTable() {
        table = (T[]) new Object[DEFAULT_SIZE];
        numItems = 0;
    }

    public T search(int slot) {
        if (slot == table.length) throw new ArrayIndexOutOfBoundsException();
        else {
            return table[slot];
        }
    }

    public void insert(int slot, T item) {
        if (slot == table.length) throw new ArrayIndexOutOfBoundsException();
        else {
            if (table[slot] == null) {
                table[slot] = item;
                numItems++;
            }
        }
    }

    public void delete(int slot) {
        if (slot == table.length || table[slot] == null) throw new ArrayIndexOutOfBoundsException();
        else {
            table[slot] = null;
            numItems--;
        }
    }

    @SuppressWarnings("unchecked")
    public void clear() {
        table = (T[]) new Object[DEFAULT_SIZE];
        numItems = 0;
    }
}
