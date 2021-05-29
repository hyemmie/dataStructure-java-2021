// reference : AVL-RB-B- class note p.38
public class AVLNode<T> {
    public T item;
    public AVLNode<T> left, right;
    public int height;

    public AVLNode() {
        this.item = null;
        this.left = null;
        this.right = null;
        this.height = 0;
    }

    public AVLNode(T item) {
        this.item = item;
        this.left = null;
        this.right = null;
        this.height = 1;
    }

    public AVLNode(T item, AVLNode<T> leftChild, AVLNode<T> rightChild, int h) {
        this.item = item;
        this.left = leftChild;
        this.right = rightChild;
        this.height = h;
    }

    public T getItem() {
		return item;
	}
	
	public AVLNode<T> getLeft() {
		return left;
	}
	public AVLNode<T> getRight() {
		return right;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setItem(T newitem) {
		this.item = newitem;
	}
	
	public void setLeft(AVLNode<T> newNode) {
		if (newNode != null) {
			this.left = newNode;
			this.height = 1 + Math.max(newNode.left.height, newNode.right.height);
		}
		else {
			this.left = null;
			this.left.height = 0;
		}
	}
	public void setRight(AVLNode<T> newNode) {
		if (newNode != null) {
			this.right = newNode;
			this.height = 1 + Math.max(newNode.left.height, newNode.right.height);
		} 
		else {
			this.right = null;
			this.right.height = 0;
		}
	}

	// @Override
	// public int compareTo(T item) {

	// }

}