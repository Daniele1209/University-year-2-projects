package Model.adt;
import Model.Exceptions.ADTException;

public interface IStack<T> {

    T pop() throws ADTException;
    void push(T v);
    boolean isEmpty();
    String toString();
}

