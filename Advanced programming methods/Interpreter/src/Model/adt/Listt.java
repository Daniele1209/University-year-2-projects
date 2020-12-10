package Model.adt;

import Model.Exceptions.ADTException;

import java.util.*;
import java.util.LinkedList;
import java.util.Queue;

public class Listt<T> implements IList<T> {
    List<T> list;

    public Listt(){
        list = new ArrayList<>();
    }

    @Override
    public void add(T v) {
        list.add(v);
    }

    @Override
    public void remove(T item) throws ADTException {
        try {
            list.remove(item);
        }
        catch (NoSuchElementException e) {
            throw new ADTException("No such element " + item);
        }
    }

    @Override
    public T get(int index) throws ADTException {
        try {
            return list.get(index);
        }
        catch (IndexOutOfBoundsException e) {
            throw new ADTException("Index out of bounds");
        }
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public String toString(){
        StringBuilder final_string = new StringBuilder();
        for(T elem : list) {
            final_string.append(elem);
            final_string.append(" ");
        }
        return final_string.toString();
    }

}
