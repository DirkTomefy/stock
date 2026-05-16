package com.example.stock.dirkfw.db.util;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.time.LocalDateTime;

import com.example.stock.dirkfw.err.db.NonSqlTypeErr;

public class DBTypes {
    public static final HashMap<Class<?>, Integer> SQL_TYPES = getTypeSql();
    private static HashMap<Class<?>, Integer> getTypeSql() {
        HashMap<Class<?>, Integer> types = new HashMap<>(); 

        //primitive types
        types.put(int.class, java.sql.Types.INTEGER);
        types.put(double.class, java.sql.Types.DOUBLE); 
        types.put(long.class, java.sql.Types.BIGINT);
        types.put(float.class, java.sql.Types.REAL);
        types.put(boolean.class, java.sql.Types.BOOLEAN);

        //wrapper types
        types.put(Integer.class, java.sql.Types.INTEGER);
        types.put(String.class, java.sql.Types.VARCHAR);
        types.put(Double.class, java.sql.Types.DOUBLE);
        types.put(Long.class, java.sql.Types.BIGINT);
        types.put(Float.class, java.sql.Types.REAL);
        types.put(Boolean.class, java.sql.Types.BOOLEAN);
        types.put(java.util.Date.class, java.sql.Types.DATE);
        types.put(LocalDateTime.class, java.sql.Types.TIMESTAMP);
        return types;
    }

    public static Integer getSqlType(Class<?> clazz) throws NonSqlTypeErr {
        Integer typeCode=SQL_TYPES.get(clazz);
        if (typeCode == null) {
            throw new NonSqlTypeErr(clazz);
        }
        return typeCode;
    }

     public static Object readValue(ResultSet rs, String columnName, Class<?> type)
            throws SQLException, ReflectiveOperationException, NonSqlTypeErr {

        int sqlType = DBTypes.getSqlType(type);

        Object value;

        if (sqlType == java.sql.Types.INTEGER) {
            int v = rs.getInt(columnName);
            value = rs.wasNull() ? null : v;

        } else if (sqlType == java.sql.Types.DOUBLE) {
            double v = rs.getDouble(columnName);
            value = rs.wasNull() ? null : v;

        } else if (sqlType == java.sql.Types.VARCHAR) {
            value = rs.getString(columnName);

        } else if (sqlType == java.sql.Types.DATE) {
            java.sql.Date d = rs.getDate(columnName);
            value = (d != null) ? new java.util.Date(d.getTime()) : null;

        } else if (sqlType == java.sql.Types.TIMESTAMP) {
            java.sql.Timestamp ts = rs.getTimestamp(columnName);
            value = (ts != null) ? ts.toLocalDateTime() : null;

        } else {
            throw new SQLException("Type SQL non supporté: " + sqlType);
        }

        return value;
    }
}