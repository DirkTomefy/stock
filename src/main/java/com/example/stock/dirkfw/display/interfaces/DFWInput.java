package com.example.stock.dirkfw.display.interfaces;

import com.example.stock.dirkfw.start.mapping.FieldInfo;

public interface DFWInput  {
    public Object getValue();
    public void setValue(Object value);
    public FieldInfo getFieldInfo();
}
