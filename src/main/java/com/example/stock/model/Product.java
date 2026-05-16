package com.example.stock.model;

import com.example.stock.dirkfw.DirkFwConfig;
import com.example.stock.dirkfw.annotation.db.IdField;
import com.example.stock.dirkfw.annotation.db.ManytoOne;
import com.example.stock.dirkfw.annotation.db.TableColumnName;
import com.example.stock.dirkfw.annotation.db.TableName;

@TableName("products")
public class Product extends DirkFwConfig {

    @IdField
    @TableColumnName("id")
    private Integer id;

    @TableColumnName("name")
    private String name;

    @TableColumnName("price")
    private Double price;

    @TableColumnName("stock_quantity")
    private Integer stockQuantity;

    @ManytoOne(joinColumn = "category_id", toDisplayOnCombobox = "toDisplayOnCombobox")
    private Category category;

    public Product() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Product{id=" + id + ", name='" + name + "', price=" + price + ", stockQuantity=" + stockQuantity + ", category=" + category + "}";
    }
}