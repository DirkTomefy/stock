package com.example.stock.mvc.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

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
        // Validation : article requis
        if (mouvement.getArticle() == null) {
            JOptionPane.showMessageDialog(this.view, "Veuillez sélectionner un article", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validation : type de mouvement requis
        if (mouvement.getTypeMouvement() == null || mouvement.getTypeMouvement().isEmpty()) {
            JOptionPane.showMessageDialog(this.view, "Veuillez sélectionner un type de mouvement", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validation : quantité requise et positive
        if (mouvement.getQuantite() == null || mouvement.getQuantite() <= 0) {
            JOptionPane.showMessageDialog(this.view, "Veuillez entrer une quantité positive", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validation : prix unitaire requis et positif
        if (mouvement.getPu() == null || mouvement.getPu() <= 0) {
            JOptionPane.showMessageDialog(this.view, "Veuillez entrer un prix unitaire positif", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            MouvementService.insertMouvement(mouvement);
            JOptionPane.showMessageDialog(this.view, "Mouvement inséré avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e1) { 
            JOptionPane.showMessageDialog(this.view, "Erreur : " + e1.getMessage() , "Erreur", JOptionPane.ERROR_MESSAGE);
            e1.printStackTrace();
            throw new RuntimeException(e1);
        }
        this.view.resetObject();
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
