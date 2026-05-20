package com.example.stock.dirkfw.display.util;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

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

    }

