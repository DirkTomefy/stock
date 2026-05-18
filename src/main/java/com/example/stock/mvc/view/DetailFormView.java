package com.example.stock.mvc.view;

import java.awt.BorderLayout;
import java.sql.SQLException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.example.stock.dirkfw.display.classes.GenericFormPanel;
import com.example.stock.dirkfw.display.classes.GenericTablePanel;
import com.example.stock.mvc.controller.DetailFormController;
import com.example.stock.mvc.model.DetailFormInput;

public class DetailFormView extends JPanel {

    private final GenericFormPanel form;
    private final GenericTablePanel table;

    public DetailFormView() throws ClassNotFoundException, SQLException {
        setLayout(new BorderLayout(10, 10));

        this.form = new GenericFormPanel(new DetailFormInput(), null);
        this.table = new GenericTablePanel();

        DetailFormController controller = new DetailFormController(this);
        this.form.setValidateFormListener(controller);

        add(this.form, BorderLayout.NORTH);
        add(new JScrollPane(this.table), BorderLayout.CENTER);
    }

    public GenericFormPanel getForm() {
        return form;
    }

    public GenericTablePanel getTable() {
        return table;
    }
}
