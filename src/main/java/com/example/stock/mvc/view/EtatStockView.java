package com.example.stock.mvc.view;

import java.awt.BorderLayout;
import java.sql.SQLException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.example.stock.mvc.controller.EtatStockController;
import com.example.stock.mvc.controller.EtatStockControllerTableRow;
import com.example.stock.mvc.view.module.form.EtatStockFormPanel;
import com.example.stock.mvc.view.module.table.EtatStockTablePanel;

public class EtatStockView extends JPanel {

    private final EtatStockFormPanel form;
    private final EtatStockTablePanel table;

    public EtatStockView() throws ClassNotFoundException, SQLException {
        setLayout(new BorderLayout(10, 10));

        this.form = new EtatStockFormPanel();
        this.table = new EtatStockTablePanel();

        EtatStockController formController = new EtatStockController(this);
        this.form.setValidateFormListener(formController);

        EtatStockControllerTableRow tableController = new EtatStockControllerTableRow(this);

        add(this.form, BorderLayout.NORTH);
        add(new JScrollPane(this.table), BorderLayout.CENTER);
        table.addMouseListener(tableController);
        formController.loadDataNow();
    }

    public EtatStockFormPanel getForm() {
        return form;
    }

    public EtatStockTablePanel getTable() {
        return table;
    }
}
