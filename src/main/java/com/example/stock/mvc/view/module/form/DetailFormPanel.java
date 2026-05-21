package com.example.stock.mvc.view.module.form;

import java.sql.SQLException;

import com.example.stock.dirkfw.display.classes.GenericFormPanel;
import com.example.stock.mvc.model.DetailFormInput;

public class DetailFormPanel extends GenericFormPanel {

    public DetailFormPanel() throws ClassNotFoundException, SQLException {
        super(new DetailFormInput(), null);
    }
}