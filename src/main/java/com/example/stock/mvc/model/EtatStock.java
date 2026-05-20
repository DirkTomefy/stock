package com.example.stock.mvc.model;

import java.time.LocalDateTime;

import com.example.stock.dirkfw.DirkFwModelTrait;
import com.example.stock.dirkfw.annotation.db.IdField;
import com.example.stock.dirkfw.annotation.db.TableColumnName;
import com.example.stock.dirkfw.annotation.db.TableName;
import com.example.stock.dirkfw.annotation.display.PrimaryOnList;

@TableName("etat_stock_general")
public class EtatStock implements DirkFwModelTrait {

    @IdField
    @TableColumnName("id_article")
    Integer idArticle;

    @TableColumnName("libelle_article")
    String libelleArticle;

    @TableColumnName("sigle_gestion_stock")
    String sigleGestionStock;

    @TableColumnName("qte_stock")
    @PrimaryOnList
    Double qteStock;

    @TableColumnName("money_value_stock")
    Double moneyValueStock;

    Double cump;

    @TableColumnName("date_dernier_mouvement")
    LocalDateTime dateDernierMouvement;

    public Integer getIdArticle() {
        return idArticle;
    }

    public void setIdArticle(Integer idArticle) {
        this.idArticle = idArticle;
    }

    public String getLibelleArticle() {
        return libelleArticle;
    }

    public void setLibelleArticle(String libelleArticle) {
        this.libelleArticle = libelleArticle;
    }

    public String getSigleGestionStock() {
        return sigleGestionStock;
    }

    public void setSigleGestionStock(String sigleGestionStock) {
        this.sigleGestionStock = sigleGestionStock;
    }

    public Double getQteStock() {
        return qteStock;
    }

    public void setQteStock(Double qteStock) {
        this.qteStock = qteStock;
    }

    public Double getMoneyValueStock() {
        return moneyValueStock;
    }

    public void setMoneyValueStock(Double moneyValueStock) {
        this.moneyValueStock = moneyValueStock;
    }

    public Double getCump() {
        return cump;
    }

    public void setCump(Double cump) {
        this.cump = cump;
    }

    public LocalDateTime getDateDernierMouvement() {
        return dateDernierMouvement;
    }

    public void setDateDernierMouvement(LocalDateTime dateDernierMouvement) {
        this.dateDernierMouvement = dateDernierMouvement;
    }

    @Override
    public String toDisplayOnList() {
        return libelleArticle;
    }

    @Override
    public String toDisplayOnCombobox() {
        return libelleArticle;
    }
}
