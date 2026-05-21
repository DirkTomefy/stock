package com.example.stock.mvc.view;

import java.awt.BorderLayout;
import java.sql.SQLException;

import javax.swing.JPanel;

import com.example.stock.mvc.controller.MouvementFormController;
import com.example.stock.mvc.view.module.form.MouvementInsertPanel;

public class MouvementFormInsertView extends JPanel {

    private final MouvementInsertPanel formPanel;

    public MouvementFormInsertView() throws ClassNotFoundException, SQLException {
        setLayout(new BorderLayout(10, 10));

        this.formPanel = new MouvementInsertPanel();
        MouvementFormController controller = new MouvementFormController(this);
        this.formPanel.setValidateFormListener(controller);

        add(this.formPanel, BorderLayout.CENTER);
    }

    public MouvementInsertPanel getFormPanel() {
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
