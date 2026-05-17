package com.example.stock.dirkfw.display.classes;

import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class DFWFenetre extends JFrame {
    private JTabbedPane tabbedPane;
    private HashMap<String, GenericFormPanel> forms;
    private HashMap<String, GenericTablePanel> tables;
    
    

    public DFWFenetre() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tabbedPane = new JTabbedPane();
        add(tabbedPane);
        forms = new HashMap<>();
        tables = new HashMap<>();
    }
    
    public void addTab(String title, JComponent panel) {
        tabbedPane.addTab(title, panel);
        if (panel instanceof GenericFormPanel) {
            forms.put(title, (GenericFormPanel) panel);
        } else if (panel instanceof GenericTablePanel) {
            tables.put(title, (GenericTablePanel) panel);
        }
    }
    
    public void removeTab(String title) {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            if (tabbedPane.getTitleAt(i).equals(title)) {
                tabbedPane.removeTabAt(i);
                break;
            }
        }
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }
}
