package com.example.stock.mvc.model;

import com.example.stock.dirkfw.DirkFwModelTrait;
import com.example.stock.dirkfw.annotation.db.IdField;
import com.example.stock.dirkfw.annotation.db.TableColumnName;
import com.example.stock.dirkfw.annotation.db.TableName;

@TableName("type_mouvement")
public class TypeMouvement implements DirkFwModelTrait {

    @IdField
    @TableColumnName("sigle")
    private String sigle;

    public TypeMouvement() {}

    public TypeMouvement(String sigle) {
        this.sigle = sigle;
    }

    public String getSigle() {
        return sigle;
    }

    public void setSigle(String sigle) {
        this.sigle = sigle;
    }

    @Override
    public String toDisplayOnList() {
        return sigle;
    }

    @Override
    public String toDisplayOnCombobox() {
        return sigle;
    }

    @Override
    public String toString() {
        return sigle;
    }
}
