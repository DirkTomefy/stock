package com.example.stock.dirkfw.db.util;

import java.sql.Connection;

@FunctionalInterface
    public interface ConnectionOperation {
        void execute(Connection conn) throws Exception;
    }

