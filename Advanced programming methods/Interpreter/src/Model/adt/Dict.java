package Model.adt;
import java.util.HashMap;
import java.util.Map;

import Model.Exceptions.ADTException;
import Model.Value.StringValue;

public class Dict<T1,T2> implements IDict<T1,T2> {
    Map<T1, T2> dictionary;

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
    public void remove(T1 id) throws ADTException{
        if(dictionary.containsKey(id))
            dictionary.remove(id);
        else
            throw new ADTException("Id not found !");
    }

    @Override
    public boolean isDefined(T1 id) {
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

    @Override
    public Map<T1, T2> getMap() {
        return dictionary;
    }

    @Override
    public IDict<T1, T2> clone() {
        Dict<T1,T2> clonedDict = new Dict<T1,T2>();

        for(Map.Entry<T1,T2> entry : dictionary.entrySet()){
            T1 key = entry.getKey();
            T2 value = entry.getValue();

            clonedDict.getMap().put(key, value);
        }
        return clonedDict;
    }
}
