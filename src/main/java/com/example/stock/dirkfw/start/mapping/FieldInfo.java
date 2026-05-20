package com.example.stock.dirkfw.start.mapping;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.stock.dirkfw.DirkFwConfig;
import com.example.stock.dirkfw.annotation.db.ManytoOne;
import com.example.stock.dirkfw.annotation.db.RecursiveCall;
import com.example.stock.dirkfw.annotation.db.TableColumnName;
import com.example.stock.dirkfw.db.GenericDao;
import com.example.stock.dirkfw.db.util.DBTypes;
import com.example.stock.dirkfw.err.NoGetterAvailable;
import com.example.stock.dirkfw.err.NoSetterAvailable;
import com.example.stock.dirkfw.err.db.NonSqlTypeErr;

public class FieldInfo{
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

    public void setGetter(Method getter) {
        this.getter = getter;
    }

    public void setSetter(Method setter) {
        this.setter = setter;
    }

    

    public Object getFieldValue(Object o) throws IllegalAccessException, InvocationTargetException{
        return this.getter.invoke(o);
    }

    public Object getDatabaseValue(TableMap tableMap,Object o) throws ReflectiveOperationException, NonSqlTypeErr, NoGetterAvailable, NoSetterAvailable {
        Object value = getFieldValue(o);
        if (isRecursive()) {
            if (value == null) {
                return null;
            }

            TableMap relationMap = getRecursiveTableMap();
            return relationMap.getIdFieldValue(value);
        }
        return value;
    }

    public Object setFieldValue(Object o,Object set) throws IllegalAccessException, InvocationTargetException{
         return this.setter.invoke(o,set);
    }

    public static String getTableColumnName(Field field) {
        if (field.isAnnotationPresent(RecursiveCall.class)) {
            return field.getAnnotation(RecursiveCall.class).value();
        }
        if (field.isAnnotationPresent(ManytoOne.class)) {
            return field.getAnnotation(ManytoOne.class).joinColumn();
        }
        if (field.isAnnotationPresent(TableColumnName.class)) {
            return field.getAnnotation(TableColumnName.class).value();
        }else{
            return field.getName();
        }
    }

    public Integer getSqlType(TableMap tableMap) throws NonSqlTypeErr, ReflectiveOperationException {
        if (isRecursive()) {
            return DBTypes.getSqlType(getRecursiveTableMap().getFieldID().getReflectField().getType());
        }
        return DBTypes.getSqlType(this.reflectField.getType());
    }

    

    public boolean isRecursive() {
        return this.reflectField.isAnnotationPresent(RecursiveCall.class);
    }

    protected TableMap getRecursiveTableMap() throws ReflectiveOperationException {
        if (!isRecursive()) {
            throw new IllegalStateException("Le champ n'est pas une relation RecursiveCall");
        }

        try {
            TableMap recursiveMap = DirkFwConfig.classInfos == null
                    ? null
                    : DirkFwConfig.classInfos.get(this.reflectField.getType().getName());

            if (recursiveMap == null) {
                recursiveMap = new TableMap(this.reflectField.getType());
            }

            return recursiveMap;
        } catch (Exception e) {
            throw new ReflectiveOperationException(e);
        }
    }

    protected Object buildRecursiveValue(Object idValue) throws ReflectiveOperationException {
        if (!isRecursive()) {
            return idValue;
        }

        if (idValue == null) {
            return null;
        }

        TableMap recursiveMap = getRecursiveTableMap();
        Object relation = recursiveMap.getConstructor().newInstance();
        recursiveMap.getFieldID().setFieldValue(relation, idValue);

        hydrateRelation(relation);
        return relation;
    }

    protected void hydrateRelation(Object relation) {
        if (relation == null) {
            return;
        }
        try (GenericDao dao = new GenericDao()) {
                dao.findById(relation);
        } catch (Exception e) {
            // Si le chargement complet échoue, on conserve au moins l'objet avec son ID.
        }
    }

    

    public  void mapResultColumnIntoField( Object toFill, ResultSet result)
            throws SQLException, ReflectiveOperationException, NonSqlTypeErr, NoSetterAvailable {
        Field field = this.getReflectField();
        String columnName = this.getTableColumnName();
        if (isRecursive()) {
            Class<?> idType = getRecursiveTableMap().getFieldID().getReflectField().getType();
            Object idValue = DBTypes.readValue(result, columnName, idType);
            this.setFieldValue(toFill, buildRecursiveValue(idValue));
            return;
        }
        Class<?> type = field.getType();
        Object value = DBTypes.readValue(result, columnName, type);
        this.setFieldValue(toFill, value);
    }

    public Method getGetter() {
        return getter;
    }

    public Method getSetter() {
        return setter;
    }

    

    

}
