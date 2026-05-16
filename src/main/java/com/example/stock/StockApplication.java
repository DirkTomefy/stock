package com.example.stock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import com.example.stock.dirkfw.DirkFwConfig;
import com.example.stock.dirkfw.db.util.ComparaisonOperation;
import com.example.stock.context.DatabaseContext;
import com.example.stock.dirkfw.db.GenericDao;
import com.example.stock.dirkfw.start.mapping.TableMap;
import com.example.stock.model.Category;
import com.example.stock.model.Product;

public class StockApplication {

	public static void init() throws Exception {
		DirkFwConfig.setClassInfos(TableMap.getAllClassFromPackage("com.example.stock.model"));
	}

	public static void main(String[] args) throws Exception {
		init();

		GenericDao dao = new GenericDao();
		dao.dbctx = new DatabaseContext();

		Category category = new Category();
		category.setName("Catégorie CRUD");
		category.setDescription("Test OneToMany côté db");
		dao.save(category);
		System.out.println("SAVE category => " + category);

		Product product = new Product();
		product.setName("Produit CRUD");
		product.setPrice(10.50);
		product.setStockQuantity(8);
		product.setCategory(category);
		dao.save(product);
		System.out.println("SAVE product => " + product);

		Category foundById = new Category();
		foundById.setId(category.getId());
		dao.findById(foundById);
		System.out.println("FINDBYID category => " + foundById);
		System.out.println("FINDBYID category.products size => "
				+ (foundById.getProducts() == null ? 0 : foundById.getProducts().size()));

		Product where = new Product();
		where.setCategory(category);
		Vector<Object> foundProducts = dao.find(where);
		System.out.println("FIND products by category => " + foundProducts.size());
		for (Object item : foundProducts) {
			System.out.println(item);
		}

		product.setName("Produit CRUD modifié");
		product.setPrice(12.75);
		dao.update(product);
		System.out.println("UPDATE product => " + product);

		Product reloaded = new Product();
		reloaded.setId(product.getId());
		dao.findById(reloaded);
		System.out.println("FINDBYID product => " + reloaded);

		// Test du nouveau findAll avec opérateurs de comparaison
		System.out.println("\n=== TEST FINDALL AVEC OPÉRATEURS ===");
		Product queryWithOps = new Product();
		queryWithOps.setPrice(11.0);  // Chercher les produits avec price > 11.0
		queryWithOps.setName("Eau minérale");
		HashMap<String, ComparaisonOperation> operations = new HashMap<>();
		operations.put("price", ComparaisonOperation.INF);  // price > ?
		
		Vector<Object> foundByOps = dao.findAll(queryWithOps, operations);
		System.out.println("FINDALL products with price < 11.0 => " + foundByOps.size());
		for (Object item : foundByOps) {
			System.out.println(item);
		}

		// Test saveWithOneToMany
		System.out.println("\n=== TEST SAVEWITHONETOMNAY ===");
		Category categoryWithProducts = new Category();
		categoryWithProducts.setName("Catégorie avec produits");
		categoryWithProducts.setDescription("Test saveWithOneToMany");
		
		Product product1 = new Product();
		product1.setName("Produit 1");
		product1.setPrice(5.50);
		product1.setStockQuantity(10);
		product1.setCategory(categoryWithProducts);
		
		Product product2 = new Product();
		product2.setName("Produit 2");
		product2.setPrice(8.99);
		product2.setStockQuantity(15);
		product2.setCategory(categoryWithProducts);
		
		ArrayList<Product> products = new ArrayList<>();
		products.add(product1);
		products.add(product2);
		categoryWithProducts.setProducts(products);
		
		dao.saveWithOneToMany(categoryWithProducts);
		System.out.println("SAVEWITHONETOMNAY category => " + categoryWithProducts);

		// Test updateWithOneToMany
		System.out.println("\n=== TEST UPDATEWITHONETOMNAY ===");
		product1.setPrice(6.99);
		product2.setPrice(9.99);
		
		dao.updateWithOneToMany(categoryWithProducts);
		System.out.println("UPDATEWITHONETOMNAY category => " + categoryWithProducts);

		dao.delete(product);
		System.out.println("DELETE product => id " + product.getId());

		dao.delete(category);
		System.out.println("DELETE category => id " + category.getId());
	}

}
