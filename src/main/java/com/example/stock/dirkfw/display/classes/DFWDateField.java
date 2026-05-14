package com.example.stock.dirkfw.display.classes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.BoxLayout;

import com.example.stock.dirkfw.DirkFwObject;
import com.example.stock.dirkfw.display.interfaces.DFWInput;
import com.example.stock.dirkfw.start.mapping.FieldInfo;
import java.util.Date;
import java.util.Calendar;

public class DFWDateField extends JPanel implements DFWInput {
    
    private FieldInfo fieldInfo;

    private DirkFwObject object;
    
    public DirkFwObject getObject() {
        return object;
    }

    public void setObject(DirkFwObject object) {
        this.object = object;
    }

    private JSpinner dateSpinner;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    public static DateTimeFormatter getFormatter() {
        return formatter;
    }

    public DFWDateField(FieldInfo fieldInfo, DirkFwObject object) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        
        this.fieldInfo = fieldInfo;
        this.object = object;
        
        // Créer le spinner pour la date
        SpinnerDateModel model = new SpinnerDateModel();
        dateSpinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd HH:mm");
        dateSpinner.setEditor(editor);
        
        // Initialiser avec la valeur actuelle si elle existe
        try {
            Object value = fieldInfo.getFieldValue(object);
            if (value instanceof LocalDateTime) {
                LocalDateTime ldt = (LocalDateTime) value;
                Calendar cal = Calendar.getInstance();
                cal.set(ldt.getYear(), ldt.getMonthValue() - 1, ldt.getDayOfMonth(),
                        ldt.getHour(), ldt.getMinute(), ldt.getSecond());
                model.setValue(cal.getTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        add(dateSpinner);
    }
    
    public Object getValue() {
        Date date = (Date) dateSpinner.getValue();
        if (date == null) {
            return null;
        }
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        
        return LocalDateTime.of(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) + 1,
            cal.get(Calendar.DAY_OF_MONTH),
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            cal.get(Calendar.SECOND)
        );
    }
    
    public void setValue(Object value) {
        if (value instanceof LocalDateTime) {
            LocalDateTime ldt = (LocalDateTime) value;
            Calendar cal = Calendar.getInstance();
            cal.set(ldt.getYear(), ldt.getMonthValue() - 1, ldt.getDayOfMonth(),
                    ldt.getHour(), ldt.getMinute(), ldt.getSecond());
            dateSpinner.setValue(cal.getTime());
        }
    }

    @Override
    public FieldInfo getFieldInfo() {
        return this.fieldInfo;
    }
}
