package com.example.stock.dirkfw.db.query;


import com.example.stock.dirkfw.db.start.mapping.*;
import com.example.stock.dirkfw.err.db.NoFieldIDErr;


public class QueryMaker {
    
    private static String buildColumnList(FieldInfo[] fields, String separator) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) builder.append(separator);
            builder.append(fields[i].getReflectField().getName());
        }
        return builder.toString();
    }

    private static String buildPlaceholderList(FieldInfo[] fields) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) builder.append(", ");
            builder.append("?");
        }
        return builder.toString();
    }

    public static String getQueryForInsert(TableMap tableMap, Object o) {
        FieldInfo[] fields = tableMap.getAllFieldWithoutID();
        String columns = buildColumnList(fields, ", ");
        String placeholders = buildPlaceholderList(fields);
        return "INSERT INTO " + tableMap.getTableName() + " (" + columns + ") VALUES (" + placeholders + ")";
    }

    public static String getQueryForUpdate(TableMap tableMap, Object o)
            throws NoFieldIDErr {
        FieldInfo[] fields = tableMap.getAllFieldWithoutID();
        StringBuilder setClause = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) setClause.append(", ");
            setClause.append(fields[i].getReflectField().getName()).append(" = ?");
        }
        String idField = tableMap.getFieldID().getReflectField().getName();
        return "UPDATE " + tableMap.getTableName() + " SET " + setClause + " WHERE " + idField + " = ?";
    }

    public static String getQueryForGetAll(TableMap tableMap) {
        return "SELECT * FROM " + tableMap.getTableName();
    }

    public static String getQueryForDelete(TableMap tableMap) {
        String idField = tableMap.getFieldID().getReflectField().getName();
        return "DELETE FROM " + tableMap.getTableName() + " WHERE " + idField + " = ?";
    }

    public static String getQueryForFindByID(TableMap tableMap) {
        String idField = tableMap.getFieldID().getReflectField().getName();
        return "SELECT * FROM " + tableMap.getTableName() + " WHERE " + idField + " = ?";
    }

    
    public static String getQueryForSelectWhere(TableMap tableMap, Object where)
            throws ReflectiveOperationException {

        StringBuilder query = new StringBuilder("SELECT * FROM ").append(tableMap.getTableName());
        StringBuilder whereClause = new StringBuilder();

        for (FieldInfo fi : tableMap.getAllFieldWithoutID()) {
            Object value = fi.getFieldValue(where);
            if (value != null) {
                if (whereClause.length() == 0) {
                    whereClause.append(" WHERE ");
                } else {
                    whereClause.append(" AND ");
                }
                whereClause.append(fi.getReflectField().getName()).append(" = ?");
            }
        }

         Object idValue = tableMap.getIdFieldValue(where);
         if (idValue != null) {
            if (whereClause.length() == 0) {
                whereClause.append(" WHERE ");
            } else {
                whereClause.append(" AND ");
            }
            whereClause.append(tableMap.getFieldID().getReflectField().getName()).append(" = ?");
        }

        query.append(whereClause);
        return query.toString();
    }

}
