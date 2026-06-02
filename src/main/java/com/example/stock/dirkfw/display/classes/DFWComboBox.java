package com.example.stock.dirkfw.display.classes;

import java.awt.Component;
import java.lang.reflect.Method;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.example.stock.dirkfw.annotation.db.ManytoOne;
import com.example.stock.dirkfw.display.interfaces.DFWInput;
import com.example.stock.dirkfw.start.mapping.FieldInfo;

public class DFWComboBox extends JComboBox<Object> implements DFWInput {

    private final FieldInfo fieldInfo;
    private final Object object;
    private final String displayMethodName;

    public DFWComboBox(FieldInfo fieldInfo, Object object, Vector<Object> items) {
        super(buildItemsWithNullOption(fieldInfo, items));
        this.fieldInfo = fieldInfo;
        this.object = object;
        this.displayMethodName = resolveDisplayMethodName();

        setRenderer(createRenderer());

        try {
            Object currentValue = fieldInfo.getFieldValue(object);
            if (currentValue != null) {
                setValue(currentValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Vector<Object> buildItemsWithNullOption(FieldInfo fieldInfo, Vector<Object> items) {
        Vector<Object> result = new Vector<>();
        
        // Add "tous" option if the field is nullable (non-primitive type)
        if (isNullable(fieldInfo)) {
            result.add(null);
        }
        
        if (items != null) {
            result.addAll(items);
        }
        
        return result;
    }

    private static boolean isNullable(FieldInfo fieldInfo) {
        Class<?> fieldType = fieldInfo.getReflectField().getType();
        // Objects are nullable, primitives are not
        return !fieldType.isPrimitive();
    }

    private String resolveDisplayMethodName() {
        ManytoOne annotation = fieldInfo.getReflectField().getAnnotation(ManytoOne.class);
        if (annotation == null) {
            return "";
        }
        return annotation.toDisplayOnCombobox();
    }

    private ListCellRenderer<? super Object> createRenderer() {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setText(resolveDisplayText(value));
                return this;
            }
        };
    }

    private String resolveDisplayText(Object value) {
        if (value == null) {
            return "";
        }

        if (displayMethodName == null || displayMethodName.isBlank()) {
            return value.toString();
        }

        try {
            Method method = value.getClass().getMethod(displayMethodName);
            Object result = method.invoke(value);
            return result == null ? "" : result.toString();
        } catch (Exception e) {
           e.printStackTrace();
           return "";
        }
    }

    @Override
    public Object getValue() {
         System.out.println(""+getSelectedItem());
        return getSelectedItem();
    }

    @Override
    public void setValue(Object value) {
        if (value == null) {
            setSelectedItem(null);
            return;
        }

        Object match = findMatchingItem(value);
        setSelectedItem(match != null ? match : value);
    }

    private Object findMatchingItem(Object value) {
        Object valueId = extractId(value);

        for (int i = 0; i < getItemCount(); i++) {
            Object item = getItemAt(i);
            if (item == value || (item != null && item.equals(value))) {
                return item;
            }

            Object itemId = extractId(item);
            if (itemId != null && itemId.equals(valueId)) {
                return item;
            }
        }
        return null;
    }

    private Object extractId(Object value) {
        if (value == null) {
            return null;
        }

        try {
            Method getter = value.getClass().getMethod("getId");
            return getter.invoke(value);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public FieldInfo getFieldInfo() {
        return fieldInfo;
    }

    public Object getObject() {
       
        return object;
    }
}