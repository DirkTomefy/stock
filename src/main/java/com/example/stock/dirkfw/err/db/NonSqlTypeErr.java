package com.example.stock.dirkfw.err.db;

public class NonSqlTypeErr extends Exception {
    Class<?> clazz;

    public NonSqlTypeErr(Class<?> clazz) {
        super("Class " + clazz.getName() + " is not a supported SQL type.");
        this.clazz = clazz;
    }


    
}
