package Model.adt;
import Model.Exceptions.ADTException;

public interface IList<T> {
    void add(T v);
    void remove(T item) throws ADTException;
    int size();
    T get(int index) throws ADTException;
    String toString();
}
