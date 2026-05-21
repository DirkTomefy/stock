package com.example.stock.mvc.view.module.form;

import java.sql.SQLException;

import com.example.stock.dirkfw.display.classes.GenericFormPanel;
import com.example.stock.mvc.model.EtatStockFormInput;

public class EtatStockFormPanel extends GenericFormPanel {

    public EtatStockFormPanel() throws ClassNotFoundException, SQLException {
        super(new EtatStockFormInput(), null);
    }
}