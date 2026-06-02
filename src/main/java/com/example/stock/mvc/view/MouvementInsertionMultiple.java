package com.example.stock.mvc.view;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import com.example.stock.dirkfw.DirkFwConfig;
import com.example.stock.dirkfw.db.GenericDao;
import com.example.stock.dirkfw.display.classes.DFWComboBox;
import com.example.stock.dirkfw.display.classes.DFWDateField;
import com.example.stock.dirkfw.display.classes.DFWTextField;
import com.example.stock.dirkfw.display.classes.InsertionMultipleForm;
import com.example.stock.dirkfw.display.classes.InsertionMultipleRowPanel;
import com.example.stock.dirkfw.display.interfaces.DFWInput;
import com.example.stock.dirkfw.start.mapping.FieldInfo;
import com.example.stock.dirkfw.start.mapping.ManyToOneFieldInfo;
import com.example.stock.dirkfw.start.mapping.TableMap;
import com.example.stock.mvc.model.Mouvement;
import com.example.stock.mvc.service.MouvementService;
import com.example.stock.mvc.view.module.row.MouvementInsertionMultipleRowPanel;

public class MouvementInsertionMultiple extends InsertionMultipleForm<Mouvement> {

    private final TableMap mouvementTableMap;

    public MouvementInsertionMultiple() {
        super();
        this.mouvementTableMap = resolveTableMap();
        addRow();
    }

    private TableMap resolveTableMap() {
        try {
            TableMap tableMap = DirkFwConfig.classInfos == null ? null : DirkFwConfig.classInfos.get(Mouvement.class.getName());
            if (tableMap != null) {
                return tableMap;
            }
            return new TableMap(Mouvement.class);
        } catch (Exception e) {
            throw new IllegalStateException("Impossible d'initialiser le formulaire des mouvements", e);
        }
    }

    @Override
    public FieldInfo getFieldInfo(String fieldName) {
        for (FieldInfo fieldInfo : mouvementTableMap.getAllFieldWithoutID()) {
            if (fieldInfo.getReflectField().getName().equals(fieldName)
                    || fieldInfo.getTableColumnName().equals(fieldName)) {
                return fieldInfo;
            }
        }

        FieldInfo idField = mouvementTableMap.getFieldID();
        if (idField != null && (idField.getReflectField().getName().equals(fieldName)
                || idField.getTableColumnName().equals(fieldName))) {
            return idField;
        }

        throw new IllegalArgumentException("Champ introuvable : " + fieldName);
    }

    @Override
    public DFWInput createInput(FieldInfo fieldInfo, Mouvement movement) {
        if (fieldInfo instanceof ManyToOneFieldInfo) {
            return createManyToOneInput(fieldInfo, movement);
        }

        if (fieldInfo.getReflectField().getType() == LocalDateTime.class) {
            return new DFWDateField(fieldInfo, movement);
        }

        return new DFWTextField(fieldInfo, movement);
    }

    public DFWInput createManyToOneInput(FieldInfo fieldInfo, Mouvement movement) {
        Vector<Object> items = new Vector<>();
        Class<?> relationType = fieldInfo.getReflectField().getType();

        if (fieldInfo instanceof ManyToOneFieldInfo) {
            relationType = ((ManyToOneFieldInfo) fieldInfo).getManyToOneType();
        }

        try (GenericDao dao = new GenericDao()) {
            items = dao.getAll(relationType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new DFWComboBox(fieldInfo, movement, items);
    }

    @Override
    protected InsertionMultipleRowPanel<Mouvement> createRowPanel() {
        return new MouvementInsertionMultipleRowPanel(this);
    }

    @Override
    protected void valider(List<Mouvement> items) throws Exception {
        items.sort(Comparator.comparing(Mouvement::getDateMouvement, Comparator.nullsLast(Comparator.naturalOrder())));
        for (Mouvement m : items) {
            MouvementService.insertMouvement(m);
        }
    }
}
