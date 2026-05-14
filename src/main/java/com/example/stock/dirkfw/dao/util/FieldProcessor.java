package com.example.stock.dirkfw.dao.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.example.stock.dirkfw.dao.start.mapping.FieldInfo;
import com.example.stock.dirkfw.err.NonSqlTypeErr;

@FunctionalInterface
public interface FieldProcessor {
    int process(PreparedStatement pstmt, int index, FieldInfo fi, Object obj)
            throws SQLException, ReflectiveOperationException, NonSqlTypeErr;
}
