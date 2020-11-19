package Model.adt;
import Model.Exceptions.ADTException;
import java.util.Map;

public interface IHeap<T> {
    void add(Integer id, T val) throws ADTException;
    int allocate(T val);
    void deallocate(int addr);
    void update(int addr, T val);
    boolean contains(int addr);
    T get(int addr);
    Map<Integer, T> getMap();
    void setMap(Map<Integer, T> map);

}
