package com.example.stock.model;
import com.example.stock.dirkfw.annotation.db.IdField;
import com.example.stock.dirkfw.annotation.db.TableName;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@TableName("methode_gestion_stock")
public class MethodeGestionStock {
    @IdField
    String sigle;

    public String toDisplayOnCombobox(){
        return sigle;
    }
}
