package com.example.stock.mvc.view;

import java.awt.BorderLayout;
import java.sql.SQLException;

import javax.swing.JPanel;

import com.example.stock.mvc.controller.ArticleFormController;
import com.example.stock.mvc.view.module.form.ArticleFormInsertPanel;

public class ArticleFormInsertView extends JPanel {

    private final ArticleFormInsertPanel formPanel;

    public ArticleFormInsertView() throws ClassNotFoundException, SQLException {
        setLayout(new BorderLayout(10, 10));

        this.formPanel = new ArticleFormInsertPanel();
        ArticleFormController controller = new ArticleFormController(this);
        this.formPanel.setValidateFormListener(controller);

        add(this.formPanel, BorderLayout.CENTER);
    }

    public ArticleFormInsertPanel getFormPanel() {
        return formPanel;
    }

    public void fillObject() {
        this.formPanel.fillObject();
    }

    public Object getObject() {
        return this.formPanel.getObject();
    }

    public void resetObject() {
        this.formPanel.resetObject();
    }
}
