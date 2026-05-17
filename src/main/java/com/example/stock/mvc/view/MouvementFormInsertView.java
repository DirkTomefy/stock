package com.example.stock.mvc.view;

import com.example.stock.dirkfw.display.classes.GenericFormPanel;
import com.example.stock.mvc.controller.MouvementFormController;
import com.example.stock.mvc.model.Mouvement;

public class MouvementFormInsertView extends GenericFormPanel {

    public MouvementFormInsertView() {
        super(new Mouvement(), null);
        MouvementFormController controller = new MouvementFormController(this);
        setValidateFormListener(controller);
    }

}
