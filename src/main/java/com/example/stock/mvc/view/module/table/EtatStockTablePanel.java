package com.example.stock.mvc.view.module.table;

import com.example.stock.dirkfw.display.classes.GenericTablePanel;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.example.stock.mvc.model.EtatStock;

public class EtatStockTablePanel extends GenericTablePanel {

    public EtatStockTablePanel() {
        super();
    }
    @Override
    protected DefaultTableCellRenderer createDefaultCellRenderer() {
        final DefaultTableCellRenderer base = super.createDefaultCellRenderer();
        return createEtatStockRenderer(base);
    }

    private DefaultTableCellRenderer createEtatStockRenderer(final DefaultTableCellRenderer base) {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component cell = base.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (isBadStock(getRowObject(row)) && !isSelected) {
                    applyBadColors(cell);
                }

                return cell;
            }
        };
    }

    private boolean isBadStock(Object rowObj) {
        if (!(rowObj instanceof EtatStock)) return false;
        EtatStock es = (EtatStock) rowObj;
        try {
            Double q = es.getQteStock();
            return q != null && q <= 0.0;
        } catch (Exception ex) {
            return false;
        }
    }

    private void applyBadColors(Component cell) {
        cell.setBackground(new Color(255, 200, 200));
        cell.setForeground(Color.BLACK);
    }
}