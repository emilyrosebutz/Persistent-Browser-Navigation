// Implements a circular array-based list for browser history and queue operations.
public class BrowserArrayList<T> extends BrowserQueue<T> implements Iterable<T> {

    // Constructs an empty circular array list.
    public BrowserArrayList() {
        theItems = (T[]) new Object[DEFAULT_CAPACITY];
    }

    private static final int DEFAULT_CAPACITY = 10;

    // theSize: number of elements in the list
    // theItems: underlying array
    // front/rear: keeps track of the front and rear indices
    private int theSize;
    private T[] theItems;
    private int front = 0;
    private int rear = 0;

    public void clear() {
        doClear();
    }

    private void doClear() {
        theSize = 0;
        front = 0;
        rear = 0;
        ensureCapacity(DEFAULT_CAPACITY);
    }

    public int size() {
        return theSize;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void ensureCapacity(int newCapacity) {
        if (newCapacity < theSize) {
            return;
        }

        T[] old = theItems;
        theItems = (T[]) new Object[newCapacity];

        // Uses modular arithmetic to copy elements in correct order
        for (int i = 0; i < theSize; i++) {
            theItems[i] = old[(front + i) % (old == null ? 1 : old.length)];
        }
        front = 0;
        rear = theSize % newCapacity;
    }

    public boolean add(T x) {
        if (theSize == theItems.length) {
            ensureCapacity(theSize * 2 + 1);
        }
        theItems[rear] = x;
        rear = (rear + 1) % theItems.length;
        theSize++;
        return true;
    }

    // Add at index (shifts elements to the right)
    public void add(int idx, T x) {
        if (idx < 0 || idx > theSize) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (theSize == theItems.length) {
            ensureCapacity(theSize * 2 + 1);
        }
        if (idx == theSize) {
            add(x);
            return;
        }
        int insertIdx = (front + idx) % theItems.length;
        // Shift elements right
        int cur = rear;
        while (cur != insertIdx) {
            int prev = (cur - 1 + theItems.length) % theItems.length;
            theItems[cur] = theItems[prev];
            cur = prev;
        }
        theItems[insertIdx] = x;
        rear = (rear + 1) % theItems.length;
        theSize++;
    }

    public T remove(int idx) {
        if (idx < 0 || idx >= theSize) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int removeIdx = (front + idx) % theItems.length;
        T removedItem = theItems[removeIdx];
        // Shift elements left
        for (int i = idx; i < theSize - 1; i++) {
            int fromIdx = (front + i + 1) % theItems.length;
            int toIdx = (front + i) % theItems.length;
            theItems[toIdx] = theItems[fromIdx];
        }
        rear = (rear - 1 + theItems.length) % theItems.length;
        theItems[rear] = null;
        theSize--;
        return removedItem;
    }

    @Override
    public void enqueue(Object item) {
        add((T) item);
    }

    @Override
    public void dequeue() {
        remove(0);
    }

    @Override
    public java.util.Iterator<T> iterator() {
        return new StackIterator<>(theItems, front, theSize);
    }
}
