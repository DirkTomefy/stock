package com.example.stock.dirkfw;

public interface DirkFwModelTrait {
    public default String toDisplayOnList(){
        return this.toString();
    }
    public default String toDisplayOnCombobox(){
        return this.toString();
    }
}
