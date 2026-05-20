package com.example.stock.mvc.model;

import java.time.LocalDateTime;

import com.example.stock.dirkfw.DirkFwModelTrait;
import com.example.stock.dirkfw.annotation.db.TableColumnName;

public class EtatStockFormInput implements DirkFwModelTrait {

    @TableColumnName("date_mouvement")
    LocalDateTime date;

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
