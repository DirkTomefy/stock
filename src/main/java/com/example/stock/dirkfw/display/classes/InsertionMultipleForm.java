package com.example.stock.dirkfw.display.classes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import com.example.stock.dirkfw.display.interfaces.DFWInput;
import com.example.stock.dirkfw.display.util.DisplayUtil;
import com.example.stock.dirkfw.start.mapping.FieldInfo;

public abstract class InsertionMultipleForm<T> extends JPanel {

    protected final JPanel rowsContainer;
    protected final List<InsertionMultipleRowPanel<T>> rows;
    protected final JButton addRowButton;
    protected final JButton validateButton;

    public InsertionMultipleForm() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(new Color(245, 245, 245));

        this.rows = new ArrayList<>();

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

        this.addRowButton.addActionListener(e -> addRow());
        this.validateButton.addActionListener(e -> validateAndInsert());

        actionPanel.add(addRowButton);
        actionPanel.add(validateButton);
        add(actionPanel, BorderLayout.SOUTH);
    }

    protected JButton buildButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new java.awt.Dimension(120, 38));
        return button;
    }

    protected void addRow() {
        InsertionMultipleRowPanel<T> rowPanel = createRowPanel();
        rows.add(rowPanel);
        rowsContainer.add(rowPanel);
        rowsContainer.add(Box.createVerticalStrut(10));
        rowsContainer.revalidate();
        rowsContainer.repaint();
    }

    public void removeRow(InsertionMultipleRowPanel<T> rowPanel) {
        if (!rows.remove(rowPanel)) {
            return;
        }

        rowsContainer.remove(rowPanel);
        refreshRowsContainer();

        if (rows.isEmpty()) {
            addRow();
        }
    }

    protected void refreshRowsContainer() {
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

    protected boolean isRowEmpty(InsertionMultipleRowPanel<T> row) {
        return row.isEmpty();
    }

    protected void validateAndInsert() {
        List<T> items = new ArrayList<>();

        try {
            for (InsertionMultipleRowPanel<T> row : rows) {
                if (isRowEmpty(row)) {
                    continue;
                }
                items.add(row.toEntity());
            }

            if (items.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Aucune ligne à insérer",
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            valider(items);

            JOptionPane.showMessageDialog(this,
                    "Éléments insérés avec succès",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);

            resetRows();
        } catch (Exception e) {
            DisplayUtil.displayPopUpErr(this, e);
        }
    }

    protected void resetRows() {
        rows.clear();
        rowsContainer.removeAll();
        addRow();
        rowsContainer.revalidate();
        rowsContainer.repaint();
    }

    public JPanel createFieldBox(String labelText, DFWInput input) {
        JPanel fieldBox = new JPanel();
        fieldBox.setLayout(new BoxLayout(fieldBox, BoxLayout.Y_AXIS));
        fieldBox.setBackground(new Color(245, 245, 245));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        label.setAlignmentX(JComponent.LEFT_ALIGNMENT);

        if (input instanceof JComponent) {
            ((JComponent) input).setAlignmentX(JComponent.LEFT_ALIGNMENT);
            ((JComponent) input).setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 40));
        }

        fieldBox.add(label);
        fieldBox.add(Box.createVerticalStrut(4));
        fieldBox.add((JComponent) input);
        return fieldBox;
    }

    protected abstract InsertionMultipleRowPanel<T> createRowPanel();

    public abstract FieldInfo getFieldInfo(String fieldName);

    public abstract DFWInput createInput(FieldInfo fieldInfo, T entity);

    protected void valider(List<T> items) throws Exception {
        // à surcharger dans les sous-classes
    }
}
