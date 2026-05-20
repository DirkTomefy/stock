package com.example.stock.mvc.model;

import com.example.stock.dirkfw.DirkFwModelTrait;
import com.example.stock.dirkfw.annotation.db.IdField;
import com.example.stock.dirkfw.annotation.db.ManytoOne;
import com.example.stock.dirkfw.annotation.db.TableName;




@TableName("article")
public class Article implements DirkFwModelTrait{
    public Article() {
    }

    @Override
    public String toString() {
        return "Article [id=" + id + ", libelle=" + libelle + "]";
    }

    @IdField
    Integer id;

    String libelle;

    @ManytoOne(joinColumn = "sigle_gestion_stock", toDisplayOnCombobox = "toDisplayOnCombobox")
    MethodeGestionStock MethodGestionStock;

    public Article(Integer id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }

    public Article(EtatStock etatStock){
        this.id=etatStock.getIdArticle();
        this.libelle=etatStock.getLibelleArticle();
    }
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

    @Override
    public String toDisplayOnCombobox() {
        return libelle;
    }
    
    @Override
    public String toDisplayOnList(){
        return libelle;
    }
}
