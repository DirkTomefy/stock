package com.example.stock.mvc.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;


import com.example.stock.dirkfw.display.util.DisplayUtil;
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

        try {
            Vector<Object> data = MouvementDetailService.findDetails(input);
            this.view.getTable().setData(data);
        } catch (Exception ex) {
            DisplayUtil.displayPopUpErr(this.view, ex);
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
