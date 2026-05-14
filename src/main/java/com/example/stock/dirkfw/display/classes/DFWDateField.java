package com.example.stock.dirkfw.display.classes;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.swing.JFormattedTextField;
import javax.swing.text.NumberFormatter;

import com.example.stock.dirkfw.start.mapping.FieldInfo;

public class DFWDateField extends JFormattedTextField {
    
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private FieldInfo fieldInfo;
    
    public DFWDateField(FieldInfo fieldInfo, Object object) {
        super();
        this.fieldInfo = fieldInfo;
        
        // Configuration du format
        NumberFormatter formatter = new NumberFormatter();
        setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(formatter));
        
        // Définir la valeur initiale
        String value = getFieldValueAsString(fieldInfo, object);
        setText(value);
        
        setColumns(20);
    }
    
    public static String getFieldValueAsString(FieldInfo fieldInfo, Object object) {
        try {
            Object value = fieldInfo.getFieldValue(object);
            if (value == null) {
                return "";
            }
            
            if (value instanceof LocalDateTime) {
                return ((LocalDateTime) value).format(DATETIME_FORMATTER);
            } else if (value instanceof LocalDate) {
                return ((LocalDate) value).format(DATE_FORMATTER);
            } else if (value instanceof Date) {
                LocalDateTime ldt = ((Date) value).toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
                return ldt.format(DATETIME_FORMATTER);
            }
            
            return value.toString();
        } catch (IllegalAccessException | InvocationTargetException e) {
            return "";
        }
    }
    
    public Object getFieldValue() {
        String text = getText().trim();
        if (text.isEmpty()) {
            return null;
        }
        
        try {
            Class<?> fieldType = fieldInfo.getReflectField().getType();
            
            if (fieldType == LocalDateTime.class) {
                return LocalDateTime.parse(text, DATETIME_FORMATTER);
            } else if (fieldType == LocalDate.class) {
                return LocalDate.parse(text, DATE_FORMATTER);
            } else if (fieldType == Date.class) {
                LocalDateTime ldt = LocalDateTime.parse(text, DATETIME_FORMATTER);
                return java.sql.Timestamp.valueOf(ldt);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du parsing de la date: " + e.getMessage());
        }
        
        return null;
    }
}

