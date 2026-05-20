package com.example.stock.dirkfw.start.mapping;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.example.stock.dirkfw.DirkFwConfig;
import com.example.stock.dirkfw.db.util.DBTypes;
import com.example.stock.dirkfw.err.NoGetterAvailable;
import com.example.stock.dirkfw.err.NoSetterAvailable;
import com.example.stock.dirkfw.err.db.NonSqlTypeErr;
import com.example.stock.dirkfw.start.interfaces.DisplayableOnCombobox;
import com.example.stock.dirkfw.start.interfaces.DisplayableOnList;

public class ManyToOneFieldInfo extends FieldInfo implements DisplayableOnCombobox, DisplayableOnList {
    private Class<?> manyToOneType;

    public Method onComboboxMethod;
    public Method onListMethod;

    public ManyToOneFieldInfo(Field field, Method getter, Method setter) {
        super(field, getter, setter);
        this.manyToOneType = field.getType();
        try {
            initMethodOnCombobox();
            initMethodOnList();
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Impossible d'initialiser les méthodes d'affichage pour " + field.getName(),
                    e);
        }
    }

    public Class<?> getManyToOneType() {
        return manyToOneType;
    }

    @Override
    public Object getDatabaseValue(TableMap tableMap, Object o)
            throws ReflectiveOperationException, NonSqlTypeErr, NoGetterAvailable, NoSetterAvailable {
        Object value = getFieldValue(o);
        if (isRecursive()) {
            if (value == null) {
                return null;
            }

            TableMap relationMap = getRecursiveTableMap();
            return relationMap.getIdFieldValue(value);
        }

        if (value == null) {
            return null;
        }

        TableMap relationMap = getRelatedTableMap();
        return relationMap.getIdFieldValue(value);
    }

    @Override
    public Integer getSqlType(TableMap tableMap) throws NonSqlTypeErr, ReflectiveOperationException {
        if (isRecursive()) {
            return DBTypes.getSqlType(getRecursiveTableMap().getFieldID().getReflectField().getType());
        }

        return DBTypes.getSqlType(getRelatedTableMap().getFieldID().getReflectField().getType());
    }

    public Object buildManyToOneValue(Object idValue) throws ReflectiveOperationException {
        if (idValue == null) {
            return null;
        }

        TableMap relationMap = getRelatedTableMap();
        Object relation = relationMap.getConstructor().newInstance();
        relationMap.getFieldID().setFieldValue(relation, idValue);

        hydrateRelation(relation);
        return relation;
    }

    public Class<?> getManyToOneIdType() throws ReflectiveOperationException {
        if (isRecursive()) {
            return getRecursiveTableMap().getFieldID().getReflectField().getType();
        }

        return getRelatedTableMap().getFieldID().getReflectField().getType();
    }

    private TableMap getRelatedTableMap() throws ReflectiveOperationException {
        try {
            TableMap relationMap = DirkFwConfig.classInfos == null
                    ? null
                    : DirkFwConfig.classInfos.get(manyToOneType.getName());

            if (relationMap == null) {
                relationMap = new TableMap(manyToOneType);
            }

            return relationMap;
        } catch (Exception e) {
            throw new ReflectiveOperationException(e);
        }
    }

    @Override
    public void initMethodOnCombobox() throws Exception {
        try {
            // Try to use annotation value first if provided
            String annotName = null;
            if (getReflectField().isAnnotationPresent(com.example.stock.dirkfw.annotation.db.ManytoOne.class)) {
                annotName = getReflectField().getAnnotation(com.example.stock.dirkfw.annotation.db.ManytoOne.class)
                        .toDisplayOnCombobox();
            }

            if (annotName != null && !annotName.isEmpty()) {
                this.onComboboxMethod = manyToOneType.getMethod(annotName);
            } else {
                this.onComboboxMethod = manyToOneType.getMethod("toDisplayOnCombobox");
            }
        } catch (NoSuchMethodException e) {
            System.out.println("La méthode toDisplayOnCombobox n'existe pas pour ManyToOneFieldInfo");
            throw e;
        }
    }

    @Override
    public Method getMethodOnCombobox() throws Exception {
        return this.onComboboxMethod;
    }

    @Override
    public void initMethodOnList() throws NoSuchMethodException {
        try {
            this.onListMethod = manyToOneType.getMethod("toDisplayOnList");
        } catch (NoSuchMethodException e) {
            System.out.println("La méthode toDisplayOnList n'existe pas pour ManyToOneFieldInfo");
            throw e;
        }
    }

    @Override
    public Method getMethodOnList() throws Exception {
        return this.onListMethod;
    }

    @Override
    public void mapResultColumnIntoField(Object toFill, java.sql.ResultSet result)
            throws java.sql.SQLException, ReflectiveOperationException, NonSqlTypeErr, NoSetterAvailable {
        String columnName = this.getTableColumnName();
        Class<?> idType = getManyToOneIdType();
        Object idValue = DBTypes.readValue(result, columnName, idType);
        this.setFieldValue(toFill, buildManyToOneValue(idValue));
    }
}
