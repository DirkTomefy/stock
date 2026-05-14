package com.example.stock.dirkfw.display.classes;

import javax.swing.JTextField;

import com.example.stock.dirkfw.DirkFwObject;
import com.example.stock.dirkfw.display.interfaces.DFWInput;
import com.example.stock.dirkfw.start.mapping.FieldInfo;

public class DFWTextField extends JTextField implements DFWInput {

    private FieldInfo fieldInfo;
    private DirkFwObject object;

    public DirkFwObject getObject() {
        return object;
    }

    public void setObject(DirkFwObject object) {
        this.object = object;
    }

    public DFWTextField(FieldInfo fieldInfo, DirkFwObject object) {
        super();
        this.fieldInfo = fieldInfo;
        this.object = object;

        // Initialiser le texte avec la valeur actuelle si elle existe
        try {
            Object value = fieldInfo.getFieldValue(object);
            if (value != null) {
                setText(value.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getValue() {
        String text = getText();
        if (text == null || text.isEmpty()) {
            return null;
        }

        Class<?> fieldType = fieldInfo.getReflectField().getType();

        try {
            if (fieldType == String.class) {
                return text;
            } else if (fieldType == Integer.class || fieldType == int.class) {
                return Integer.parseInt(text);
            } else if (fieldType == Double.class || fieldType == double.class) {
                return Double.parseDouble(text);
            } else if (fieldType == Float.class || fieldType == float.class) {
                return Float.parseFloat(text);
            } else if (fieldType == Long.class || fieldType == long.class) {
                return Long.parseLong(text);
            } else if (fieldType == Boolean.class || fieldType == boolean.class) {
                return Boolean.parseBoolean(text);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return text;
    }

    public void setValue(Object value) {
        if (value != null) {
            setText(value.toString());
        }
    }

    @Override
    public FieldInfo getFieldInfo() {
        return this.fieldInfo;
    }
}
