package com.example.stock.model;

import com.example.stock.dirkfw.annotation.db.IdField;
import com.example.stock.dirkfw.annotation.db.ManytoOne;
import com.example.stock.dirkfw.annotation.db.TableColumnName;
import com.example.stock.dirkfw.annotation.db.TableName;




@TableName("article")
public class Article {
    @IdField
    Integer id;

    String libelle;

    @TableColumnName(value="sigle_gestion_stock")
    @ManytoOne(joinColumn = "sigle", toDisplayOnCombobox = "toDisplayOnCombobox")
    MethodeGestionStock MethodGestionStock;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public MethodeGestionStock getMethodGestionStock() {
        return MethodGestionStock;
    }

    public void setMethodGestionStock(MethodeGestionStock methodGestionStock) {
        MethodGestionStock = methodGestionStock;
    }

    public String toDisplayOnCombobox() {
        return libelle;
    }
    
}
