package com.example.stock;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDateTime;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.example.stock.dirkfw.DirkFwObject;
import com.example.stock.dirkfw.display.classes.GenericFormPanel;
import com.example.stock.dirkfw.start.reflect.ReflectManager;
import com.example.stock.model.MouvementModel;

public class StockApplication {

	public static void init() throws Exception{
		DirkFwObject.setClassInfos(ReflectManager.getAllClassFromPackage("com.example.stock.model"));
	}
	public static void main(String[] args) throws Exception {
		init();
		
		// Test GenericFormPanel avec MouvementModel (qui contient un champ LocalDateTime)
		MouvementModel mouvement = new MouvementModel();
		mouvement.setDateMouvement(LocalDateTime.now());
		
		MouseListener validateListener = new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(null, "Formulaire validé !");
			}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
		};
		
		GenericFormPanel formPanel = new GenericFormPanel(mouvement, validateListener);
		
		JFrame frame = new JFrame("Gestion des Mouvements - Formulaire");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.add(formPanel);
		frame.setSize(500, 600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
