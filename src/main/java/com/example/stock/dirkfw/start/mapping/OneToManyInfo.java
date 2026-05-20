package com.example.stock.dirkfw.start.mapping;

import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import com.example.stock.dirkfw.start.interfaces.DisplayableOnCombobox;
import com.example.stock.dirkfw.start.interfaces.DisplayableOnList;

public class OneToManyInfo implements DisplayableOnCombobox , DisplayableOnList{
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

    public Method onComboboxMethod;
    public Method onListMethod;

    public OneToManyInfo(Field field, Method getter, Method setter, Field mappedByField,
            Method childGetter, Method childSetter, Constructor<?> childConstructor,
            String mappedBy, Class<?> childClass) throws NoSuchMethodException   {
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
        initMethodOnCombobox();
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

    @Override
    public void initMethodOnCombobox() throws NoSuchMethodException   {
                try {
                this.onComboboxMethod = this.field.getType().getMethod("toDisplayOnCombobox");
                } catch (NoSuchMethodException e) {
                    System.out.println("La méthode dans OneToManyInfo n'existe pas");
                    throw e;
                }
    }

    @Override
    public Method getMethodOnCombobox() throws Exception {
        return this.onComboboxMethod;
    }

    @Override
    public void initMethodOnList() throws Exception {
        this.onListMethod=this.field.getType().getMethod("toDisplayOnList");
               
    }

    @Override
    public Method getMethodOnList() throws Exception {
       return this.onListMethod;
    }
}
