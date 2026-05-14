package com.example.stock.dirkfw.dao.util;

import java.sql.Connection;

@FunctionalInterface
    public interface ConnectionQuery<T> {
        T execute(Connection conn) throws Exception;
    }
