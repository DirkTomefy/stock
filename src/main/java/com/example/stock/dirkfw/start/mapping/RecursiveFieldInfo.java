package com.example.stock.dirkfw.start.mapping;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.example.stock.dirkfw.DirkFwConfig;
import com.example.stock.dirkfw.db.util.DBTypes;
import com.example.stock.dirkfw.err.NoGetterAvailable;
import com.example.stock.dirkfw.err.NoSetterAvailable;
import com.example.stock.dirkfw.err.db.NonSqlTypeErr;

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
        // Override to use the recursive column name instead of default
        return recursiveColumnName;
    }
    
    @Override
    public Object getDatabaseValue(Object o) throws ReflectiveOperationException, NoGetterAvailable, NoSetterAvailable    {
        // For recursive relations, extract the ID from the recursive object
        Object value = this.getFieldValue(o);
        
        if (value == null) {
            return null;
        }

        // Get the TableMap for the recursive type to extract its ID
        Class<?> recursiveType = this.getReflectField().getType();
        TableMap recursiveMap = DirkFwConfig.classInfos == null
                ? null
                : DirkFwConfig.classInfos.get(recursiveType.getName());

        if (recursiveMap == null) {
            recursiveMap = new TableMap(recursiveType);
        }

        // Return the ID value of the recursive object
        return recursiveMap.getIdFieldValue(value);
    }
    
    @Override
    public Integer getSqlType() throws NonSqlTypeErr, ReflectiveOperationException {
        // For recursive relations, get the ID field type of the recursive class
        Class<?> recursiveType = this.getReflectField().getType();
        
        try {
            TableMap recursiveMap = DirkFwConfig.classInfos == null
                    ? null
                    : DirkFwConfig.classInfos.get(recursiveType.getName());

            if (recursiveMap == null) {
                recursiveMap = new TableMap(recursiveType);
            }

            // Return the SQL type of the recursive class's ID field
            return DBTypes.getSqlType(recursiveMap.getFieldID().getReflectField().getType());
        } catch (Exception e) {
            throw new ReflectiveOperationException(e);
        }
    }

}

