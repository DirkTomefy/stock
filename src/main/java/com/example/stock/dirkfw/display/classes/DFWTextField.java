package com.example.stock.dirkfw.display.classes;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JTextField;

import com.example.stock.dirkfw.start.mapping.FieldInfo;

public class DFWTextField extends JTextField{
    public DFWTextField(FieldInfo fieldInfo,Object o){
        super(
                getFieldValueAsString(fieldInfo,o)
        );
    }
    public static String getFieldValueAsString(FieldInfo fieldInfo,Object object) {
        try {
            Object value = fieldInfo.getFieldValue(object);
            return value != null ? value.toString() : "";
        } catch (IllegalAccessException | InvocationTargetException e) {
            return "";
        }
    }
}
