package com.example.stock.dirkfw.start.mapping;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.stock.dirkfw.annotation.db.TableColumnName;
import com.example.stock.dirkfw.db.util.DBTypes;
import com.example.stock.dirkfw.err.NoSetterAvailable;
import com.example.stock.dirkfw.err.db.NonSqlTypeErr;

public class FieldInfo {
    Field reflectField;

    Method getter;
    Method setter;
    String tableColumnName;

    public String getTableColumnName() {
        return tableColumnName;
    }

    public void setTableColumnName(String tableColumnName) {
        this.tableColumnName = tableColumnName;
    }

    public FieldInfo(Field value, Method getter, Method setter) {
        this.reflectField = value;
        this.getter = getter;
        this.setter = setter;
        this.tableColumnName = getTableColumnName(value);
    }

    public Field getReflectField() {
        return reflectField;
    }
    public void setReflectField(Field reflectField) {
        this.reflectField = reflectField;
    }

    public String getDFWName(){
        return this.getTableColumnName();
    }

    public void setGetter(Method getter) {
        this.getter = getter;
    }

    public void setSetter(Method setter) {
        this.setter = setter;
    }

    public Object getFieldValue(Object o) throws IllegalAccessException, InvocationTargetException{
        return this.getter.invoke(o);
    }

    public Object setFieldValue(Object o,Object set) throws IllegalAccessException, InvocationTargetException{
         return this.setter.invoke(o,set);
    }

    public static String getTableColumnName(Field field) {
        if (field.isAnnotationPresent(TableColumnName.class)) {
            return field.getAnnotation(TableColumnName.class).value();
        }else{
            return field.getName();
        }
    }

    public  void mapResultColumnIntoField( Object toFill, ResultSet result)
            throws SQLException, ReflectiveOperationException, NonSqlTypeErr, NoSetterAvailable {
        Field field = this.getReflectField();
        String columnName = this.getTableColumnName();
        Class<?> type = field.getType();
        Object value = DBTypes.readValue(result, columnName, type);
        this.setFieldValue(toFill, value);
    }
}
