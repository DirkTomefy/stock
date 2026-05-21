package com.example.stock.mvc.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import com.example.stock.dirkfw.DirkFwConfig;
import com.example.stock.dirkfw.db.GenericDao;
import com.example.stock.dirkfw.display.classes.DFWComboBox;
import com.example.stock.dirkfw.display.classes.DFWDateField;
import com.example.stock.dirkfw.display.classes.DFWTextField;
import com.example.stock.dirkfw.display.interfaces.DFWInput;
import com.example.stock.dirkfw.display.util.DisplayUtil;
import com.example.stock.dirkfw.start.mapping.FieldInfo;
import com.example.stock.dirkfw.start.mapping.ManyToOneFieldInfo;
import com.example.stock.dirkfw.start.mapping.TableMap;
import com.example.stock.mvc.model.Mouvement;
import com.example.stock.mvc.service.MouvementService;
import com.example.stock.mvc.view.module.row.MouvementRowPanel;

public class ArticleMouvementManyToOne extends JPanel {

    public final JPanel rowsContainer;
    public final List<MouvementRowPanel> rows;
    public final JButton addRowButton;
    public final JButton validateButton;
    public final TableMap mouvementTableMap;

    public ArticleMouvementManyToOne() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(new Color(245, 245, 245));

        this.rows = new ArrayList<>();
        this.mouvementTableMap = resolveTableMap();

        this.rowsContainer = new JPanel();
        this.rowsContainer.setLayout(new BoxLayout(rowsContainer, BoxLayout.Y_AXIS));
        this.rowsContainer.setBackground(new Color(245, 245, 245));

        JScrollPane scrollPane = new JScrollPane(rowsContainer);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setBackground(new Color(245, 245, 245));

        this.addRowButton = buildButton("Ajouter");
        this.validateButton = buildButton("Valider");

        this.addRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRow();
            }
        });

        this.validateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validateAndInsert();
            }
        });

        actionPanel.add(addRowButton);
        actionPanel.add(validateButton);
        add(actionPanel, BorderLayout.SOUTH);

        addRow();
    }

    public JButton buildButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new java.awt.Dimension(120, 38));
        return button;
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

    public JPanel createFieldBox(String labelText, DFWInput input) {
        JPanel fieldBox = new JPanel();
        fieldBox.setLayout(new BoxLayout(fieldBox, BoxLayout.Y_AXIS));
        fieldBox.setBackground(new Color(245, 245, 245));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        label.setAlignmentX(LEFT_ALIGNMENT);

        if (input instanceof JComponent) {
            ((JComponent) input).setAlignmentX(LEFT_ALIGNMENT);
            ((JComponent) input).setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 40));
        }

        fieldBox.add(label);
        fieldBox.add(Box.createVerticalStrut(4));
        fieldBox.add((JComponent) input);
        return fieldBox;
    }

    protected void addRow() {
        MouvementRowPanel rowPanel = new MouvementRowPanel(this);
        rows.add(rowPanel);
        rowsContainer.add(rowPanel);
        rowsContainer.add(Box.createVerticalStrut(10));
        rowsContainer.revalidate();
        rowsContainer.repaint();
    }

    public void removeRow(MouvementRowPanel rowPanel) {
        if (!rows.remove(rowPanel)) {
            return;
        }

        rowsContainer.remove(rowPanel);
        refreshRowsContainer();

        if (rows.isEmpty()) {
            addRow();
        }
    }

    public void refreshRowsContainer() {
        rowsContainer.removeAll();
        for (int i = 0; i < rows.size(); i++) {
            rowsContainer.add(rows.get(i));
            if (i < rows.size() - 1) {
                rowsContainer.add(Box.createVerticalStrut(10));
            }
        }
        rowsContainer.revalidate();
        rowsContainer.repaint();
    }

    public boolean isRowEmpty(MouvementRowPanel row) {
        return row.getArticleValue() == null
                && row.getTypeValue() == null
                && row.getQuantiteValue() == null
                && row.getPuValue() == null;
    }

    public void validateAndInsert() {
        List<Mouvement> mouvements = new ArrayList<>();

        try {
            for (int i = 0; i < rows.size(); i++) {
                MouvementRowPanel row = rows.get(i);
                if (isRowEmpty(row)) {
                    continue;
                }

                Mouvement mouvement = row.toMouvement();
                mouvements.add(mouvement);
            }

            if (mouvements.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Aucune ligne à insérer",
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            for (Mouvement mouvement : mouvements) {
                MouvementService.insertMouvement(mouvement);
            }

            JOptionPane.showMessageDialog(this,
                    "Mouvements insérés avec succès",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);

            resetRows();
        } catch (Exception e) {
            DisplayUtil.displayPopUpErr(this, e);
        }
    }

    public void resetRows() {
        rows.clear();
        rowsContainer.removeAll();
        addRow();
        rowsContainer.revalidate();
        rowsContainer.repaint();
    }


}