package Model.adt;

import Model.Exceptions.ADTException;

import java.util.*;
import java.util.LinkedList;
import java.util.Queue;

public class List<T> implements IList<T> {
    ArrayList<T> list;

    public List(){
        list = new ArrayList<>();
    }

    @Override
    public void add(T v) {
        list.add(v);
    }

    @Override
    public T pop() throws ADTException {
        if(list.size() == 0)
            throw new ADTException("List is empty !");
        else
            return list.remove(list.size()-1);
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
