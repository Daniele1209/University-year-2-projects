package View.GUI;

import Model.Value.IValue;
import javafx.beans.property.SimpleStringProperty;

public class entry_heap {

    private SimpleStringProperty address;
    private SimpleStringProperty value;
    private Integer o_addr;
    private IValue o_value;

    public entry_heap(Integer i, IValue v) {
        address = new SimpleStringProperty(String.valueOf(i));
        value = new SimpleStringProperty(v.toString());
        o_addr = i;
        o_value = v.deepCopy();
    }

    public Integer getAddress() {
        return o_addr;
    }
    public IValue getValue() {
        return o_value;
    }
}
