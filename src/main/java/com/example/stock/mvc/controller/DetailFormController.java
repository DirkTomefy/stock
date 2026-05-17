package com.example.stock.mvc.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.example.stock.mvc.model.DetailFormInput;
import com.example.stock.mvc.service.MouvementDetailService;
import com.example.stock.mvc.view.DetailFormView;

public class DetailFormController implements MouseListener {

    private final DetailFormView view;

    public DetailFormController(DetailFormView view) {
        this.view = view;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.view.getForm().fillObject();
        DetailFormInput input = (DetailFormInput) this.view.getForm().getObject();

        if (input.getArticle() == null) {
            JOptionPane.showMessageDialog(this.view, "Veuillez sélectionner un article", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (input.getDate() == null) {
            JOptionPane.showMessageDialog(this.view, "Veuillez sélectionner une date", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Vector<Object> data = MouvementDetailService.findDetails(input);
            this.view.getTable().setData(data);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this.view, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
