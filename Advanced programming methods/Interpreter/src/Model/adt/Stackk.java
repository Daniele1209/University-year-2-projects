package Model.adt;
import Model.Exceptions.ADTException;

import java.util.Stack;

public class Stackk<T> implements IStack<T> {
    Stack<T> stack;

    public Stackk() {
        stack = new Stack<T>();
    }

    @Override
    public T pop() throws ADTException {
        if(stack.size() == 0)
            throw new ADTException("Stack has no elements !");
        return stack.pop();
    }

    @Override
    public void push(T v) {
        stack.push(v);
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder final_string = new StringBuilder();
        for(T elem : stack) {
            final_string.append(elem);
            final_string.append(" ");
        }
        return final_string.toString();
    }
}