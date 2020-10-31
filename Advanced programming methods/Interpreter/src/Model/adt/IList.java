package Model.adt;
import Model.Exceptions.ADTException;

public interface IList<T> {
    void add(T v);
    T pop() throws ADTException;
    int size();
    String toString();
}
