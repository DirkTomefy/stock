package com.example.stock.dirkfw.dao.start.query;

import java.lang.reflect.Field;

import com.example.stock.dirkfw.dao.start.mapping.*;
import com.example.stock.dirkfw.err.NoFieldIDErr;


public class QueryMaker {
    public static String getQueryForInsert(TableMap tableMap, Object o) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("INSERT INTO ").append(tableMap.getTableName()).append(" (");
        StringBuilder columnsBuilder = new StringBuilder();
        StringBuilder valuesBuilder = new StringBuilder();

        boolean first = true;
        for (FieldInfo fieldInfo : tableMap.getAllFieldWithoutID()) {
            if (!first) {
                columnsBuilder.append(", ");
                valuesBuilder.append(", ");
            } else {
                first = false;
            }
            columnsBuilder.append(fieldInfo.getReflectField().getName());
            valuesBuilder.append("?");
        }
        sqlBuilder.append(columnsBuilder)
                .append(") VALUES (")
                .append(valuesBuilder)
                .append(")");
        return sqlBuilder.toString();
    }

    public static String getQueryForUpdate(TableMap tableMap, Object o)
            throws NoFieldIDErr {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(" UPDATE ").append(tableMap.getTableName()).append(" SET ");

        boolean first = true;
        for (FieldInfo field : tableMap.getAllFieldWithoutID()) {
            if (!first) {
                sqlBuilder.append(", ");
            } else {
                first = false;
            }
            sqlBuilder.append(field.getReflectField().getName());
            sqlBuilder.append(" = ?");
        }

        sqlBuilder.append(" WHERE ").append(tableMap.getFieldID().getReflectField().getName()).append(" = ?");
        return sqlBuilder.toString();
    }

    public static String getQueryForGetAll(TableMap tableMap) {
        return "SELECT * FROM " + tableMap.getTableName();
    }

    public static String getQueryForDelete(TableMap tableMap) {
        return "DELETE FROM " + tableMap.getTableName() + " WHERE " + tableMap.getFieldID().getReflectField().getName()
                + " = ?";
    }

    public static String getQueryForFindByID(TableMap tableMap) {
        return "SELECT * FROM " + tableMap.getTableName() + " WHERE " + tableMap.getFieldID().getReflectField().getName()
                + " = ?";
    }

    // TODO Warnings : Tsy maka ny id izy
    public static String getQueryForSelectWhere(TableMap tableMap, Object where)
            throws ReflectiveOperationException {

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ").append(tableMap.getTableName());

        StringBuilder whereClause = new StringBuilder();

        for (FieldInfo fi : tableMap.getAllFieldWithoutID()) {

            Field field = fi.getReflectField();
            Object value = fi.getFieldValue(where);

            if (value == null) {

                if (whereClause.length() == 0) {
                    whereClause.append(" WHERE ");
                } else {
                    whereClause.append(" AND ");
                }

                whereClause.append(field.getName()).append(" = ?");
            }
        }

        query.append(whereClause);

        return query.toString();
    }

}
