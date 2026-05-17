package com.example.stock.mvc.view;

import com.example.stock.dirkfw.display.classes.GenericFormPanel;
import com.example.stock.mvc.controller.ArticleFormController;
import com.example.stock.mvc.model.Article;

public class ArticleFormInsertView extends GenericFormPanel {

    public ArticleFormInsertView() {
        super(new Article(), null);  
        ArticleFormController controller = new ArticleFormController(this);  
        setValidateFormListener(controller);    
    }
    
}
