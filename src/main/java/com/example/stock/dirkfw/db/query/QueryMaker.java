package com.example.stock.dirkfw.db.query;


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
            setClause.append(fields[i].getTableColumnName()).append(" = ?");
        }
        String idField = tableMap.getFieldID().getTableColumnName();
        return "UPDATE " + tableMap.getTableName() + " SET " + setClause + " WHERE " + idField + " = ?";
    }

    public static String getQueryForGetAll(TableMap tableMap) {
        return "SELECT * FROM " + tableMap.getTableName();
    }

    public static String getQueryForDelete(TableMap tableMap) {
        String idField = tableMap.getFieldID().getTableColumnName();
        return "DELETE FROM " + tableMap.getTableName() + " WHERE " + idField + " = ?";
    }

    public static String getQueryForFindByID(TableMap tableMap) {
        String idField = tableMap.getFieldID().getTableColumnName();
        return "SELECT * FROM " + tableMap.getTableName() + " WHERE " + idField + " = ?";
    }

    
    public static String getQueryForSelectWhere(TableMap tableMap, Object where)
            throws ReflectiveOperationException {

        StringBuilder query = new StringBuilder("SELECT * FROM ").append(tableMap.getTableName());
        StringBuilder whereClause = new StringBuilder();

        for (FieldInfo fi : tableMap.getAllFieldWithoutID()) {
            Object value;
            try {
                value = fi.getDatabaseValue(where);
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

}
