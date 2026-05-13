package com.example.stock.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MouvementModel {

    private Integer id;
    private Integer idArticle;

    // ENTREE ou SORTIE
    private String type;

    private LocalDateTime dateMouvement;

    private BigDecimal qte;
    private BigDecimal pu;

    // qte * pu
    private BigDecimal valeur;

    private BigDecimal qteStock;

    private BigDecimal moneyValueStock;

    private BigDecimal cump;

    public MouvementModel() {
    }

    public MouvementModel(Integer id, Integer idArticle, String type,
                          LocalDateTime dateMouvement,
                          BigDecimal qte, BigDecimal pu,
                          BigDecimal valeur,
                          BigDecimal qteStock,
                          BigDecimal moneyValueStock,
                          BigDecimal cump) {

        this.id = id;
        this.idArticle = idArticle;
        this.type = type;
        this.dateMouvement = dateMouvement;
        this.qte = qte;
        this.pu = pu;
        this.valeur = valeur;
        this.qteStock = qteStock;
        this.moneyValueStock = moneyValueStock;
        this.cump = cump;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdArticle() {
        return idArticle;
    }

    public void setIdArticle(Integer idArticle) {
        this.idArticle = idArticle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getDateMouvement() {
        return dateMouvement;
    }

    public void setDateMouvement(LocalDateTime dateMouvement) {
        this.dateMouvement = dateMouvement;
    }

    public BigDecimal getQte() {
        return qte;
    }

    public void setQte(BigDecimal qte) {
        this.qte = qte;
    }

    public BigDecimal getPu() {
        return pu;
    }

    public void setPu(BigDecimal pu) {
        this.pu = pu;
    }

    public BigDecimal getValeur() {
        return valeur;
    }

    public void setValeur(BigDecimal valeur) {
        this.valeur = valeur;
    }

    public BigDecimal getQteStock() {
        return qteStock;
    }

    public void setQteStock(BigDecimal qteStock) {
        this.qteStock = qteStock;
    }

    public BigDecimal getMoneyValueStock() {
        return moneyValueStock;
    }

    public void setMoneyValueStock(BigDecimal moneyValueStock) {
        this.moneyValueStock = moneyValueStock;
    }

    public BigDecimal getCump() {
        return cump;
    }

    public void setCump(BigDecimal cump) {
        this.cump = cump;
    }

    @Override
    public String toString() {
        return "MouvementModel{" +
                "id=" + id +
                ", idArticle=" + idArticle +
                ", type='" + type + '\'' +
                ", dateMouvement=" + dateMouvement +
                ", qte=" + qte +
                ", pu=" + pu +
                ", valeur=" + valeur +
                ", qteStock=" + qteStock +
                ", moneyValueStock=" + moneyValueStock +
                ", cump=" + cump +
                '}';
    }
}