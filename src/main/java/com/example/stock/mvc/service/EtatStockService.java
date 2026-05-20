package com.example.stock.mvc.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.example.stock.dirkfw.db.GenericDao;
import com.example.stock.dirkfw.db.util.ComparaisonOperation;
import com.example.stock.mvc.model.Article;
import com.example.stock.mvc.model.EtatStock;
import com.example.stock.mvc.model.Mouvement;

public class EtatStockService {

    public static Vector<Object> getEtatStockGeneral() throws Exception {

        return getEtatStockGeneral(LocalDateTime.now());
    }

    public static Vector<Object> getEtatStockGeneral(LocalDateTime date) throws Exception {
        try (GenericDao dao = new GenericDao()) {

            Vector<Object> articlesRaw = dao.getAll(Article.class);

            HashMap<String, ComparaisonOperation> operations = new HashMap<>();
            operations.put("date_mouvement", ComparaisonOperation.INFEQ);

            Mouvement whereMouvement = new Mouvement();
            whereMouvement.setDateMouvement(date);

            Vector<Object> mouvementsRaw = dao.findAll(whereMouvement, operations);

            Map<Integer, Mouvement> lastMouvementByArticle = new HashMap<>();

            for (Object mouvementObj : mouvementsRaw) {
                Mouvement mouvement = (Mouvement) mouvementObj;
                if (mouvement.getArticle() == null || mouvement.getArticle().getId() == null) {
                    continue;
                }

                Integer articleId = mouvement.getArticle().getId();
                Mouvement current = lastMouvementByArticle.get(articleId);

                if (current == null || isAfter(mouvement, current)) {
                    lastMouvementByArticle.put(articleId, mouvement);
                }
            }

            Vector<Object> etats = new Vector<>();

            for (Object articleObj : articlesRaw) {
                Article article = (Article) articleObj;

                EtatStock etat = new EtatStock();
                etat.setIdArticle(article.getId());
                etat.setLibelleArticle(article.getLibelle());
                etat.setSigleGestionStock(
                        article.getMethodGestionStock() == null ? null : article.getMethodGestionStock().getSigle());

                Mouvement last = lastMouvementByArticle.get(article.getId());
                if (last == null) {
                    etat.setQteStock(0.0);
                    etat.setMoneyValueStock(0.0);
                    etat.setCump(0.0);
                    etat.setDateDernierMouvement(null);
                } else {
                    etat.setQteStock(last.getQteStock() == null ? 0.0 : last.getQteStock());
                    etat.setMoneyValueStock(last.getMoneyValueStock() == null ? 0.0 : last.getMoneyValueStock());
                    etat.setCump(last.getCump() == null ? 0.0 : last.getCump());
                    etat.setDateDernierMouvement(last.getDateMouvement());
                }

                etats.add(etat);
            }

            return etats;
        }
    }

    private static boolean isAfter(Mouvement candidate, Mouvement reference) {
        if (candidate.getDateMouvement() == null) {
            return false;
        }
        if (reference.getDateMouvement() == null) {
            return true;
        }

        int dateCompare = candidate.getDateMouvement().compareTo(reference.getDateMouvement());
        if (dateCompare > 0) {
            return true;
        }
        if (dateCompare < 0) {
            return false;
        }

        if (candidate.getId() == null) {
            return false;
        }
        if (reference.getId() == null) {
            return true;
        }
        return candidate.getId() > reference.getId();
    }
}
