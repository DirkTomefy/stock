package com.example.stock.model;

import java.util.ArrayList;
import java.util.List;

import com.example.stock.dirkfw.DirkFwConfig;
import com.example.stock.dirkfw.annotation.db.IdField;
import com.example.stock.dirkfw.annotation.db.OneToMany;
import com.example.stock.dirkfw.annotation.db.TableColumnName;
import com.example.stock.dirkfw.annotation.db.TableName;

@TableName("categories")
public class Category extends DirkFwConfig {

    @IdField
    @TableColumnName("id")
    private Integer id;

    @TableColumnName("name")
    private String name;

    @TableColumnName("description")
    private String description;

    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();

    public Category() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String toDisplayOnCombobox() {
        if (name == null || name.isBlank()) {
            return String.valueOf(id);
        }
        return name;
    }

    @Override
    public String toString() {
        if (name == null || name.isBlank()) {
            return "Category{id=" + id + "}";
        }
        return "Category{id=" + id + ", name='" + name + "'}";
    }
}