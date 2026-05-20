package com.example.stock.dirkfw.display.util;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class DisplayUtil {
    public static void displayPopUp(JComponent panel,String title){        
        JOptionPane.showConfirmDialog(
            null, 
            panel, 
            title, 
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

    }

    public static void displayPopUpErr(JPanel parent,Exception e){
        JOptionPane.showMessageDialog(parent, "Erreur : " + e.getMessage() , "Erreur", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();

    }
    }

