package com.example.stock.model;

import java.time.LocalDateTime;
import com.example.stock.dirkfw.annotation.db.IdField;

public class ArticleModel {
    @IdField
    private Integer id;

    private String libelle;

    private LocalDateTime datecreation;

    public LocalDateTime getDatecreation() {
        return datecreation;
    }

    public void setDatecreation(LocalDateTime datecreation) {
        this.datecreation = datecreation;
    }

    // FIFO, LIFO ou CUMP
    private String sigleGestionStock;

    

    public ArticleModel(Integer id, String libelle, String sigleGestionStock) {
        this.id = id;
        this.libelle = libelle;
        this.sigleGestionStock = sigleGestionStock;
    }

    public ArticleModel() {
        
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

    public String getSigleGestionStock() {
        return sigleGestionStock;
    }

    public void setSigleGestionStock(String sigleGestionStock) {
        this.sigleGestionStock = sigleGestionStock;
    }

    @Override
    public String toString() {
        return "ArticleModel{" +
                "id=" + id +
                ", libelle='" + libelle + '\'' +
                ", sigleGestionStock='" + sigleGestionStock + '\'' +
                '}';
    }
}