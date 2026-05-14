package com.example.stock.dirkfw.dao.start.query;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.example.stock.dirkfw.dao.DBTypes;
import com.example.stock.dirkfw.dao.start.mapping.*;
import com.example.stock.dirkfw.err.NonSqlTypeErr;

public class QueryFiller {
     public static int fillpstmtFromIndex(PreparedStatement pstmt, TableMap tableMap, Object o, int index)
            throws SQLException, NonSqlTypeErr, ReflectiveOperationException {
        for (FieldInfo fInfo : tableMap.getAllFieldWithoutID()) {
            try {
                Object value = fInfo.getFieldValue(o);
                if (value == null) {
                    pstmt.setNull(index++, DBTypes.getSqlType(fInfo.getReflectField().getType()));
                } else {
                    pstmt.setObject(index++, value);
                }
            } catch (SQLException e) {
                throw e;
            }

        }

        return index;
    }

    public static int fillpstmt(PreparedStatement pstmt, TableMap tableMap, Object o)
            throws SQLException, NonSqlTypeErr, ReflectiveOperationException {
        int index = 1;
        return fillpstmtFromIndex(pstmt, tableMap, o, index);
    }

    public static int fillWhere(PreparedStatement pstmt, TableMap tableMap, Object where)
        throws ReflectiveOperationException, SQLException {

    int index = 1;

    for (FieldInfo fi : tableMap.getAllFieldWithoutID()) {
        Object value = fi.getFieldValue(where);
        if (value==null) {
            pstmt.setObject(index++, value);
        }
    }

    return index;
}

    
}
