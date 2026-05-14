package com.example.stock.dirkfw.dao.util;

import java.sql.Connection;

@FunctionalInterface
    public interface ConnectionOperation {
        void execute(Connection conn) throws Exception;
    }

