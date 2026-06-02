package com.example.stock.mvc.view.module.row;

import java.time.LocalDateTime;

import com.example.stock.dirkfw.display.classes.InsertionMultipleForm;
import com.example.stock.dirkfw.display.classes.InsertionMultipleRowPanel;
import com.example.stock.dirkfw.display.interfaces.DFWInput;
import com.example.stock.mvc.model.Mouvement;
import com.example.stock.mvc.view.MouvementInsertionMultiple;

public class MouvementInsertionMultipleRowPanel extends InsertionMultipleRowPanel<Mouvement> {

    private final Mouvement mouvement;

    public MouvementInsertionMultipleRowPanel(InsertionMultipleForm<Mouvement> parent) {
        super(parent);
        this.mouvement = new Mouvement();
        this.mouvement.setDateMouvement(LocalDateTime.now());

        addField("Article", "article", createInput("article"));
        addField("Type", "typeMouvement", createInput("typeMouvement"));
        addField("Date", "dateMouvement", createInput("dateMouvement"));
        addField("Quantité", "quantite", createInput("quantite"));
        addField("PU", "pu", createInput("pu"));
        finishLayout();
    }

    private DFWInput createInput(String fieldName) {
        return ((MouvementInsertionMultiple) parent).createInput(parent.getFieldInfo(fieldName), mouvement);
    }

    @Override
    public Mouvement toEntity() throws Exception {
        getInput("article").getFieldInfo().setFieldValue(mouvement, getInput("article").getValue());
        getInput("typeMouvement").getFieldInfo().setFieldValue(mouvement, getInput("typeMouvement").getValue());
        getInput("dateMouvement").getFieldInfo().setFieldValue(mouvement, getInput("dateMouvement").getValue());
        getInput("quantite").getFieldInfo().setFieldValue(mouvement, getInput("quantite").getValue());
        getInput("pu").getFieldInfo().setFieldValue(mouvement, getInput("pu").getValue());
        return mouvement;
    }

    @Override
    public boolean isEmpty() {
        return getInput("article").getValue() == null
                && getInput("typeMouvement").getValue() == null
                && getInput("quantite").getValue() == null
                && getInput("pu").getValue() == null;
    }
}
