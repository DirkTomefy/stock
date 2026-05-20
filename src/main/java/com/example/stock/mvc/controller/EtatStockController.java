package com.example.stock.mvc.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDateTime;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.example.stock.dirkfw.display.util.DisplayUtil;
import com.example.stock.mvc.model.EtatStockFormInput;
import com.example.stock.mvc.service.EtatStockService;
import com.example.stock.mvc.view.EtatStockView;

public class EtatStockController implements MouseListener {

    private final EtatStockView view;

    public EtatStockController(EtatStockView view) {
        this.view = view;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.view.getForm().fillObject();
        EtatStockFormInput input = (EtatStockFormInput) this.view.getForm().getObject();

        if (input.getDate() == null) {
            JOptionPane.showMessageDialog(this.view, "Veuillez sélectionner une date", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        loadData(input.getDate());
    }

    public void loadDataNow() {
        loadData(LocalDateTime.now());
    }

    private void loadData(LocalDateTime date) {
        try {
            Vector<Object> data = EtatStockService.getEtatStockGeneral(date);
            this.view.getTable().setData(data);
        } catch (Exception ex) {
            DisplayUtil.displayPopUpErr(this.view, ex);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
