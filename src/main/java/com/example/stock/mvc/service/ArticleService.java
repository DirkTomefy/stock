package com.example.stock.mvc.service;
import com.example.stock.dirkfw.db.GenericDao;

public class ArticleService {
    public static void insertArticle(Object article) {
        if (article == null)
            throw new IllegalArgumentException("Article cannot be null");
        try (GenericDao dao = new GenericDao()) {
            if (article instanceof com.example.stock.mvc.model.Article) {
                com.example.stock.mvc.model.Article art = (com.example.stock.mvc.model.Article) article;
                if (art.getLibelle() == null || art.getLibelle().isEmpty()) {
                    throw new IllegalArgumentException("Article libelle cannot be null or empty");
                }
            } else {
                throw new IllegalArgumentException("Invalid article type");
            }
            dao.save(article);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
