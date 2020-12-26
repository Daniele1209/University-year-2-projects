package View.GUI;

import Controller.Controller;
import Model.Exceptions.ADTException;
import Model.Exceptions.Custom_Exception;
import Model.PrgState;
import Model.Value.IValue;
import Model.Value.StringValue;
import Model.adt.IDict;
import Model.adt.IHeap;
import Model.adt.IList;
import Model.adt.IStack;
import Model.stmt.IStmt;
import View.GUI.entry_heap;
import View.GUI.entry_symTable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Execution {
    Controller controller;
    PrgState last_program;

    @FXML
    private Label program_counter;

    @FXML
    private Button step_button;

    @FXML
    private ListView<Integer> programState_view;

    @FXML
    private ListView<IValue> out_view;

    @FXML
    private ListView<IStmt> exe_view;

    @FXML
    private ListView<StringValue> fileTable_view;

    @FXML
    private TableView<entry_heap> heap_table;
    @FXML
    private TableColumn<Map.Entry<Integer, IValue>, Integer> heap_address;
    @FXML
    private TableColumn<Map.Entry<Integer, IValue>, String> heap_value;

    @FXML
    private TableView<entry_symTable> symTable_table;
    @FXML
    private TableColumn<HashMap.Entry<String, Integer>, String> symTable_variable;
    @FXML
    private TableColumn<HashMap.Entry<String, Integer>, String> symTable_value;

    public Execution(Controller contr) {
        controller = contr;
        last_program = null;
    }

    @FXML
    void initialize() {
        heap_address.setCellValueFactory(new PropertyValueFactory<Map.Entry<Integer, IValue>, Integer>("address"));
        heap_value.setCellValueFactory(new PropertyValueFactory<Map.Entry<Integer, IValue>, String>("value"));
        symTable_variable.setCellValueFactory(new PropertyValueFactory<HashMap.Entry<String, Integer>, String>("name"));
        symTable_value.setCellValueFactory(new PropertyValueFactory<HashMap.Entry<String, Integer>, String>("value"));
        programState_view.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        try {
            populate_widgets();
        }
        catch(Custom_Exception | ADTException err) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Execution error !");
            error.setHeaderText("Nothing to execute ! :(");
            error.showAndWait();
        }
    }

    @FXML
    void switchState(MouseEvent event) {
        List<PrgState> states = controller.getRepo().getPrgList();
        int index = programState_view.getSelectionModel().getSelectedIndex();
        PrgState program = states.get(index);
        populate_exe(program);
        populate_symTable(program);
    }

    @FXML
    private void executeOneStep() throws ADTException, Custom_Exception {
        try{
            List<PrgState> states  = controller.getRepo().getPrgList();
            if(states.size() > 0)
                controller.executeStep();
        } catch (Custom_Exception | InterruptedException err) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Execution error !");
            error.setHeaderText("Execution error ! :(");
            error.showAndWait();
        }
        populate_widgets();
    }

    private void populate_prgState(List<PrgState> state_list) {
        List<Integer> state_id = state_list.stream().map(PrgState::getId).collect(Collectors.toList());
        programState_view.setItems(FXCollections.observableArrayList(state_id));
    }

    private void populate_out(PrgState prgState) throws ADTException {
        IList<IValue> out = prgState.getOut();
        ArrayList<IValue> out_list = new ArrayList<IValue>();
        for (int i = 0; i < out.size(); i++) {
            out_list.add(out.get(i));
        }
        out_view.setItems(FXCollections.observableArrayList(out_list));
    }

    private void populate_exe(PrgState prgState) {
        IStack<IStmt> stack = prgState.getStack();
        exe_view.setItems(FXCollections.observableArrayList(stack.toList()));
    }

    private void populate_fileTable(PrgState prgState) {
        IDict<StringValue, BufferedReader> fileTable = prgState.getFileTable();
        List<StringValue> list = new ArrayList<>(fileTable.getMap().keySet());
        fileTable_view.setItems(FXCollections.observableArrayList(list));
    }

    private void populate_heap(PrgState prgState) {
        IHeap<IValue> heap = prgState.getHeap();
        ArrayList<entry_heap> entries = new ArrayList<entry_heap>();
        for (Map.Entry<Integer, IValue> entry: heap.getMap().entrySet()) {
            entries.add(new entry_heap(entry.getKey(), entry.getValue()));
        }
        heap_table.setItems(FXCollections.observableArrayList(entries));
    }

    private void populate_symTable(PrgState prgState) {
        IDict<String, IValue> symTable = prgState.getSymTable();
        ArrayList<entry_symTable> entries = new ArrayList<entry_symTable>();
        for (Map.Entry<String, IValue> entry: symTable.getMap().entrySet()) {
            entries.add(new entry_symTable(entry.getKey(), entry.getValue()));
        }
        symTable_table.setItems(FXCollections.observableArrayList(entries));
    }

    void populate_widgets() throws Custom_Exception, ADTException {
        List<PrgState> states = controller.getRepo().getPrgList();
        PrgState current;

        if(states.size() == 0) {
            program_counter.setText("Program States: 0");

            if(last_program == null)
                throw new Custom_Exception("Program is empty !");
            else {
                current = last_program;
                last_program = null;
            }
        }
        else {
            current = states.get(0);
            last_program = states.get(0);
            program_counter.setText("Program States: " + states.size());
        }
        populate_prgState(states);
        programState_view.getSelectionModel().selectIndices(0);

        try {
            populate_out(current);
            populate_exe(current);
            populate_fileTable(current);
            populate_heap(current);
            populate_symTable(current);

        } catch(ADTException err) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error when populating !");
            error.setHeaderText("Error has occurred !");
            error.showAndWait();
        }

    }

}
