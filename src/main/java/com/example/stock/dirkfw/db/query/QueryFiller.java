package com.example.stock.dirkfw.db.query;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.example.stock.dirkfw.db.start.mapping.*;
import com.example.stock.dirkfw.db.util.FieldProcessor;
import com.example.stock.dirkfw.err.db.NonSqlTypeErr;
public class QueryFiller {

    private static int processFields(PreparedStatement pstmt, TableMap tableMap, Object o, int index, FieldProcessor processor)
            throws SQLException, NonSqlTypeErr, ReflectiveOperationException {
        for (FieldInfo fInfo : tableMap.getAllFieldWithoutID()) {
            index = processor.process(pstmt, index, fInfo, o);
        }
        FieldInfo idField = tableMap.getFieldID();
        index = processor.process(pstmt, index, idField, o);
        return index;
    }

    public static int fillpstmtFromIndex(PreparedStatement pstmt, TableMap tableMap, Object o, int index)
            throws SQLException, NonSqlTypeErr, ReflectiveOperationException {
        
        return processFields(pstmt, tableMap, o, index, (stmt, idx, fi, obj) -> {
            Object value = fi.getFieldValue(obj);
            if (value == null) {
                stmt.setNull(idx, java.sql.Types.VARCHAR);
            } else {
                stmt.setObject(idx, value);
            }
            return idx + 1;
        });
    }

    public static int fillpstmtForUpdates(PreparedStatement pstmt, TableMap tableMap, Object o)
            throws SQLException, NonSqlTypeErr, ReflectiveOperationException {
        return fillpstmtFromIndex(pstmt, tableMap, o, 1);
    }

    public static int fillWhere(PreparedStatement pstmt, TableMap tableMap, Object where)
        throws ReflectiveOperationException, SQLException, NonSqlTypeErr {
        return processFields(pstmt, tableMap, where, 1, (stmt, idx, fi, obj) -> {
            Object value = fi.getFieldValue(obj);
            if (value != null) {
                stmt.setObject(idx, value);
                return idx + 1;
            }
            return idx;
        });
    }
}
