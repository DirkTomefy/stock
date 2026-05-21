package com.example.stock.mvc.view.module.row;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.example.stock.dirkfw.display.interfaces.DFWInput;
import com.example.stock.mvc.model.Mouvement;
import com.example.stock.mvc.view.ArticleMouvementManyToOne;

public class MouvementRowPanel extends JPanel {

        public final Mouvement mouvement;
        public final DFWInput articleInput;
        public final DFWInput typeInput;
        public final DFWInput dateInput;
        public final DFWInput quantiteInput;
        public final DFWInput puInput;

        public MouvementRowPanel(ArticleMouvementManyToOne articleForm) {
            setLayout(new BorderLayout(10, 10));
            setBackground(Color.WHITE);
            setBorder(new EmptyBorder(10, 10, 10, 10));

            this.mouvement = new Mouvement();
            this.mouvement.setDateMouvement(LocalDateTime.now());

            JPanel fieldsPanel = new JPanel();
            fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.X_AXIS));
            fieldsPanel.setBackground(Color.WHITE);

            this.articleInput = articleForm.createInput(articleForm.getFieldInfo("article"), mouvement);
            this.typeInput = articleForm.createInput(articleForm.getFieldInfo("typeMouvement"), mouvement);
            this.dateInput = articleForm.createInput(articleForm.getFieldInfo("dateMouvement"), mouvement);
            this.quantiteInput = articleForm.createInput(articleForm.getFieldInfo("quantite"), mouvement);
            this.puInput = articleForm.createInput(articleForm.getFieldInfo("pu"), mouvement);

            fieldsPanel.add(articleForm.createFieldBox("Article", articleInput));
            fieldsPanel.add(Box.createHorizontalStrut(10));
            fieldsPanel.add(articleForm.createFieldBox("Type", typeInput));
            fieldsPanel.add(Box.createHorizontalStrut(10));
            fieldsPanel.add(articleForm.createFieldBox("Date", dateInput));
            fieldsPanel.add(Box.createHorizontalStrut(10));
            fieldsPanel.add(articleForm.createFieldBox("Quantité", quantiteInput));
            fieldsPanel.add(Box.createHorizontalStrut(10));
            fieldsPanel.add(articleForm.createFieldBox("PU", puInput));

            JButton removeButton = new JButton("Retirer");
            removeButton.setFont(new Font("Arial", Font.BOLD, 12));
            removeButton.setBackground(new Color(220, 20, 60));
            removeButton.setForeground(Color.WHITE);
            removeButton.setFocusPainted(false);
            removeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    articleForm.removeRow(MouvementRowPanel.this);
                }
            });

            add(fieldsPanel, BorderLayout.CENTER);
            add(removeButton, BorderLayout.EAST);
        }

       

         public Object getArticleValue() {
            return articleInput.getValue();
        }

        public Object getTypeValue() {
            return typeInput.getValue();
        }

        public Object getQuantiteValue() {
            return quantiteInput.getValue();
        }

        public Object getPuValue() {
            return puInput.getValue();
        }

        public Mouvement toMouvement() throws Exception {
            articleInput.getFieldInfo().setFieldValue(mouvement, articleInput.getValue());
            typeInput.getFieldInfo().setFieldValue(mouvement, typeInput.getValue());
            dateInput.getFieldInfo().setFieldValue(mouvement, dateInput.getValue());
            quantiteInput.getFieldInfo().setFieldValue(mouvement, quantiteInput.getValue());
            puInput.getFieldInfo().setFieldValue(mouvement, puInput.getValue());
            return mouvement;
        }
    }