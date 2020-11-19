package Model.adt;

import Model.Exceptions.ADTException;

import java.util.Map;

public class Heap<T> implements IHeap<T> {
    Map<Integer, T> map;
    int location;

    @Override
    public void add(Integer id, T val) throws ADTException {
        if(!map.containsKey(id))
            map.put(id, val);
        else
            throw new ADTException("Element already in map !");
    }

    @Override
    public int allocate(T val) {
        location ++;
        map.put(location, val);

        return location;
    }

    @Override
    public void deallocate(int addr) {
        map.remove(addr);
    }

    @Override
    public void update(int addr, T val) {
        map.put(addr, val);
    }

    @Override
    public boolean contains(int addr) {
        return map.containsKey(addr);
    }

    @Override
    public T get(int addr) {
        return map.get(addr);
    }

    @Override
    public Map<Integer, T> getMap() {
        return map;
    }

    @Override
    public void setMap(Map<Integer, T> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        StringBuilder final_string = new StringBuilder();
        for(Map.Entry<Integer, T> elem : map.entrySet()) {
            final_string.append(elem.getKey());
            final_string.append("-");
            final_string.append(elem.getValue());
            final_string.append(" ");
        }

        return final_string.toString();
    }
}
