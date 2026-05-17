package com.example.stock;
import com.example.stock.dirkfw.DirkFwConfig;
import com.example.stock.context.DatabaseContext;
import com.example.stock.dirkfw.db.GenericDao;
import com.example.stock.dirkfw.display.classes.DFWFenetre;
import com.example.stock.dirkfw.start.mapping.TableMap;
import com.example.stock.view.ArticleFormInsert;

public class StockApplication extends DFWFenetre{

	public static void init() throws Exception {
		DirkFwConfig.setClassInfos(TableMap.getAllClassFromPackage("com.example.stock.model"));
	}

	public void initOnglet(){
		ArticleFormInsert articleFormInsert = new ArticleFormInsert();
		addTab("Ajouter un article", articleFormInsert);
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
		StockApplication app = new StockApplication();
		app.setVisible(true);
		GenericDao dao = new GenericDao();
		dao.dbctx = new DatabaseContext();
	}

}
