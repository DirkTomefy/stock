package com.example.stock.dirkfw.db.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.example.stock.dirkfw.err.db.NonSqlTypeErr;
import com.example.stock.dirkfw.start.mapping.FieldInfo;

@FunctionalInterface
public interface FieldProcessor {
    int process(PreparedStatement pstmt, int index, FieldInfo fi, Object obj)
            throws SQLException, ReflectiveOperationException, NonSqlTypeErr;
}
