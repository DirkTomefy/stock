package com.example.stock.dirkfw.db.query;

import java.util.HashMap;

import com.example.stock.dirkfw.db.util.ComparaisonOperation;
import com.example.stock.dirkfw.err.db.NoFieldIDErr;
import com.example.stock.dirkfw.start.mapping.*;


public class QueryMaker {
    
    private static String buildColumnList(FieldInfo[] fields, String separator) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) builder.append(separator);
            builder.append(fields[i].getTableColumnName());
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
        return getQueryForInsert(tableMap, tableMap.getTableName(), o);
    }

    public static String getQueryForInsert(TableMap tableMap, String tableName, Object o) {
        FieldInfo[] fields = tableMap.getAllFieldWithoutID();
        String columns = buildColumnList(fields, ", ");
        String placeholders = buildPlaceholderList(fields);
        return "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + placeholders + ")";
    }

    public static String getQueryForUpdate(TableMap tableMap, Object o)
            throws NoFieldIDErr {
        return getQueryForUpdate(tableMap, tableMap.getTableName(), o);
    }

    public static String getQueryForUpdate(TableMap tableMap, String tableName, Object o)
            throws NoFieldIDErr {
        FieldInfo[] fields = tableMap.getAllFieldWithoutID();
        StringBuilder setClause = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) setClause.append(", ");
            setClause.append(fields[i].getTableColumnName()).append(" = ?");
        }
        String idField = tableMap.getFieldID().getTableColumnName();
        return "UPDATE " + tableName + " SET " + setClause + " WHERE " + idField + " = ?";
    }

    public static String getQueryForGetAll(TableMap tableMap) {
        return getQueryForGetAll(tableMap, tableMap.getTableName());
    }

    public static String getQueryForGetAll(TableMap tableMap, String tableName) {
        return "SELECT * FROM " + tableName;
    }

    public static String getQueryForDelete(TableMap tableMap) {
        return getQueryForDelete(tableMap, tableMap.getTableName());
    }

    public static String getQueryForDelete(TableMap tableMap, String tableName) {
        String idField = tableMap.getFieldID().getTableColumnName();
        return "DELETE FROM " + tableName + " WHERE " + idField + " = ?";
    }

    public static String getQueryForFindByID(TableMap tableMap) {
        return getQueryForFindByID(tableMap, tableMap.getTableName());
    }

    public static String getQueryForFindByID(TableMap tableMap, String tableName) {
        String idField = tableMap.getFieldID().getTableColumnName();
        return "SELECT * FROM " + tableName + " WHERE " + idField + " = ?";
    }

    
    public static String getQueryForSelectWhere(TableMap tableMap, Object where)
            throws ReflectiveOperationException {

        return getQueryForSelectWhere(tableMap, tableMap.getTableName(), where);
    }

    public static String getQueryForSelectWhere(TableMap tableMap, String tableName, Object where)
            throws ReflectiveOperationException {

        StringBuilder query = new StringBuilder("SELECT * FROM ").append(tableName);
        StringBuilder whereClause = new StringBuilder();

        for (FieldInfo fi : tableMap.getAllFieldWithoutID()) {
            Object value;
            try {
                value = fi.getDatabaseValue(tableMap,where);
            } catch (Exception e) {
                throw new ReflectiveOperationException(e);
            }
            if (value != null) {
                if (whereClause.length() == 0) {
                    whereClause.append(" WHERE ");
                } else {
                    whereClause.append(" AND ");
                }
                whereClause.append(fi.getTableColumnName()).append(" = ?");
            }
        }

         Object idValue = tableMap.getIdFieldValue(where);
         if (idValue != null) {
            if (whereClause.length() == 0) {
                whereClause.append(" WHERE ");
            } else {
                whereClause.append(" AND ");
            }
            whereClause.append(tableMap.getFieldID().getTableColumnName()).append(" = ?");
        }

        query.append(whereClause);
        return query.toString();
    }

    public static String getQueryForSelectWhereWithOperations(TableMap tableMap, Object where,
            HashMap<String, ComparaisonOperation> operations)
            throws ReflectiveOperationException {

        return getQueryForSelectWhereWithOperations(tableMap, tableMap.getTableName(), where, operations);
    }

    public static String getQueryForSelectWhereWithOperations(TableMap tableMap, String tableName, Object where,
            HashMap<String, ComparaisonOperation> operations)
            throws ReflectiveOperationException {

        StringBuilder query = new StringBuilder("SELECT * FROM ").append(tableName);
        StringBuilder whereClause = new StringBuilder();

        for (FieldInfo fi : tableMap.getAllFieldWithoutID()) {
            Object value;
            try {
                value = fi.getDatabaseValue(tableMap,where);
            } catch (Exception e) {
                throw new ReflectiveOperationException(e);
            }
            if (value != null) {
                if (whereClause.length() == 0) {
                    whereClause.append(" WHERE ");
                } else {
                    whereClause.append(" AND ");
                }
                
                ComparaisonOperation op = operations.getOrDefault(fi.getTableColumnName(), ComparaisonOperation.EQ);
                whereClause.append(fi.getTableColumnName()).append(" ").append(getOperatorSymbol(op)).append(" ?");
            }
        }

         Object idValue = tableMap.getIdFieldValue(where);
         if (idValue != null) {
            if (whereClause.length() == 0) {
                whereClause.append(" WHERE ");
            } else {
                whereClause.append(" AND ");
            }
            ComparaisonOperation op = operations.getOrDefault(tableMap.getFieldID().getTableColumnName(), ComparaisonOperation.EQ);
            whereClause.append(tableMap.getFieldID().getTableColumnName()).append(" ").append(getOperatorSymbol(op)).append(" ?");
        }

        query.append(whereClause);
        return query.toString();
    }

    private static String getOperatorSymbol(ComparaisonOperation op) {
        switch (op) {
            case INF:
                return "<";
            case SUP:
                return ">";
            case INFEQ:
                return "<=";
            case SUPEQ:
                return ">=";
            case EQ:
                return "=";
            case NEQ:
                return "!=";
            default:
                return "=";
        }
    }

    public static String getQueryForHydrateOneToMany(TableMap parentMap, TableMap childMap, String foreignKeyColumn) {
        return "SELECT * FROM " + childMap.getTableName() + " WHERE " + foreignKeyColumn + " = ?";
    }
}
