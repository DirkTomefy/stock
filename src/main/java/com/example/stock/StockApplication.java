package com.example.stock;

import com.example.stock.dirkfw.DirkFwObject;
import com.example.stock.dirkfw.dao.reflect.ReflectManager;

public class StockApplication {

	public static void init() throws Exception{
		DirkFwObject.setClassInfos(ReflectManager.getAllClassFromPackage("package com.example.stock.model"));
	}
	public static void main(String[] args) throws Exception {
		init();
	}

}
