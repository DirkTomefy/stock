package com.example.stock.dirkfw.start.mapping;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.example.stock.dirkfw.err.NoGetterAvailable;
import com.example.stock.dirkfw.err.NoSetterAvailable;
import com.example.stock.dirkfw.err.db.NonSqlTypeErr;
import com.example.stock.dirkfw.start.interfaces.DisplayableOnCombobox;



public class RecursiveFieldInfo extends FieldInfo implements DisplayableOnCombobox{

    private String recursiveColumnName;
    private Method onComboboxMethod;

    public RecursiveFieldInfo(Field value, Method getter, Method setter, String recursiveColumnName) throws NoSuchMethodException {
        super(value, getter, setter);
        this.recursiveColumnName = recursiveColumnName;
        initMethodOnCombobox();
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

    @Override
    public void initMethodOnCombobox() throws NoSuchMethodException {
        this.onComboboxMethod = this.reflectField.getType().getMethod("toDisplayOnCombobox");

    }

    @Override
    public Method getMethodOnCombobox() {
        return this.onComboboxMethod;
    }

}

