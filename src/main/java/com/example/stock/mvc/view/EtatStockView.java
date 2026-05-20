package com.example.stock.mvc.view;

import java.awt.BorderLayout;
import java.sql.SQLException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.example.stock.dirkfw.display.classes.GenericFormPanel;
import com.example.stock.dirkfw.display.classes.GenericTablePanel;
import com.example.stock.mvc.controller.EtatStockController;
import com.example.stock.mvc.controller.EtatStockControllerTableRow;
import com.example.stock.mvc.model.EtatStockFormInput;

public class EtatStockView extends JPanel {

    private final GenericFormPanel form;
    private final GenericTablePanel table;

    public EtatStockView() throws ClassNotFoundException, SQLException {
        setLayout(new BorderLayout(10, 10));

        this.form = new GenericFormPanel(new EtatStockFormInput(), null);
        this.table = new GenericTablePanel();

        EtatStockController formController = new EtatStockController(this);
        this.form.setValidateFormListener(formController);

        EtatStockControllerTableRow tableController = new EtatStockControllerTableRow(this);

        add(this.form, BorderLayout.NORTH);
        add(new JScrollPane(this.table), BorderLayout.CENTER);
        table.addMouseListener(tableController);
        formController.loadDataNow();
    }

    public GenericFormPanel getForm() {
        return form;
    }

    public GenericTablePanel getTable() {
        return table;
    }
}
