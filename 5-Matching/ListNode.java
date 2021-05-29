// reuse Assignment2 MovieDatabase Node.java
public class ListNode<T> {
    private T item;
    private ListNode<T> next;

    public ListNode(T obj) {
        this.item = obj;
        this.next = null;
    }
    
    public ListNode(T obj, ListNode<T> next) {
        this.item = obj;
        this.next = next;
    }
    
    public final T getItem() {
        return item;                      
    }
    
    public final void setItem(T item) {
        this.item = item; 
    }
    
    public final void setNext(ListNode<T> next) {
        this.next = next;
    }
    
    public ListNode<T> getNext() {
        return this.next;
    }

    public final void insertNext(T obj) {
        ListNode<T> newNode = new ListNode<T>(obj);
        newNode.setNext(this.next);
        this.next = newNode;
    }
    
    public final void removeNext() {
        this.next = this.next.next;
    }
}
