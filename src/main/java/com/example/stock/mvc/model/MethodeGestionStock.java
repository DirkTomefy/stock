package com.example.stock.mvc.model;
import com.example.stock.dirkfw.annotation.db.IdField;
import com.example.stock.dirkfw.annotation.db.TableName;

@TableName("methode_gestion_stock")
public class MethodeGestionStock {
    @IdField
    String sigle;

    public String toDisplayOnCombobox(){
        return sigle;
    }

    public String getSigle() {
        return sigle;
    }

    public void setSigle(String sigle) {
        this.sigle = sigle;
    }
}
