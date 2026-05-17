package com.example.stock.model;
import java.time.LocalDateTime;

import com.example.stock.dirkfw.annotation.db.IdField;
import com.example.stock.dirkfw.annotation.db.IgnoreDbOpperation;
import com.example.stock.dirkfw.annotation.db.ManytoOne;
import com.example.stock.dirkfw.annotation.db.TableColumnName;
import com.example.stock.dirkfw.annotation.db.TableName;
import com.example.stock.dirkfw.annotation.display.IgnoreDisplayOpperation;

@TableName("mouvement")
public class Mouvement {
    @IdField
    Integer id;

    @TableColumnName("id_article")
    @ManytoOne(joinColumn = "id", toDisplayOnCombobox = "toDisplayOnCombobox")
    Article article;

    @TableColumnName("type_mouvement")
    String typeMouvement; // "ENTREE" ou "SORTIE"

    LocalDateTime dateMouvement;

    @TableColumnName("quantite")
    Integer quantite;   

    Double pu;

    Double valeur;

    @TableColumnName("qte_stock")
    Double qteStock;

    @TableColumnName("money_value_stock")
    Double moneyValueStock;

    Double cump;

    @IgnoreDisplayOpperation
    @IgnoreDbOpperation
    Mouvement source;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public String getTypeMouvement() {
        return typeMouvement;
    }

    public void setTypeMouvement(String typeMouvement) {
        this.typeMouvement = typeMouvement;
    }

    public LocalDateTime getDateMouvement() {
        return dateMouvement;
    }

    public void setDateMouvement(LocalDateTime dateMouvement) {
        this.dateMouvement = dateMouvement;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public Double getPu() {
        return pu;
    }

    public void setPu(Double pu) {
        this.pu = pu;
    }

    public Double getValeur() {
        return valeur;
    }

    public void setValeur(Double valeur) {
        this.valeur = valeur;
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

    public Mouvement getSource() {
        return source;
    }

    public void setSource(Mouvement source) {
        this.source = source;
    }
}
