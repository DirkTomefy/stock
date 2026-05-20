package com.example.stock.dirkfw.display.classes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseListener;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.example.stock.context.DatabaseContext;
import com.example.stock.dirkfw.DirkFwConfig;
import com.example.stock.dirkfw.annotation.display.IgnoreDisplayOpperation;
import com.example.stock.dirkfw.annotation.display.IgnoreFormulaire;
import com.example.stock.dirkfw.db.GenericDao;
import com.example.stock.dirkfw.display.interfaces.DFWInput;
import com.example.stock.dirkfw.start.mapping.FieldInfo;
import com.example.stock.dirkfw.start.mapping.TableMap;

public class GenericFormPanel extends JPanel {

    private Object object;
    private HashMap<String, DFWInput> inputs;
    private JButton validateButton;
    private MouseListener validateFormListener;
    private Connection connection;

    
    public MouseListener getValidateFormListener() {
        return validateFormListener;
    }

    public void setValidateFormListener(MouseListener validateFormListener) {
        this.validateFormListener = validateFormListener;
        this.validateButton.removeMouseListener(this.validateFormListener);
        this.validateButton.addMouseListener(validateFormListener);
    }

    public GenericFormPanel(Object object, MouseListener validateFormListener) throws ClassNotFoundException, SQLException {
        setLayout(new BorderLayout(10, 10));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(245, 245, 245));


        this.object = object;
        this.inputs = new HashMap<>();
        this.validateFormListener = validateFormListener;
        this.connection = DatabaseContext.createNewConnection();
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setBackground(new Color(245, 245, 245));

        FieldInfo[] fields = DirkFwConfig.getClassInfos().get(object.getClass().getName()).getAllFieldWithoutID();

        for (FieldInfo fieldInfo : fields) {
            if (isDisplayable(fieldInfo.getReflectField())) {
                addField(fieldInfo, fieldsPanel);
            }
        }

        add(fieldsPanel, BorderLayout.CENTER);
        this.validateButton = createButton();
        add(createButtonPanel(this.validateButton), BorderLayout.SOUTH);
    }

    private boolean isDisplayable(Field field) {
        return !TableMap.isIdField(field)
                && !field.isAnnotationPresent(IgnoreDisplayOpperation.class)
                && !field.isAnnotationPresent(IgnoreFormulaire.class);
    }

    private void addField(FieldInfo fieldInfo, JPanel panel) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.X_AXIS));
        fieldPanel.setBackground(new Color(245, 245, 245));
        fieldPanel.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 50));

        JLabel label = new JLabel(fieldInfo.getTableColumnName());
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        label.setPreferredSize(new java.awt.Dimension(120, 30));

        DFWInput input;
        if (fieldInfo.isManyToOne()) {
            input = createManyToOneInput(fieldInfo);
        } else if (isDateTimeField(fieldInfo.getReflectField())) {
            input = new DFWDateField(fieldInfo, object);
        } else {
            input = new DFWTextField(fieldInfo, object);
        }
        ((JComponent) input).setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 40));

        this.inputs.put(fieldInfo.getTableColumnName(), input);
        fieldPanel.add(label);
        fieldPanel.add(Box.createHorizontalStrut(10));
        fieldPanel.add((JComponent) input);

        panel.add(fieldPanel);
        panel.add(Box.createVerticalStrut(10));
    }

    private DFWInput createManyToOneInput(FieldInfo fieldInfo) {
        Vector<Object> items = new Vector<>();
        try (GenericDao dao = new GenericDao()) {
            items = dao.getAll(fieldInfo.getReflectField().getType(),connection);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new DFWComboBox(fieldInfo, object, items);
    }

    private boolean isDateTimeField(Field field) {
        return field.getType() == LocalDateTime.class;
    }

    private JPanel createButtonPanel(JButton boutonValider) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(new Color(245, 245, 245));

        buttonPanel.add(Box.createHorizontalGlue());
        JButton reload = new JButton("Recharger");
        reload.setFont(new Font("Arial", Font.BOLD, 12));
        reload.setBackground(new Color(100, 149, 237));
        reload.setForeground(Color.WHITE);
        reload.setFocusPainted(false);
        reload.setPreferredSize(new java.awt.Dimension(120, 40));
        reload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                reloadComboboxes();
            }
        });

        buttonPanel.add(reload);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(boutonValider);
        buttonPanel.add(Box.createHorizontalStrut(5));
        return buttonPanel;
    }

    private void reloadComboboxes() {
        for (DFWInput input : this.inputs.values()) {
            if (input instanceof DFWComboBox) {
                DFWComboBox combo = (DFWComboBox) input;
                FieldInfo fi = combo.getFieldInfo();
                Vector<Object> items = new Vector<>();
                try (GenericDao dao = new GenericDao()) {
                    items = dao.getAll(fi.getReflectField().getType(),connection);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                combo.removeAllItems();
                for (Object it : items) {
                    combo.addItem(it);
                }
            }
        }
    }

    private JButton createButton() {
        JButton validateButton = new JButton("Valider");
        validateButton.setFont(new Font("Arial", Font.BOLD, 12));
        validateButton.setBackground(new Color(70, 130, 180));
        validateButton.setForeground(Color.WHITE);
        validateButton.setFocusPainted(false);
        validateButton.setPreferredSize(new java.awt.Dimension(120, 40));

        if (validateFormListener != null) {
            validateButton.addMouseListener(validateFormListener);
        }
        return validateButton;
    }
    public HashMap<String, DFWInput> getInputs() {
        return inputs;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }


    public void fillObject(){
        try {
            for (DFWInput input : this.inputs.values()) {
                FieldInfo fieldInfo = input.getFieldInfo();
                Object value = input.getValue();
                // debug: afficher le champ et la valeur récupérée
                try {
                    String col = fieldInfo.getTableColumnName();
                    String valStr = (value == null) ? "null" : value.toString();
                    System.out.println("[fillObject] field=" + col + " value=" + valStr + " (class=" + (value==null?"null":value.getClass().getName()) + ")");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                fieldInfo.setFieldValue(this.object, value);
            }
            System.out.println(""+object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetObject(){
        try {
            TableMap tableMap = DirkFwConfig.getClassInfos().get(object.getClass().getName());
            FieldInfo[] fields = tableMap.getAllFieldWithoutID();
            for (FieldInfo fieldInfo : fields) {
                fieldInfo.setFieldValue(this.object, null);
            }
            FieldInfo idFieldInfo = DirkFwConfig.getClassInfos().get(object.getClass().getName()).getFieldID();
            if (idFieldInfo != null) 
                idFieldInfo.setFieldValue(this.object, null);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}