// Abstract stack implementation using BrowserLinkedList for browser navigation.
public abstract class BrowserStack<T> {

    // Pushes an item onto the stack
    public abstract void push(T item);

    // Pops the top item from the stack
    public abstract T pop();

}
