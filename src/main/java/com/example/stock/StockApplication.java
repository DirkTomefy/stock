package com.example.stock;
import com.example.stock.dirkfw.DirkFwConfig;
import com.example.stock.context.DatabaseContext;
import com.example.stock.dirkfw.db.GenericDao;
import com.example.stock.dirkfw.display.classes.DFWFenetre;
import com.example.stock.dirkfw.start.mapping.TableMap;
import com.example.stock.mvc.view.DetailFormView;
import com.example.stock.mvc.view.ArticleFormInsertView;
import com.example.stock.mvc.view.MouvementFormInsertView;

public class StockApplication extends DFWFenetre{

	public static void init() throws Exception {
		DirkFwConfig.setClassInfos(TableMap.getAllClassFromPackage("com.example.stock.mvc.model"));
	}

	public void initOnglet(){
		ArticleFormInsertView articleFormInsert = new ArticleFormInsertView();
		addTab("Ajouter un article", articleFormInsert);
		MouvementFormInsertView mouvementFormInsert = new MouvementFormInsertView();
		addTab("Ajouter un mouvement", mouvementFormInsert);
		DetailFormView detailFormView = new DetailFormView();
		addTab("Détails mouvements", detailFormView);
	}
	public StockApplication(){
		super();
		setTitle("Stock Management");
		setSize(800, 600);
		setLocationRelativeTo(null);
		initOnglet();
	}
	public static void main(String[] args) throws Exception {
		init();
		GenericDao.initializeContext(new DatabaseContext());
		StockApplication app = new StockApplication();
		app.setVisible(true);
	}

}
