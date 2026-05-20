package com.example.stock.mvc.service;

import java.util.Vector;
import com.example.stock.dirkfw.db.GenericDao;
import com.example.stock.mvc.model.Article;
import com.example.stock.mvc.model.Mouvement;

public class MouvementService {

   
    public  static Mouvement getLastMouvementInfo(Article article) throws Exception {

        try (GenericDao dao = new GenericDao()) {

            dao.setAlterName("last_mouvement");

            Mouvement where = new Mouvement();
            where.setArticle(article);

            Vector<Object> res = dao.find(where);

            if (res.isEmpty()) {
                return Mouvement.defaultMouvement();
            }

            return (Mouvement) res.get(0);
        }
    }

    
    private static void processEntreeForLifoFifo(Mouvement m, Mouvement last) {

        m.setTypeMouvement("ENTREE");

        m.setValeur(calcValeur(m));

        m.setQteStock(last.getQteStock() + m.getQuantite());

        m.setMoneyValueStock(last.getMoneyValueStock() + m.getValeur());
        
        m.setQuantitePrise(0);
        m.setTotalPriseForEntree(0);

        m.setSource(null);
    }

    
    public static void insertMouvementAsCUMP(Object mouvement) throws Exception {

        if (!(mouvement instanceof Mouvement)) return;
        Mouvement m = (Mouvement) mouvement;

        Mouvement last = getLastMouvementInfo(m.getArticle());

        m.setValeur(calcValeur(m));

        if (m.getTypeMouvement().equals("ENTREE")) {

            m.setQteStock(last.getQteStock() + m.getQuantite());
            m.setMoneyValueStock(last.getMoneyValueStock() + m.getValeur());

        } else {

            m.setQteStock(last.getQteStock() - m.getQuantite());
            m.setMoneyValueStock(last.getMoneyValueStock() - m.getValeur());
        }

        m.setCump(m.getMoneyValueStock() / m.getQteStock());
        
        try (GenericDao dao = new GenericDao()) {
            dao.save(m);
        }
    }

    
    public static void insertMouvmentAsFIFOENTREE(Object mouvement) throws Exception {

        if (!(mouvement instanceof Mouvement)) return;
        Mouvement m = (Mouvement) mouvement;

        Mouvement last = getLastMouvementInfo(m.getArticle());

        processEntreeForLifoFifo(m, last);
        
        try (GenericDao dao = new GenericDao()) {
            dao.save(m);
        }
    }

    // =========================================================
    // LIFO ENTREE
    // =========================================================
    public static void insertMouvmentAsLIFOENTREE(Object mouvement) throws Exception {

        if (!(mouvement instanceof Mouvement)) return;
        Mouvement m = (Mouvement) mouvement;

        Mouvement last = getLastMouvementInfo(m.getArticle());

        processEntreeForLifoFifo(m, last);
        
        try (GenericDao dao = new GenericDao()) {
            dao.save(m);
        }
    }

    // =========================================================
    // FIFO SORTIE
    // =========================================================
    public static  void insertMouvmentAsFIFOSORTIE(Object mouvement) throws Exception {

        processSortie(mouvement, "mouvement_fifo");
    }

    // =========================================================
    // LIFO SORTIE
    // =========================================================
    public   static void insertMouvmentAsLIFOSORTIE(Object mouvement) throws Exception {

        processSortie(mouvement, "mouvement_lifo");
    }

    // =========================================================
    // SORTIE COMMON LOGIC
    // =========================================================
    private static void processSortie(Object mouvement, String view) throws Exception {

        if (!(mouvement instanceof Mouvement)) return;
        Mouvement sortie = (Mouvement) mouvement;

        int reste = sortie.getQuantite();

        try (GenericDao readDao = new GenericDao(); GenericDao updateDao = new GenericDao(); GenericDao saveDao = new GenericDao()) {

            readDao.setAlterName(view);

            Mouvement where = new Mouvement();
            where.setArticle(sortie.getArticle());

            Vector<Object> entrees = readDao.find(where);

            for (Object obj : entrees) {

                if (reste <= 0) break;

                Mouvement entree = (Mouvement) obj;

                int dejaPrise = entree.getTotalPriseForEntree() == null ? 0 : entree.getTotalPriseForEntree();
                double disponible = entree.getQuantite() - dejaPrise;

                if (disponible <= 0) continue;

                int prise = (int) Math.min(reste, disponible);

                Mouvement m = buildSortie(sortie, entree, prise);

                reste -= prise;

                
                int nouvellePrise = dejaPrise + prise;
                entree.setTotalPriseForEntree(nouvellePrise);
                updateDao.update(entree);

                
                double qteStockRestante = entree.getQteStock() - nouvellePrise;
                double moneyValueStockRestante = entree.getMoneyValueStock() - (nouvellePrise * m.getPu());

                m.setQteStock(qteStockRestante);
                m.setMoneyValueStock(moneyValueStockRestante);

                saveDao.save(m);
            }
        }
    }

   
    private  static Mouvement buildSortie(Mouvement sortie, Mouvement entree, int prise) {

        Mouvement m = new Mouvement();

        m.setArticle(sortie.getArticle());
        m.setTypeMouvement("SORTIE");
        m.setDateMouvement(sortie.getDateMouvement());

        m.setSource(entree);
        m.setQuantitePrise(prise);

        m.setPu(entree.getPu());
        m.setQuantite(prise);

        m.setValeur(calcValeur(m));

        return m;
    }

    
    private static double calcValeur(Mouvement m) {
        return m.getPu() * m.getQuantite();
    }

    public static  void validateFormat(Mouvement m){
         if (m.getArticle() == null) {
            throw new IllegalArgumentException("Veuillez sélectionner un article");
        }
        if (m.getTypeMouvement() == null || m.getTypeMouvement().isEmpty()) {
            throw new IllegalArgumentException("Veuillez sélectionner un type de mouvement");
        }
        if (m.getQuantite() == null || m.getQuantite() <= 0) {
            throw new IllegalArgumentException("Veuillez entrer une quantité positive");
        }
        if (m.getPu() == null || m.getPu() <= 0) {
            throw new IllegalArgumentException("Veuillez entrer un prix unitaire positif");
        }
    }
    public static  void insertMouvement(Object mouvement) throws Exception {

        if (!(mouvement instanceof Mouvement)) return;
        Mouvement m = (Mouvement) mouvement;
        validateFormat(m);
        m.setQuantitePrise(0);
        m.setTotalPriseForEntree(0);
        m.setValeur(m.getPu()*m.getQuantite());

        if (m.getArticle() == null || m.getArticle().getMethodGestionStock() == null
                || m.getArticle().getMethodGestionStock().getSigle() == null) {
            insertMouvementAsCUMP(m);
            return;
        }

        String sigle = m.getArticle().getMethodGestionStock().getSigle();

        switch (sigle) {
        case "CUMP":
            insertMouvementAsCUMP(m);
            break;

        case "FIFO":
            if ("ENTREE".equalsIgnoreCase(m.getTypeMouvement())) {
                insertMouvmentAsFIFOENTREE(m);
            } else {
                insertMouvmentAsFIFOSORTIE(m);
            }
            break;

        case "LIFO":
            if ("ENTREE".equalsIgnoreCase(m.getTypeMouvement())) {
                insertMouvmentAsLIFOENTREE(m);
            } else {
                insertMouvmentAsLIFOSORTIE(m);
            }
            break;

        default:
            
            insertMouvementAsCUMP(m);
            break;
        }
    }
}