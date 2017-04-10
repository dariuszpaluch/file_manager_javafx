package com.dariuszpaluch;


import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class FilesTableView extends TableView {
    private final String[] COLUMNS_NAMES = {"Name", "Ext", "Size", "Date", "Attr"};
    private final String[] COLUMNS_NAMES_IN_FILE_DATA_CLASS = {"name", "ext", "size", "date", "attr"};

    private ObservableList<FileRow> rows;
    public FilesTableView() {
        this.initialize();
    }

    public FilesTableView(ObservableList items) {
        super(items);
        this.initialize();
    }

    private void initialize() {
        this.initialColumns();
    }

    private void initialColumns() {
        ObservableList columns = this.getColumns();

        for(int i = 0; i < COLUMNS_NAMES.length; i++) {
            TableColumn column = new TableColumn(COLUMNS_NAMES[i]);
            column.setCellValueFactory(new PropertyValueFactory<FileRow, String>(COLUMNS_NAMES_IN_FILE_DATA_CLASS[i]));
            columns.add(column);
        }
    }
}
