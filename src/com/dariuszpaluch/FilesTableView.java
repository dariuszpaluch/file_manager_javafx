package com.dariuszpaluch;


import com.dariuszpaluch.FileRow;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class FilesTableView extends TableView {
    private final String[] COLUMNS_NAMES = {"Name", "Ext", "Size", "Date", "Attr"};
    private final String[] COLUMNS_NAMES_IN_FILE_DATA_CLASS = {"name", "ext", "size", "date", "attr"};

    public class ColumnData {
        String name;
        String fieldName;
        Double width;
        boolean resizable;
        public ColumnData(String name, String fieldName, Double width, boolean resizable) {
            this.name = name;
            this.fieldName = fieldName;
            this.width = width;
            this.resizable = resizable;
        }


    }

    private final ColumnData[] COLUMNS_DATA = {
            new ColumnData("Name", "name", null, true),
            new ColumnData("Ext", "ext", 100.0, false),
            new ColumnData("Size", "size", null, true),
            new ColumnData("Date", "date", 120.0, true),
            new ColumnData("Attr", "attr", 50.0, false)
    };

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

        BufferedImage folderIcon = null;
        try {
            folderIcon = ImageIO.read(new File("ic_folder.png"));
        } catch (IOException e) {
        }


        for(int i = 0; i < COLUMNS_NAMES.length; i++) {
            ColumnData item = COLUMNS_DATA[i];
            TableColumn column = new TableColumn( item.name);
            column.setCellValueFactory(new PropertyValueFactory<FileRow, String>( item.fieldName));
            column.setResizable(item.resizable);
            if(item.width != null) {
                column.setPrefWidth(item.width);
            }
            columns.add(column);
        }

        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    }
}
