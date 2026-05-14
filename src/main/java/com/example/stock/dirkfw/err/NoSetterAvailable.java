package com.example.stock.dirkfw.err;

public class NoSetterAvailable extends Exception {
     String fieldName;
    String className;

    public NoSetterAvailable(String fieldName, String className, Exception cause) {
        super("No setter available for field " + fieldName + " in class " + className + "\n" + cause.getMessage(),
                cause);
        this.addSuppressed(cause);
        this.fieldName = fieldName;
        this.className = className;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }   
}
