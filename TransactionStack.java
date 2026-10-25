public class TransactionStack {
    private char[] array;
    private int top;
    private int capacity;

    public TransactionStack(int capacity) {
        this.capacity = capacity;
        this.array = new char[capacity];
        this.top = -1;
    }

    public void push(char c) {
        if (top < capacity - 1) {
            array[++top] = c;
        }
    }

    public char pop() {
        if (!isEmpty()) {
            return array[top--];
        }
        return '\0';
    }

    public char peek() {
        if (!isEmpty()) {
            return array[top];
        }
        return '\0';
    }

    public boolean isEmpty() {
        return top == -1;
    }
}