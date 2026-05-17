package com.example.stock.dirkfw.db.query;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import com.example.stock.dirkfw.db.util.ComparaisonOperation;
import com.example.stock.dirkfw.db.util.FieldProcessor;
import com.example.stock.dirkfw.err.NoGetterAvailable;
import com.example.stock.dirkfw.err.NoSetterAvailable;
import com.example.stock.dirkfw.err.db.NonSqlTypeErr;
import com.example.stock.dirkfw.start.mapping.*;
public class QueryFiller {

    private static int processFields(PreparedStatement pstmt, TableMap tableMap, Object o, int index, boolean includeId, FieldProcessor processor)
            throws SQLException, NonSqlTypeErr, ReflectiveOperationException,NoGetterAvailable,NoSetterAvailable {
        for (FieldInfo fInfo : tableMap.getAllFieldWithoutID()) {
            index = processor.process(pstmt, index, fInfo, o);
        }
        if (includeId) {
            FieldInfo idField = tableMap.getFieldID();
            index = processor.process(pstmt, index, idField, o);
        }
        return index;
    }

    public static int fillpstmtForInsert(PreparedStatement pstmt, TableMap tableMap, Object o, int index)
            throws SQLException, NonSqlTypeErr, ReflectiveOperationException, NoGetterAvailable, NoSetterAvailable {
        
        return processFields(pstmt, tableMap, o, index, false, (stmt, idx, fi, obj) -> {
            Object value = fi.getDatabaseValue(obj);
            if (value == null) {
                stmt.setNull(idx, fi.getSqlType());
            } else {
                stmt.setObject(idx, value);
            }
            return idx + 1;
        });
    }

    public static int fillpstmtForUpdates(PreparedStatement pstmt, TableMap tableMap, Object o)
            throws SQLException, NonSqlTypeErr, ReflectiveOperationException, NoGetterAvailable, NoSetterAvailable {
        return processFields(pstmt, tableMap, o, 1, false, (stmt, idx, fi, obj) -> {
            Object value = fi.getDatabaseValue(obj);
            if (value == null) {
                stmt.setNull(idx, fi.getSqlType());
            } else {
                stmt.setObject(idx, value);
            }
            return idx + 1;
        });
    }

    public static int fillWhere(PreparedStatement pstmt, TableMap tableMap, Object where)
        throws ReflectiveOperationException, SQLException, NonSqlTypeErr, NoGetterAvailable, NoSetterAvailable {
        return processFields(pstmt, tableMap, where, 1, true, (stmt, idx, fi, obj) -> {
            Object value = fi.getDatabaseValue(obj);
            if (value != null) {
                stmt.setObject(idx, value);
                return idx + 1;
            }
            return idx;
        });
    }

    public static int fillWhereWithOperations(PreparedStatement pstmt, TableMap tableMap, Object where,
            HashMap<String, ComparaisonOperation> operations)
        throws ReflectiveOperationException, SQLException, NonSqlTypeErr, NoGetterAvailable, NoSetterAvailable {
        
        int index = 1;

        for (FieldInfo fInfo : tableMap.getAllFieldWithoutID()) {
            Object value = fInfo.getDatabaseValue(where);
            if (value != null) {
                pstmt.setObject(index, value);
                index++;
            }
        }

        Object idValue = tableMap.getIdFieldValue(where);
        if (idValue != null) {
            pstmt.setObject(index, idValue);
            index++;
        }

        return index;
    }
}
