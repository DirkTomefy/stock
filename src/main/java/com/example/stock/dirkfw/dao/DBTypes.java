package com.example.stock.dirkfw.dao;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.example.stock.dirkfw.err.NonSqlTypeErr;

public class DBTypes {
    public static final HashMap<Class<?>, Integer> SQL_TYPES = getTypeSql();
    private static HashMap<Class<?>, Integer> getTypeSql() {
        HashMap<Class<?>, Integer> types = new HashMap<>(); 

        //primitive types
        types.put(int.class, java.sql.Types.INTEGER);
        types.put(double.class, java.sql.Types.DOUBLE); 

        //wrapper types
        types.put(Integer.class, java.sql.Types.INTEGER);
        types.put(String.class, java.sql.Types.VARCHAR);
        types.put(Double.class, java.sql.Types.DOUBLE);
        types.put(java.util.Date.class, java.sql.Types.DATE);
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

        } else {
            throw new SQLException("Type SQL non supporté: " + sqlType);
        }

        return value;
    }
}