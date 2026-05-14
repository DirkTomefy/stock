package com.example.stock.dirkfw.db.util;

import java.sql.Connection;

@FunctionalInterface
    public interface ConnectionQuery<T> {
        T execute(Connection conn) throws Exception;
    }
