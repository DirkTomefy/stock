package com.example.stock.dirkfw.db.start.mapping;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FieldInfo {
    Field reflectField;
    

    Method getter;
    Method setter;
    public FieldInfo(Field value, Method getter, Method setter) {
        this.reflectField = value;
        this.getter = getter;
        this.setter = setter;
    }

    public Field getReflectField() {
        return reflectField;
    }
    public void setReflectField(Field reflectField) {
        this.reflectField = reflectField;
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
}
