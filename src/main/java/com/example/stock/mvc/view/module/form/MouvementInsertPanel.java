package com.example.stock.mvc.view.module.form;

import java.sql.SQLException;

import com.example.stock.dirkfw.display.classes.GenericFormPanel;
import com.example.stock.mvc.model.Mouvement;

public class MouvementInsertPanel extends GenericFormPanel {

    public MouvementInsertPanel() throws ClassNotFoundException, SQLException {
        super(new Mouvement(), null);
    }
}
