package Model.adt;
import Model.Exceptions.ADTException;

import java.util.List;

public interface IStack<T> {

    T pop() throws ADTException;
    void push(T v);
    boolean isEmpty();
    public List<T> toList();
    String toString();
}

