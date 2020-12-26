package Model.adt;
import Model.Exceptions.ADTException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    public List<T> toList() {
        List<T> list = new ArrayList<>(stack);
        Collections.reverse(list);
        return list;
    }

    @Override
    public String toString() {
        StringBuilder final_string = new StringBuilder();
        for(T elem : stack) {
            final_string.append(elem.toString());
            final_string.append("\n");
        }
        return final_string.toString();
    }
}