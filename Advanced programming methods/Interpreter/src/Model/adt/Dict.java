package Model.adt;
import java.util.HashMap;
import java.util.Map;

import Model.Exceptions.ADTException;

public class Dict<T1,T2> implements IDict<T1,T2> {
    HashMap<T1, T2> dictionary;

    public Dict() {
        dictionary = new HashMap<>();
    }

    @Override
    public void add(T1 v1, T2 v2) throws ADTException {
        if(dictionary.containsKey(v1))
            throw new ADTException("Can not add element!");
        dictionary.put(v1,v2);
    }

    @Override
    public void update(T1 v1, T2 v2) {
        dictionary.put(v1,v2);
    }

    @Override
    public T2 lookup(T1 id) {
        T2 rez = dictionary.get(id);
        return rez;
    }

    @Override
    public boolean isDefined(String id) {
        return dictionary.containsKey(id);
    }

    @Override
    public String toString() {
        StringBuilder final_string = new StringBuilder();
        for (Map.Entry<T1, T2> elem : dictionary.entrySet()) {
            final_string.append(elem.getKey()).append("-").append(elem.getValue()).append(" ");
        }
        return final_string.toString();
    }
}
