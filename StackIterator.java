
import java.util.Iterator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

// Iterator for BrowserStack: traverses from oldest to newest (bottom to top).
public class StackIterator<T> implements Iterator<T> {
    // Array mode fields
    private T[] array;
    private int arrayFront;
    private int arraySize;
    private int arrayCurrent;
    private boolean isArrayMode = false;

    // Linked list mode fields
    private BrowserLinkedList.Node<T> current;
    private BrowserLinkedList.Node<T> tail;
    private BrowserLinkedList<T> list;
    private int expectedModCount;

    // ArrayList mode constructor
    public StackIterator(T[] array, int front, int size) {
        this.array = array;
        this.arrayFront = front;
        this.arraySize = size;
        this.arrayCurrent = 0;
        this.isArrayMode = true;
    }

    // LinkedList mode constructor
    public StackIterator(BrowserLinkedList<T> list) {
        this.list = list;
        this.current = list.getHead().next;
        this.tail = list.getTail();
        this.expectedModCount = list.getModCount();
        this.isArrayMode = false;
    }

    @Override
    public boolean hasNext() {
        if (isArrayMode) {
            return arrayCurrent < arraySize;
        } else {
            return current != tail;
        }
    }

    @Override
    public T next() {
        if (isArrayMode) {
            if (!hasNext())
                throw new NoSuchElementException();
            T item = array[(arrayFront + arrayCurrent) % array.length];
            arrayCurrent++;
            return item;
        } else {
            if (list.getModCount() != expectedModCount)
                throw new ConcurrentModificationException();
            if (!hasNext())
                throw new NoSuchElementException();
            T nextItem = current.data;
            current = current.next;
            return nextItem;
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove not supported in StackIterator");
    }
}
