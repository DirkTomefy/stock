package com.example.stock.mvc.view.module.form;

import java.sql.SQLException;

import com.example.stock.dirkfw.display.classes.GenericFormPanel;
import com.example.stock.mvc.model.Article;

public class ArticleFormInsertPanel extends GenericFormPanel {

    public ArticleFormInsertPanel() throws ClassNotFoundException, SQLException {
        super(new Article(), null);
    }
}
