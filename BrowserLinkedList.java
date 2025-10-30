// Implements a doubly linked list for stack and history operations.
public class BrowserLinkedList<T> extends BrowserStack<T> implements Iterable<T> {

    // Initializes an empty doubly linked list.
    public BrowserLinkedList() {
        doClear();
    }

    // Node class for doubly linked list
    public static class Node<T> {
        public T data;
        public Node<T> next;
        public Node<T> prev;

        public Node(T d, Node<T> p, Node<T> n) {
            data = d;
            prev = p;
            next = n;
        }
    }

    private Node<T> head, tail;
    private int size;
    private int modCount = 0;

    // Clears the list and resets pointers
    public void clear() {
        doClear();
    }

    private void doClear() {
        head = new Node<T>(null, null, null);
        tail = new Node<T>(null, head, null);
        head.next = tail;

        size = 0;
        modCount++;
    }

    // Returns the number of elements in the list
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // Adds an element to the end or at a specific index
    public boolean add(T x) {
        add(size(), x);
        return true;
    }

    public void add(int idx, T x) {
        addBefore(getNode(idx, 0, size()), x);
    }

    public T remove(int idx) {
        return remove(getNode(idx));
    }

    // Pushes an item onto the stack
    public void push(T item) {
        add(item);
    }

    // Pops the top item from the stack
    public T pop() {
        if (isEmpty()) {
            throw new java.util.EmptyStackException();
        }
        return remove(size() - 1);
    }

    private void addBefore(Node<T> p, T x) {
        Node<T> newNode = new Node<>(x, p.prev, p);
        newNode.prev.next = newNode;
        p.prev = newNode;
        size++;
        modCount++;
    }

    private T remove(Node<T> p) {
        p.next.prev = p.prev;
        p.prev.next = p.next;
        size--;
        modCount++;

        return p.data;
    }

    private Node<T> getNode(int idx) {
        return getNode(idx, 0, size() - 1);
    }

    private Node<T> getNode(int idx, int lower, int upper) {
        Node<T> p;

        if (idx < lower || idx > upper)
            throw new IndexOutOfBoundsException();

        if (idx < size() / 2) {
            p = head.next;
            for (int i = 0; i < idx; i++)
                p = p.next;
        } else {
            p = tail;
            for (int i = size(); i > idx; i--)
                p = p.prev;
        }

        return p;
    }

    // Expose head, tail, and modCount for StackIterator
    Node<T> getHead() {
        return head;
    }

    Node<T> getTail() {
        return tail;
    }

    int getModCount() {
        return modCount;
    }

    public java.util.Iterator<T> iterator() {
        return new StackIterator<>(this);
    }

}
