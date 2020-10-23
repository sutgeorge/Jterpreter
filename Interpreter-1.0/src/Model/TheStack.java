package Model;
import Exceptions.StackException;

import java.util.Stack;

public class TheStack<T> implements StackInterface<T> {
    private Stack<T> stack;

    public TheStack() {
        stack = new Stack<T>();
    }

    @Override
    public void push(T element) {
        stack.push(element);
    }

    @Override
    public T pop() throws StackException {
        if (stack.size() == 0)
            throw new StackException("Can't extract elements from an empty stack.");
        return stack.pop();
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public String toString() {
        String text = "";
        Stack<T> stackClone = (Stack<T>)stack.clone();

        int index = 0;
        while (!stackClone.empty()) {
            if (index > 0)
                text += ", ";
            text += stackClone.pop().toString();
            index++;
        }

        return text;
    }
}
