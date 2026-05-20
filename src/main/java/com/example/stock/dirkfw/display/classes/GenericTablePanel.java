package com.example.stock.dirkfw.display.classes;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.example.stock.dirkfw.DirkFwConfig;
import com.example.stock.dirkfw.annotation.display.DisplayOnList;
import com.example.stock.dirkfw.annotation.display.IgnoreDisplayOpperation; 
import com.example.stock.dirkfw.annotation.display.PrimaryOnList;
import com.example.stock.dirkfw.annotation.display.SkipTableList;
import com.example.stock.dirkfw.start.interfaces.DisplayableOnCombobox;
import com.example.stock.dirkfw.start.mapping.FieldInfo;
import com.example.stock.dirkfw.start.mapping.TableMap;

public class GenericTablePanel extends JTable {

    private Vector<Object> data;
    private Vector<Integer> primaryColumns;

    public GenericTablePanel() {
        this(new Vector<>());
    }

    public GenericTablePanel(Vector<Object> data) {
        super();
        this.primaryColumns = new Vector<>();
        setData(data);
    }

    public void setData(Vector<Object> data) {
        this.data = data == null ? new Vector<>() : data;
        rebuildModel();
    }

    public Vector<Object> getData() {
        return data;
    }

    public Object getRowObject(int rowIndex) {
        return (rowIndex >= 0 && rowIndex < data.size()) ? data.get(rowIndex) : null;
    }

    private void rebuildModel() {
        DefaultTableModel model = createReadOnlyModel();

        if (data.isEmpty()) {
            setModel(model);
            return;
        }

        TableMap tableMap = getTableMapForFirstObject();
        Vector<FieldInfo> displayFields = buildDisplayableFields(tableMap);
        Vector<String> columnNames = buildColumnHeaders(displayFields);
        buildTableRows(tableMap, displayFields, columnNames, model);

        setModel(model);
        applyColumnRenderers();
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
        Object first = data.firstElement();
        return DirkFwConfig.getClassInfos().get(first.getClass().getName());
    }

    private Vector<FieldInfo> buildDisplayableFields(TableMap tableMap) {
        Vector<FieldInfo> fields = new Vector<>();

        FieldInfo idField = tableMap.getFieldID();
        if (idField != null 
                && !idField.getReflectField().isAnnotationPresent(SkipTableList.class)
                && !idField.getReflectField().isAnnotationPresent(IgnoreDisplayOpperation.class)) {
            fields.add(idField);
        }

        for (FieldInfo field : tableMap.getAllFieldWithoutID()) {
            if (!field.getReflectField().isAnnotationPresent(SkipTableList.class)
                    && !field.getReflectField().isAnnotationPresent(IgnoreDisplayOpperation.class)) {
                fields.add(field);
            }
        }

        return fields;
    }

    private Vector<String> buildColumnHeaders(Vector<FieldInfo> fields) {
        Vector<String> columns = new Vector<>();

        for (FieldInfo field : fields) {
            columns.add(field.getTableColumnName());
        }

        return columns;
    }

    private void buildTableRows(TableMap tableMap, Vector<FieldInfo> displayFields, Vector<String> columnNames, DefaultTableModel model) {
        for (String columnName : columnNames) {
            model.addColumn(columnName);
        }
        int colIndex = 0;
        for (FieldInfo field : displayFields) {
            if (field.getReflectField().isAnnotationPresent(PrimaryOnList.class)) {
                this.primaryColumns.add(colIndex);
            }
            colIndex++;
        }
        for (Object obj : data) {
            Vector<Object> row = extractRowData(displayFields, obj);
            model.addRow(row);
        }
    }

    private Vector<Object> extractRowData(Vector<FieldInfo> displayFields, Object obj) {
        Vector<Object> row = new Vector<>();

        try {
            for (FieldInfo field : displayFields) {
                if (field.getReflectField().isAnnotationPresent(IgnoreDisplayOpperation.class)) continue;

                Object value = field.getFieldValue(obj);
                Object displayValue = formatDisplayValue(field, value);
                row.add(displayValue);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return row;
    }

    private Object formatDisplayValue(FieldInfo field, Object value) {
        if (value == null) {
            return "";
        }

        if (field.getReflectField().isAnnotationPresent(DisplayOnList.class)) {
            DisplayOnList anno = field.getReflectField().getAnnotation(DisplayOnList.class);
            String methodName = anno.value();
            try {
                Method method = value.getClass().getMethod(methodName);
                Object result = method.invoke(value);
                return result == null ? "" : result.toString();
            } catch (Exception ex) {
                return value.toString();
            }
        }

        if (field instanceof DisplayableOnCombobox f) {
            try {
                Method dispMethod = f.getMethodOnCombobox();
                Object result = dispMethod.invoke(value);
                return result == null ? "" : result.toString();
            } catch (Exception ex) {
                return value.toString();
            }
        }

        return value.toString();
    }

    private void applyColumnRenderers() {
        DefaultTableCellRenderer primaryRenderer = new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                java.awt.Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (primaryColumns.contains(column)) {
                    if (isSelected) {
                        cell.setBackground(new Color(100, 149, 237));
                        cell.setForeground(Color.WHITE);
                    } else {
                        cell.setBackground(new Color(200, 220, 255));
                        cell.setForeground(Color.BLACK);
                    }
                } else {
                    if (isSelected) {
                        cell.setBackground(table.getSelectionBackground());
                        cell.setForeground(table.getSelectionForeground());
                    } else {
                        cell.setBackground(Color.WHITE);
                        cell.setForeground(Color.BLACK);
                    }
                }

                return cell;
            }
        };

        for (int i = 0; i < getColumnCount(); i++) {
            TableColumn col = getColumnModel().getColumn(i);
            col.setCellRenderer(primaryRenderer);
        }

        setRowHeight(25);
    }
}