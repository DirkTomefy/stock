package com.example.stock.mvc.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

import com.example.stock.mvc.service.ArticleService;
import com.example.stock.mvc.view.ArticleFormInsertView;

public class ArticleFormController implements MouseListener{
    ArticleFormInsertView view;

    public ArticleFormController(ArticleFormInsertView view) {
        this.view = view;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.view.fillObject();
        try {
        ArticleService.insertArticle(this.view.getObject());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this.view, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
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
