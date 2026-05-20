package com.example.stock.mvc.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;

import com.example.stock.dirkfw.display.classes.GenericTablePanel;
import com.example.stock.dirkfw.display.util.DisplayUtil;
import com.example.stock.mvc.model.EtatStock;
import com.example.stock.mvc.view.DetailFormView;
import com.example.stock.mvc.view.EtatStockView;

public class EtatStockControllerTableRow implements MouseListener{
    private final EtatStockView view;

    public EtatStockControllerTableRow(EtatStockView view) {
        this.view = view;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        GenericTablePanel tablePanel=this.view.getTable();
        int row = tablePanel.rowAtPoint(e.getPoint());
        if(row>=0){
            if(e.getClickCount() == 2){
                EtatStock data = (EtatStock) tablePanel.getData().get(row);
                try {
                    DisplayUtil.displayPopUp(new DetailFormView(data), "Tital");
                } catch (ClassNotFoundException | SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
     
    }

    @Override
    public void mouseEntered(MouseEvent e) {
      
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
    
}
