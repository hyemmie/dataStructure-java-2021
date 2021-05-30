// reference : AVL-RB-B-Tree class note pp.39-42
interface SubAVLTreeInterface<K, T> {
    public AVLNode<T> search(K key);
	public void insert(T item);
	public void delete(T item);
    public boolean isEmpty();
	public void removeAll();
}	

public class SubAVLTree<K extends Comparable<K>, T extends SubStringListInterface<K>> implements SubAVLTreeInterface<K, T> {
    private AVLNode<T> root;
    final AVLNode<T> NIL = new AVLNode<>();
    private final int LL = 1, LR = 2, RR = 3, RL = 4, NO_NEED = 0, ILLEGAL = -1;


    public SubAVLTree() {
        root = NIL;
    }

    public AVLNode<T> getRoot() {
        return root;
    }

    public AVLNode<T> search(K key) {
        return searchItem(root, key);
    }

    private AVLNode<T> searchItem(AVLNode<T> tNode, K key) {
        if (tNode == NIL) return NIL;
        else if (tNode.item.getKey().compareTo(key) == 0) return tNode;
        else if (tNode.item.getKey().compareTo(key) > 0) return searchItem(tNode.left, key);
        else return searchItem(tNode.right, key);
    }

    public void insert(T item) {
        root = insertItem(root, item);
    }

    private AVLNode<T> insertItem(AVLNode<T> tNode, T item) {
        int type;
        if (tNode == NIL) {
            tNode = new AVLNode<>(item, NIL, NIL, 1);
        } else if (tNode.item.getKey().compareTo(item.getKey()) > 0) {
            tNode.left = insertItem(tNode.left, item);
            tNode.height = 1 + Math.max(tNode.right.height, tNode.left.height);
            type = needBalance(tNode);
            if (type != NO_NEED) tNode = balanceAVL(tNode, type);
        } else {
            tNode.right = insertItem(tNode.right, item);
            tNode.height = 1 + Math.max(tNode.right.height, tNode.left.height);
            type = needBalance(tNode);
            if (type != NO_NEED) tNode = balanceAVL(tNode, type);
        }
        return tNode;
    }

    public void delete(T item) {
        root = deleteItem(root, item);
    }

    private AVLNode<T> deleteItem(AVLNode<T> tNode, T item) {
        if (tNode == NIL) return NIL;
        else {
            if (item.getKey().compareTo(tNode.item.getKey()) == 0) tNode = deleteNode(tNode);
            else if (tNode.item.getKey().compareTo(item.getKey()) > 0) {
                tNode.left = deleteItem(tNode.left, item);
                tNode.height = 1 + Math.max(tNode.right.height, tNode.left.height);
                int type = needBalance(tNode);
                if (type != NO_NEED) tNode = balanceAVL(tNode, type);
            }
            return tNode;
        }
    }

    private AVLNode<T> deleteNode(AVLNode<T> tNode) {
        if (tNode.left == NIL && tNode.right == NIL) {
            return NIL;
        } else if (tNode.left == NIL) {
            return tNode.right;
        } else if (tNode.right == NIL) {
            return tNode.left;
        } else {
            ReturnPair<T> rPair = deleteMinItem(tNode.right);
            tNode.item = rPair.item;
            tNode.right = rPair.node;
            tNode.height = 1 + Math.max(tNode.right.height, tNode.left.height);
            int type = needBalance(tNode);
            if (type != NO_NEED) tNode = balanceAVL(tNode, type);
            return tNode;
        }
    }

    private ReturnPair<T> deleteMinItem(AVLNode<T> tNode) {
        if (tNode.left == NIL) return new ReturnPair<>(tNode.item, tNode.right);
        else {
            ReturnPair<T> rPair = deleteMinItem(tNode.left);
            tNode.left = rPair.node;
            tNode.height = 1 + Math.max(tNode.right.height, tNode.left.height);
            int type = needBalance(tNode);
            if (type != NO_NEED) tNode = balanceAVL(tNode, type);
            rPair.node = tNode;
            return rPair;
        }
    }

    private class ReturnPair<V> {
        V item;
        AVLNode<V> node;

        private ReturnPair(V it, AVLNode<V> nd) {
            item = it;
            node = nd;
        }
    }

    private int needBalance(AVLNode<T> t) {
        int type = ILLEGAL;
        if (t.left.height + 2 <= t.right.height) {
            if (t.right.left.height <= t.right.right.height) type = RR;
            else type = RL;
        } else if (t.left.height >= t.right.height + 2) {
            if (t.left.left.height >= t.left.right.height) type = LL;
            else type = LR;
        } else {
            type = NO_NEED;
        }
        return type;
    }

    private AVLNode<T> balanceAVL(AVLNode<T> tNode, int type) {
        AVLNode<T> returnNode = NIL;
        switch(type) {
            case LL : 
                returnNode = rightRotate(tNode);
                break;
            case LR :
                tNode.left = leftRotate(tNode.left);
                returnNode = rightRotate(tNode);
                break;
            case RR :
                returnNode = leftRotate(tNode);
                break;
            case RL :
                tNode.right = rightRotate(tNode.right);
                returnNode = leftRotate(tNode);
                break;
            default : System.out.println("Impossible type! Should be one of LL, LR, RR, RL");
                break;
        }
        return returnNode;
    }

    private AVLNode<T> leftRotate(AVLNode<T> t) {
        AVLNode<T> rChild = t.right;
        if (rChild == NIL) System.out.println("t's rightchild shouldn't be NIL!");
        AVLNode<T> lrChild = rChild.left;
        rChild.left = t;
        t.right = lrChild;
        t.height = 1 + Math.max(t.left.height, t.right.height);
        rChild.height = 1 + Math.max(rChild.left.height, rChild.right.height); 
        return rChild;
        }

    private AVLNode<T> rightRotate(AVLNode<T> t) {
        AVLNode<T> lChild = t.left;
        if (lChild == NIL) System.out.println("t's leftchild shouldn't be NIL!");
        AVLNode<T> lrChild = lChild.right;
        lChild.right = t;
        t.left = lrChild;
        t.height = 1 + Math.max(t.left.height, t.right.height);
        lChild.height = 1 + Math.max(lChild.left.height, lChild.right.height); 
        return lChild;
    }

    public boolean isEmpty() {
		return root == null;
	}

	public void removeAll() {
		root = null;
	}

}
