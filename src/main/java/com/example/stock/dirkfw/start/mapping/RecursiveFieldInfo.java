package com.example.stock.dirkfw.start.mapping;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.example.stock.dirkfw.DirkFwConfig;
import com.example.stock.dirkfw.db.util.DBTypes;
import com.example.stock.dirkfw.err.NoGetterAvailable;
import com.example.stock.dirkfw.err.NoSetterAvailable;
import com.example.stock.dirkfw.err.db.NonSqlTypeErr;

import lombok.val;

public class RecursiveFieldInfo extends FieldInfo {

    private String recursiveColumnName;

    public RecursiveFieldInfo(Field value, Method getter, Method setter, String recursiveColumnName) {
        super(value, getter, setter);
        this.recursiveColumnName = recursiveColumnName;
    }

    public String getRecursiveColumnName() {
        return recursiveColumnName;
    }
    
    @Override
    public String getTableColumnName() {
        return recursiveColumnName;
    }
    
    @Override
    public Object getDatabaseValue(TableMap tableMap,Object o) throws ReflectiveOperationException, NoGetterAvailable, NoSetterAvailable    {
        Object value = this.getFieldValue(o); 
        System.out.println(""+this.getTableColumnName());       
        if (value == null) {
            return null;
        }
        return tableMap.getIdFieldValue(value);
       
    }
    
    @Override
    public Integer getSqlType(TableMap tableMap) throws NonSqlTypeErr, ReflectiveOperationException {
        return tableMap.getFieldID().getSqlType(tableMap);   
    }

}

