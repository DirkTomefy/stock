package com.example.stock.dirkfw.dao.start.mapping;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class TableMap {
    String tableName;
    FieldInfo[] allFieldWithoutID;
    FieldInfo fieldID;
    Constructor<?> constructor;

    public Constructor<?> getConstructor() {
        return constructor;
    }

    public void setConstructor(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public FieldInfo[] getAllFieldWithoutID() {
        return allFieldWithoutID;
    }

    public void setAllFieldWithoutID(FieldInfo[] allFieldWithoutID) {
        this.allFieldWithoutID = allFieldWithoutID;
    }

    public FieldInfo getFieldID() {
        return fieldID;
    }

    public void setFieldID(FieldInfo fieldID) {
        this.fieldID = fieldID;
    }

    public  Object getIdFieldValue( Object o)
            throws IllegalAccessException, InvocationTargetException {
        return this.getFieldID().getFieldValue(o);
    }

    
}
