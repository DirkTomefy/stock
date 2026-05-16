package com.example.stock.dirkfw.start.mapping;

import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class OneToManyInfo {
    private Field field;
    private Method getter;
    private Method setter;
    private Field mappedByField;
    private Method childGetter;
    private Method childSetter;
    private Constructor<?> childConstructor;
    private String fieldName;
    private String mappedBy;
    private Class<?> childClass;

    public OneToManyInfo(Field field, Method getter, Method setter, Field mappedByField,
            Method childGetter, Method childSetter, Constructor<?> childConstructor,
            String mappedBy, Class<?> childClass) {
        this.field = field;
        this.getter = getter;
        this.setter = setter;
        this.mappedByField = mappedByField;
        this.childGetter = childGetter;
        this.childSetter = childSetter;
        this.childConstructor = childConstructor;
        this.fieldName = field.getName();
        this.mappedBy = mappedBy;
        this.childClass = childClass;
    }

    // Getters
    public Field getField() {
        return field;
    }

    public Method getGetter() {
        return getter;
    }

    public Method getSetter() {
        return setter;
    }

    public Field getMappedByField() {
        return mappedByField;
    }

    public Method getChildGetter() {
        return childGetter;
    }

    public Method getChildSetter() {
        return childSetter;
    }

    public Constructor<?> getChildConstructor() {
        return childConstructor;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getMappedBy() {
        return mappedBy;
    }

    public Class<?> getChildClass() {
        return childClass;
    }
}
