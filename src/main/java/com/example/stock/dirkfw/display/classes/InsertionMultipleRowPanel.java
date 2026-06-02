package com.example.stock.dirkfw.display.classes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.example.stock.dirkfw.display.interfaces.DFWInput;

public abstract class InsertionMultipleRowPanel<T> extends JPanel {

    protected final InsertionMultipleForm<T> parent;
    protected final HashMap<String, DFWInput> inputs;
    protected final JPanel fieldsPanel;
    protected final JButton removeButton;

    public InsertionMultipleRowPanel(InsertionMultipleForm<T> parent) {
        this.parent = parent;
        this.inputs = new HashMap<>();

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        this.fieldsPanel = new JPanel();
        this.fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.X_AXIS));
        this.fieldsPanel.setBackground(Color.WHITE);

        this.removeButton = buildRemoveButton();

        add(fieldsPanel, BorderLayout.CENTER);
        add(removeButton, BorderLayout.EAST);
    }

    protected void putInput(String name, DFWInput input) {
        inputs.put(name, input);
    }

    protected void addField(String labelText, String name, DFWInput input) {
        if (fieldsPanel.getComponentCount() > 0) {
            fieldsPanel.add(Box.createHorizontalStrut(10));
        }

        fieldsPanel.add(parent.createFieldBox(labelText, input));
        putInput(name, input);
    }

    protected JButton buildRemoveButton() {
        JButton button = new JButton("Retirer");
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(new Color(220, 20, 60));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.addActionListener(e -> parent.removeRow(this));
        return button;
    }

    public DFWInput getInput(String name) {
        return inputs.get(name);
    }

    protected void finishLayout() {
        revalidate();
        repaint();
    }

    public abstract T toEntity() throws Exception;

    public abstract boolean isEmpty();
}
