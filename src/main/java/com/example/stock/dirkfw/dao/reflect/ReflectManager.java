package com.example.stock.dirkfw.dao.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import com.example.stock.dirkfw.annotation.db.IdField;
import com.example.stock.dirkfw.annotation.db.IgnoreDbOpperation;
import com.example.stock.dirkfw.dao.DBTypes;
import com.example.stock.dirkfw.dao.start.mapping.FieldInfo;
import com.example.stock.dirkfw.dao.start.mapping.TableMap;
import com.example.stock.dirkfw.err.NoGetterAvailable;
import com.example.stock.dirkfw.err.NoSetterAvailable;
import com.example.stock.dirkfw.err.NonSqlTypeErr;

public class ReflectManager {

    public static void mapResultColumnIntoField(FieldInfo fieldInfo, Object toFill, ResultSet result)
            throws SQLException, ReflectiveOperationException, NonSqlTypeErr, NoSetterAvailable {
        Field field = fieldInfo.getReflectField();
        String columnName = field.getName();
        Class<?> type = field.getType();
        Object value = DBTypes.readValue(result, columnName, type);
        fieldInfo.setFieldValue(toFill, value);
    }

    public static void mapResultIntoObject(
            Object toFill,
            ResultSet result,
            TableMap tableMap) throws ReflectiveOperationException, SQLException, NoSetterAvailable, NonSqlTypeErr {

        for (FieldInfo fieldInfo : tableMap.getAllFieldWithoutID()) {
            mapResultColumnIntoField(fieldInfo, toFill, result);
        }

        FieldInfo fieldId = tableMap.getFieldID();
        mapResultColumnIntoField(fieldId, toFill, result);

    }

    public static Method getterField(Class<?> clazz, Field f) throws NoGetterAvailable {
        String fieldName = f.getName();

        String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        try {
            Method getter = clazz.getMethod(getterName);
            return getter;
        } catch (NoSuchMethodException ex) {
            throw new NoGetterAvailable(fieldName, clazz.getName(), ex);
        }
    }

    public static Method setterField(Class<?> clazz, Field f) throws NoSetterAvailable {
        String fieldName = f.getName();

        String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        try {
            java.lang.reflect.Method setter = clazz.getMethod(setterName, f.getType());
            return setter;
        } catch (NoSuchMethodException ex) {
            throw new NoSetterAvailable(fieldName, clazz.getName(), ex);
        }
    }

    public static HashMap<String, TableMap> getAllClassFromPackage(String packageName) throws Exception {

        HashMap<String, TableMap> result = new HashMap<>();

        try {
            Reflections reflections = new Reflections(packageName, new SubTypesScanner(false));
            Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);
            System.out.println("" + classes.size());
            for (Class<?> clazz : classes) {
                result.put(clazz.getName(), buildTableMap(clazz));
            }

        } catch (Exception e) {
            throw e;
        }

        return result;
    }

    private static TableMap buildTableMap(Class<?> clazz) throws Exception {

        TableMap tableMap = new TableMap();
        tableMap.setTableName(clazz.getSimpleName());

        List<FieldInfo> fieldInfos = new ArrayList<>();
        tableMap.setConstructor(clazz.getConstructor());

        for (Field field : clazz.getDeclaredFields()) {
            if (!isFieldOpperable(field)) 
                continue;
            try {
                FieldInfo inf = new FieldInfo(
                        field, ReflectManager.getterField(clazz, field),
                        ReflectManager.setterField(clazz, field));
                if (ReflectManager.isIdField(field)) {
                    tableMap.setFieldID(inf);
                } else {
                    fieldInfos.add(inf);
                }
            } catch (NoGetterAvailable | NoSetterAvailable e) {
                throw e;
            }

        }

        tableMap.setAllFieldWithoutID(fieldInfos.toArray(new FieldInfo[0]));

        return tableMap;
    }

    public static boolean isFieldOpperable(Field field) {
        return !field.isAnnotationPresent(IgnoreDbOpperation.class);
    }

    public static boolean isIdField(Field field) {
        return field.isAnnotationPresent(IdField.class);
    }

}
