package Model.adt;
import Model.Exceptions.ADTException;
import Model.Value.StringValue;

import java.util.Map;

public interface IDict<T1,T2>{

    void add(T1 v1, T2 v2) throws ADTException;
    void update(T1 v1, T2 v2);
    T2 lookup(T1 id);
    boolean isDefined(T1 id);
    void remove(T1 id) throws ADTException;
    String toString();
    Map<T1, T2> getMap();
    IDict<T1, T2> clone();
}
