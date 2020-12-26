package View.GUI;

import Model.Value.IValue;
import javafx.beans.property.SimpleStringProperty;

public class entry_symTable {

    private SimpleStringProperty name;
    private SimpleStringProperty value;
    private String o_addr;
    private IValue o_value;

    public entry_symTable(String i, IValue v) {
        name = new SimpleStringProperty(String.valueOf(i));
        value = new SimpleStringProperty(v.toString());
        o_addr = i;
        o_value = v.deepCopy();
    }

    public String getName() {
        return o_addr;
    }
    public IValue getValue() {
        return o_value;
    }
}
