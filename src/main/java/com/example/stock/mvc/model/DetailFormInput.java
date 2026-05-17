package com.example.stock.mvc.model;

import java.time.LocalDateTime;

import com.example.stock.dirkfw.annotation.db.ManytoOne;
import com.example.stock.dirkfw.annotation.db.TableColumnName;

public class DetailFormInput {
    
    
    @ManytoOne(joinColumn = "id_article", toDisplayOnCombobox = "toDisplayOnCombobox")
    Article article;

    @TableColumnName("date_mouvement")
    LocalDateTime date;

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
