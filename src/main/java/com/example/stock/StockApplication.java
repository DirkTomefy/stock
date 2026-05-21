package com.example.stock;
import com.example.stock.dirkfw.DirkFwConfig;

import java.sql.SQLException;
import com.example.stock.dirkfw.display.classes.DFWFenetre;
import com.example.stock.dirkfw.start.mapping.TableMap;
import com.example.stock.mvc.view.ArticleFormInsertView;
import com.example.stock.mvc.view.DetailFormView;
import com.example.stock.mvc.view.EtatStockView;
import com.example.stock.mvc.view.MouvementFormInsertView;

public class StockApplication extends DFWFenetre{

	public static void init() throws Exception {
		DirkFwConfig.setClassInfos(TableMap.getAllClassFromPackage("com.example.stock.mvc.model"));
	}

	public void initOnglet() throws ClassNotFoundException, SQLException{
		ArticleFormInsertView articleFormInsert = new ArticleFormInsertView();
		addTab("Ajouter un article", articleFormInsert);
		MouvementFormInsertView mouvementFormInsert = new MouvementFormInsertView();
		addTab("Ajouter un mouvement", mouvementFormInsert);
		DetailFormView detailFormView = new DetailFormView();
		addTab("Détails mouvements", detailFormView);
		EtatStockView etatStockView = new EtatStockView();
		addTab("Etat de stock", etatStockView);
	}
	public StockApplication() throws ClassNotFoundException, SQLException{
		super();
		setTitle("Stock Management");
		setSize(800, 600);
		setLocationRelativeTo(null);
		initOnglet();
	}
	public static void main(String[] args) throws Exception {
		init();
		StockApplication app = new StockApplication();
		app.setVisible(true);
	}

}
