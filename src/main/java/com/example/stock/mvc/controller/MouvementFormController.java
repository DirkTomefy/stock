package com.example.stock.mvc.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

import com.example.stock.dirkfw.display.util.DisplayUtil;
import com.example.stock.mvc.model.Mouvement;
import com.example.stock.mvc.service.MouvementService;
import com.example.stock.mvc.view.MouvementFormInsertView;

public class MouvementFormController implements MouseListener {
    MouvementFormInsertView view;

    public MouvementFormController(MouvementFormInsertView view) {
        this.view = view;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.view.fillObject();
        Mouvement mouvement = (Mouvement) this.view.getObject();
        try {
            MouvementService.insertMouvement(mouvement);
            JOptionPane.showMessageDialog(this.view, "Mouvement inséré avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
            this.view.resetObject();
        } catch (Exception e1) { 
            DisplayUtil.displayPopUpErr(this.view, e1);
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
