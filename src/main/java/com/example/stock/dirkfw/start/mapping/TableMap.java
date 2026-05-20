package com.example.stock.dirkfw.start.mapping;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import com.example.stock.dirkfw.annotation.db.IdField;
import com.example.stock.dirkfw.annotation.db.IgnoreDbOpperation;
import com.example.stock.dirkfw.annotation.db.OneToMany;
import com.example.stock.dirkfw.err.NoGetterAvailable;
import com.example.stock.dirkfw.err.NoSetterAvailable;
import com.example.stock.dirkfw.err.db.NonSqlTypeErr;

public class TableMap {

    private String tableName;
    private FieldInfo[] allFieldWithoutID;
    private FieldInfo fieldID;
    private Constructor<?> constructor;
    private OneToManyInfo[] oneToManyInfos;

    // =========================
    // GETTERS / SETTERS
    // =========================

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

    public OneToManyInfo[] getOneToManyInfos() {
        return oneToManyInfos;
    }

    public void setOneToManyInfos(OneToManyInfo[] oneToManyInfos) {
        this.oneToManyInfos = oneToManyInfos;
    }

    // =========================
    // CONSTRUCTOR
    // =========================

    public TableMap(Class<?> clazz) throws ReflectiveOperationException,NoGetterAvailable, NoSetterAvailable {
        initializeTable(clazz);
        initializeFields(clazz);
        initializeOneToManyFields(clazz);
    }

    // =========================
    // INITIALIZATION
    // =========================

    private void initializeTable(Class<?> clazz) throws NoSuchMethodException {
        this.setTableName(getTableNameFromAnnotation(clazz));
        this.setConstructor(clazz.getConstructor());
    }

    private void initializeFields(Class<?> clazz)
            throws NoGetterAvailable, NoSetterAvailable, NoSuchMethodException {

        List<FieldInfo> fieldInfos = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {

            if (!isFieldOpperable(field)) {
                continue;
            }

            FieldInfo fieldInfo = createFieldInfo(clazz, field);

            if (isIdField(field)) {
                this.setFieldID(fieldInfo);
            } else {
                fieldInfos.add(fieldInfo);
            }
        }

        this.setAllFieldWithoutID(fieldInfos.toArray(new FieldInfo[0]));
    }

    private void initializeOneToManyFields(Class<?> clazz) throws ReflectiveOperationException,NoGetterAvailable, NoSetterAvailable {
        List<OneToManyInfo> oneToManyList = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {
            OneToMany annotation = field.getAnnotation(OneToMany.class);
            if (annotation == null) {
                continue;
            }

            Method getter = getterField(clazz, field);
            Method setter = setterField(clazz, field);
            
            Class<?> childClass = getCollectionGenericType(field);
            if (childClass != null) {
                Field mappedByField = childClass.getDeclaredField(annotation.mappedBy());
                Method childGetter = getterField(childClass, mappedByField);
                Method childSetter = setterField(childClass, mappedByField);
                java.lang.reflect.Constructor<?> childConstructor = childClass.getConstructor();
                OneToManyInfo info = new OneToManyInfo(
                        field,
                        getter,
                        setter,
                        mappedByField,
                        childGetter,
                        childSetter,
                    childConstructor,
                        annotation.mappedBy(),
                        childClass);
                oneToManyList.add(info);
            }
        }

        this.setOneToManyInfos(oneToManyList.toArray(new OneToManyInfo[0]));
    }

    private Class<?> getCollectionGenericType(Field field) {
        java.lang.reflect.Type genericType = field.getGenericType();
        if (!(genericType instanceof java.lang.reflect.ParameterizedType)) {
            return null;
        }

        java.lang.reflect.Type[] arguments = ((java.lang.reflect.ParameterizedType) genericType).getActualTypeArguments();
        if (arguments.length == 0) {
            return null;
        }

        java.lang.reflect.Type first = arguments[0];
        if (first instanceof Class<?>) {
            return (Class<?>) first;
        }

        return null;
    }

    private FieldInfo createFieldInfo(Class<?> clazz, Field field)
            throws NoGetterAvailable, NoSetterAvailable, NoSuchMethodException {
        java.lang.reflect.Method getter = getterField(clazz, field);
        java.lang.reflect.Method setter = setterField(clazz, field);

        if (field.isAnnotationPresent(com.example.stock.dirkfw.annotation.db.RecursiveCall.class)) {
            String col = field.getAnnotation(com.example.stock.dirkfw.annotation.db.RecursiveCall.class).value();
            return new com.example.stock.dirkfw.start.mapping.RecursiveFieldInfo(field, getter, setter, col);
        }

        return new FieldInfo(
            field,
            getter,
            setter);
    }

    private String getTableNameFromAnnotation(Class<?> clazz) {
        if (clazz.isAnnotationPresent(com.example.stock.dirkfw.annotation.db.TableName.class)) {
            return clazz.getAnnotation(com.example.stock.dirkfw.annotation.db.TableName.class).value();
        } else {
            return clazz.getSimpleName();
        }
    }

    // =========================
    // OBJECT MAPPING
    // =========================

    public Object getIdFieldValue(Object o)
            throws IllegalAccessException, InvocationTargetException {

        return this.getFieldID().getFieldValue(o);
    }

    public void mapResultIntoObject(
            Object toFill,
            ResultSet result)
            throws ReflectiveOperationException,
            SQLException,
            NoSetterAvailable,
            NonSqlTypeErr {

        for (FieldInfo fieldInfo : this.getAllFieldWithoutID()) {
            fieldInfo.mapResultColumnIntoField(toFill, result);
        }

        this.getFieldID().mapResultColumnIntoField(toFill, result);
    }

   

    // =========================
    // REFLECTION METHODS
    // =========================

    public static Method getterField(Class<?> clazz, Field field)
            throws NoGetterAvailable {

        String fieldName = field.getName();

        String methodName =
                "get"
                        + fieldName.substring(0, 1).toUpperCase()
                        + fieldName.substring(1);

        try {
            return clazz.getMethod(methodName);
        } catch (NoSuchMethodException ex) {
            throw new NoGetterAvailable(fieldName, clazz.getName(), ex);
        }
    }

    public static Method setterField(Class<?> clazz, Field field)
            throws NoSetterAvailable {

        String fieldName = field.getName();

        String methodName =
                "set"
                        + fieldName.substring(0, 1).toUpperCase()
                        + fieldName.substring(1);

        try {
            return clazz.getMethod(methodName, field.getType());
        } catch (NoSuchMethodException ex) {
            throw new NoSetterAvailable(fieldName, clazz.getName(), ex);
        }
    }

    // =========================
    // PACKAGE SCAN
    // =========================

    public static HashMap<String, TableMap> getAllClassFromPackage(String packageName)
            throws Exception {

        HashMap<String, TableMap> result = new HashMap<>();

        Reflections reflections =
                new Reflections(packageName, new SubTypesScanner(false));

        Set<Class<? extends Object>> classes =
                reflections.getSubTypesOf(Object.class);

        System.out.println(classes.size());

        for (Class<?> clazz : classes) {
            result.put(clazz.getName(), new TableMap(clazz));
        }

        return result;
    }

    // =========================
    // FIELD VALIDATION
    // =========================

    public static boolean isFieldOpperable(Field field) {
        return !field.isAnnotationPresent(IgnoreDbOpperation.class)
                && !field.isAnnotationPresent(OneToMany.class)
                && !Collection.class.isAssignableFrom(field.getType());
    }

    public static boolean isIdField(Field field) {
        return field.isAnnotationPresent(IdField.class);
    }
}