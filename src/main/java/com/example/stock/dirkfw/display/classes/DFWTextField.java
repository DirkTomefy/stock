package com.example.stock.dirkfw.display.classes;

import javax.swing.JTextField;

import com.example.stock.dirkfw.display.interfaces.DFWInput;
import com.example.stock.dirkfw.start.mapping.FieldInfo;

public class DFWTextField extends JTextField implements DFWInput {

    private FieldInfo fieldInfo;
    private Object object;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public DFWTextField(FieldInfo fieldInfo, Object object) {
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
            if (fieldInfo.isManyToOne()) {
                Object idValue = parseSimpleValue(text, fieldInfo.getManyToOneIdType());
                return fieldInfo.buildManyToOneValue(idValue);
            }

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return text;
    }

    private Object parseSimpleValue(String text, Class<?> type) {
        try {
            if (type == Integer.class || type == int.class) {
                return Integer.parseInt(text);
            } else if (type == Long.class || type == long.class) {
                return Long.parseLong(text);
            } else if (type == Double.class || type == double.class) {
                return Double.parseDouble(text);
            } else if (type == Float.class || type == float.class) {
                return Float.parseFloat(text);
            } else if (type == Boolean.class || type == boolean.class) {
                return Boolean.parseBoolean(text);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
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
