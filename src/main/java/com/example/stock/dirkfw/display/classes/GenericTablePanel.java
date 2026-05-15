package com.example.stock.dirkfw.display.classes;

import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.example.stock.dirkfw.DirkFwObject;
import com.example.stock.dirkfw.start.mapping.FieldInfo;
import com.example.stock.dirkfw.start.mapping.TableMap;

public class GenericTablePanel extends JTable {

    private Vector<DirkFwObject> data;

    public GenericTablePanel() {
        this(new Vector<>());
    }

    public GenericTablePanel(Vector<DirkFwObject> data) {
        super();
        setData(data);
    }

    public void setData(Vector<DirkFwObject> data) {
        this.data = data == null ? new Vector<>() : data;
        rebuildModel();
    }

    public Vector<DirkFwObject> getData() {
        return data;
    }

    public DirkFwObject getRowObject(int rowIndex) {
        return (rowIndex >= 0 && rowIndex < data.size()) ? data.get(rowIndex) : null;
    }

    private void rebuildModel() {
        DefaultTableModel model = createReadOnlyModel();

        if (data.isEmpty()) {
            setModel(model);
            return;
        }

        TableMap tableMap = getTableMapForFirstObject();
        Vector<String> columnNames = buildColumnHeaders(tableMap);
        buildTableRows(tableMap, columnNames, model);

        setModel(model);
    }

    private DefaultTableModel createReadOnlyModel() {
        return new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private TableMap getTableMapForFirstObject() {
        DirkFwObject first = data.firstElement();
        return DirkFwObject.getClassInfos().get(first.getClass().getName());
    }

    private Vector<String> buildColumnHeaders(TableMap tableMap) {
        Vector<String> columns = new Vector<>();

        FieldInfo idField = tableMap.getFieldID();
        if (idField != null) {
            columns.add(idField.getReflectField().getName());
        }

        for (FieldInfo field : tableMap.getAllFieldWithoutID()) {
            columns.add(field.getDFWName());
        }

        return columns;
    }

    private void buildTableRows(TableMap tableMap, Vector<String> columnNames, DefaultTableModel model) {
        // add column headers
        for (String columnName : columnNames) {
            model.addColumn(columnName);
        }

        // add data rows
        for (DirkFwObject obj : data) {
            Vector<Object> row = extractRowData(tableMap, columnNames.size(), obj);
            model.addRow(row);
        }
    }

    private Vector<Object> extractRowData(TableMap tableMap, int expectedSize, DirkFwObject obj) {
        Vector<Object> row = new Vector<>();

        try {
            FieldInfo idField = tableMap.getFieldID();
            if (idField != null) {
                row.add(idField.getFieldValue(obj));
            }

            for (FieldInfo field : tableMap.getAllFieldWithoutID()) {
                row.add(field.getFieldValue(obj));
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            // fill remaining cells with null for stability
            int remaining = expectedSize - row.size();
            for (int i = 0; i < remaining; i++) {
                row.add(null);
            }
        }

        return row;
    }

}
