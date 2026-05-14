package com.example.stock.dirkfw.display.classes;

import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.lang.reflect.Field;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.example.stock.dirkfw.DirkFwObject;
import com.example.stock.dirkfw.start.mapping.FieldInfo;
import com.example.stock.dirkfw.start.reflect.ReflectManager;

public class GenericFormPanel extends JPanel {

    private final Object object;
    private HashMap<String,DFWTextField> inputs;
    private MouseListener validateFormListener;
    public GenericFormPanel(Object object,MouseListener validateFormListener) {
        super(new GridLayout(0, 2));

        this.object = object;
        this.inputs = new HashMap<>();
        this.validateFormListener = validateFormListener;

        FieldInfo[] fields =  DirkFwObject.getClassInfos().get(object.getClass().getName()).getAllFieldWithoutID();

        for (FieldInfo fieldInfo : fields) {
            if(isDisplayable(fieldInfo.getReflectField()))
            addField(fieldInfo);
        }
        
        addValidateButton();
    }

   

    private boolean isDisplayable(Field field) {
        return ReflectManager.isFieldOpperable(field)
                && !ReflectManager.isIdField(field);
    }

    private void addField(FieldInfo fieldInfo) {
        JLabel label = new JLabel(fieldInfo.getDFWName());

        DFWTextField input = new DFWTextField(fieldInfo, object);
        this.inputs.put(fieldInfo.getDFWName(), input);
        add(label);
        add(input);
    }

    private void addValidateButton() {
        JButton validateButton = new JButton("Valider");
        if (validateFormListener != null) {
            validateButton.addMouseListener(validateFormListener);
        }
        add(validateButton);
    }
    
}