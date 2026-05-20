package com.example.stock.mvc.model;
import java.time.LocalDateTime;

import com.example.stock.dirkfw.annotation.db.IdField;
import com.example.stock.dirkfw.annotation.db.ManytoOne;
import com.example.stock.dirkfw.annotation.db.RecursiveCall;
import com.example.stock.dirkfw.annotation.db.TableColumnName;
import com.example.stock.dirkfw.annotation.db.TableName;
import com.example.stock.dirkfw.annotation.display.IgnoreDisplayOpperation;
import com.example.stock.dirkfw.annotation.display.IgnoreFormulaire;
import com.example.stock.dirkfw.annotation.display.DisplayOnList;
import com.example.stock.dirkfw.annotation.display.PrimaryOnList;
import com.example.stock.dirkfw.annotation.display.SkipTableList;

@TableName("mouvement")
public class Mouvement {
    @IdField
    Integer id;

    @ManytoOne(joinColumn = "id_article", toDisplayOnCombobox = "toDisplayOnCombobox")
    Article article;

    @TableColumnName("type")
    String typeMouvement; // "ENTREE" ou "SORTIE"

    @TableColumnName("date_mouvement")
    LocalDateTime dateMouvement;

    @TableColumnName("qte")
    Integer quantite;   

    Double pu;

    @IgnoreFormulaire
    Double valeur;

    @TableColumnName("qte_stock")
    @IgnoreFormulaire
    Double qteStock;

    @TableColumnName("money_value_stock")
    @IgnoreFormulaire
    Double moneyValueStock;

    @IgnoreFormulaire
    Double cump;

    @IgnoreDisplayOpperation
    @RecursiveCall("source_id")
    @DisplayOnList("toDisplayOnList")
    Mouvement source;

    @TableColumnName("qte_prise")
    @IgnoreFormulaire
    @SkipTableList
    Integer quantitePrise;

    @TableColumnName("total_prise_for_entree")
    @IgnoreFormulaire
    @PrimaryOnList
    Integer totalPriseForEntree;
    public Integer getQuantitePrise() {
        return quantitePrise;
    }


    public void setQuantitePrise(Integer quantitePrise) {
        this.quantitePrise = quantitePrise;
    }

    public Integer getTotalPriseForEntree() {
        return totalPriseForEntree;
    }

    public void setTotalPriseForEntree(Integer totalPriseForEntree) {
        this.totalPriseForEntree = totalPriseForEntree;
    }


    public static Mouvement defaultMouvement() {
    Mouvement mouvement = new Mouvement();

    mouvement.setId(0);
    mouvement.setQuantite(0);
    mouvement.setQuantitePrise(0);
    mouvement.setTotalPriseForEntree(0);

    mouvement.setPu(0.0);
    mouvement.setValeur(0.0);
    mouvement.setQteStock(0.0);
    mouvement.setMoneyValueStock(0.0);
    mouvement.setCump(0.0);

    return mouvement;
}

  
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


    @Override
    public String toString() {
        return "Mouvement [id=" + id + ", article=" + article + ", typeMouvement=" + typeMouvement + ", dateMouvement="
                + dateMouvement + ", quantite=" + quantite + ", pu=" + pu + ", valeur=" + valeur + ", qteStock="
                + qteStock + ", moneyValueStock=" + moneyValueStock + ", cump=" + cump + ", source=" + source
                + ", quantitePrise=" + quantitePrise + ", totalPriseForEntree=" + totalPriseForEntree + "]";
    }

    public String toDisplayOnList() {
        return "Mouvement#"+this.id;
    }
    public String toDisplayOnCombobox(){
        return "Mouvement#"+this.id;
    }
}
