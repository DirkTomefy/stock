package com.example.stock.mvc.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Vector;
import com.example.stock.dirkfw.db.GenericDao;
import com.example.stock.dirkfw.db.util.ComparaisonOperation;
import com.example.stock.mvc.model.Article;
import com.example.stock.mvc.model.Mouvement;
import com.example.stock.mvc.model.TypeMouvement;

public class MouvementService {

   
       public static Mouvement getLastMouvementInfo(Article article) throws Exception {
            return getLastMouvementInfo(article, null);
        }

        public static Mouvement getLastMouvementInfo(Article article, LocalDateTime date) throws Exception {

            try (GenericDao dao = new GenericDao()) {

                Mouvement where = new Mouvement();
                where.setArticle(article);
                where.setDateMouvement(date);

                HashMap<String, ComparaisonOperation> operations = new HashMap<>();
                if (date != null) {
                    operations.put("date_mouvement", ComparaisonOperation.INFEQ);
                }

                Vector<Object> res = operations.isEmpty() ? dao.find(where) : dao.findAll(where, operations);

                if (res.isEmpty()) {
                    return Mouvement.defaultMouvement();
                }

                res.sort(Comparator.comparing(MouvementService::movementDateForSort)
                        .thenComparing(m -> ((Mouvement) m).getId() == null ? 0 : ((Mouvement) m).getId()));

                return (Mouvement) res.lastElement();
            }
        }

        private static LocalDateTime movementDateForSort(Object obj) {
            Mouvement m = (Mouvement) obj;
            return m.getDateMouvement() == null ? LocalDateTime.MIN : m.getDateMouvement();
        }

    
    private static void processEntreeForLifoFifo(Mouvement m, Mouvement last) {

        m.setTypeMouvement(new TypeMouvement("ENTREE"));

        m.setValeur(calcValeur(m));

        m.setQteStock(last.getQteStock() + m.getQuantite());

        m.setMoneyValueStock(last.getMoneyValueStock() + m.getValeur());
        
        m.setQuantitePrise(0);
        m.setTotalPriseForEntree(0);

        m.setSource(null);
    }

    public static void makeDefaultValByLast(Mouvement m, Mouvement last){
         if (!m.getTypeMouvement().getSigle().equals("ENTREE")){
            m.setPu(last.getCump());
            m.setQteStock(last.getQteStock() - m.getQuantite());
            m.setValeur(m.getPu()*m.getQuantite());
            m.setMoneyValueStock(m.getQteStock()*last.getCump());
            m.setCump(last.getCump());
        }
    }
    
    public static void insertMouvementAsCUMP(Object mouvement,Mouvement last) throws Exception {

        if (!(mouvement instanceof Mouvement)) return;
        Mouvement m = (Mouvement) mouvement;

        

        m.setValeur(calcValeur(m));

        if (m.getTypeMouvement().getSigle().equals("ENTREE")) {

            m.setQteStock(last.getQteStock() + m.getQuantite());
            m.setMoneyValueStock(last.getMoneyValueStock() + m.getValeur());
            m.setCump(m.getMoneyValueStock() / m.getQteStock());
            
        } else {
            m.setPu(last.getCump());
            m.setQteStock(last.getQteStock() - m.getQuantite());
            m.setValeur(m.getPu()*m.getQuantite());
            m.setMoneyValueStock(m.getQteStock()*last.getCump());
            m.setCump(last.getCump());
        }

        
        try (GenericDao dao = new GenericDao()) {
            dao.save(m);
        }
    }

    
    public static void insertMouvmentAsFIFOENTREE(Object mouvement) throws Exception {

        if (!(mouvement instanceof Mouvement)) return;
        Mouvement m = (Mouvement) mouvement;

        Mouvement last = getLastMouvementInfo(m.getArticle(), m.getDateMouvement());

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

        Mouvement last = getLastMouvementInfo(m.getArticle(), m.getDateMouvement());

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
        Mouvement last = getLastMouvementInfo(sortie.getArticle(), sortie.getDateMouvement());
        double stockRestantTotal = last.getQteStock() == null ? 0.0 : last.getQteStock();
        double moneyRestantTotal = last.getMoneyValueStock() == null ? 0.0 : last.getMoneyValueStock();
        
        try (java.sql.Connection conn = com.example.stock.context.DatabaseContext.createNewConnection()) {
            try {
                conn.setAutoCommit(false);

                try (GenericDao readDao = new GenericDao(); GenericDao writeDao = new GenericDao()) {
                    readDao.setAlterName(view);

                    Mouvement where = new Mouvement();
                    where.setArticle(sortie.getArticle());
                    where.setDateMouvement(sortie.getDateMouvement());

                    HashMap<String, com.example.stock.dirkfw.db.util.ComparaisonOperation> ops = new HashMap<>();
                    ops.put("date_mouvement", com.example.stock.dirkfw.db.util.ComparaisonOperation.INFEQ);

                    Vector<Object> entrees = readDao.findAll(where, ops, conn);
                    entrees.sort((a, b) -> {
                        Mouvement m1 = (Mouvement) a;
                        Mouvement m2 = (Mouvement) b;

                        int dateCompare = (m1.getDateMouvement() == null ? LocalDateTime.MIN : m1.getDateMouvement())
                                .compareTo(m2.getDateMouvement() == null ? LocalDateTime.MIN : m2.getDateMouvement());
                        if (dateCompare == 0) {
                            int id1 = m1.getId() == null ? 0 : m1.getId();
                            int id2 = m2.getId() == null ? 0 : m2.getId();
                            return "mouvement_lifo".equals(view) ? Integer.compare(id2, id1) : Integer.compare(id1, id2);
                        }
                        return "mouvement_lifo".equals(view) ? -dateCompare : dateCompare;
                    });

                    for (Object obj : entrees) {

                        if (reste <= 0) break;

                        Mouvement entree = (Mouvement) obj;

                        int dejaPrise = entree.getTotalPriseForEntree() == null ? 0 : entree.getTotalPriseForEntree();
                        double disponible = entree.getQuantite() - dejaPrise;

                        if (disponible <= 0) continue;

                        int prise = (int) Math.min(reste, disponible);

                        Mouvement m = buildSortie(sortie, entree, prise);

                        reste -= prise;
                        stockRestantTotal -= prise;
                        moneyRestantTotal -= prise * m.getPu();

                        int nouvellePrise = dejaPrise + prise;
                        entree.setTotalPriseForEntree(nouvellePrise);
                        
                        writeDao.update(entree, conn);

                        m.setQteStock(stockRestantTotal);
                        m.setMoneyValueStock(moneyRestantTotal);

                        writeDao.save(m, conn);
                    }
                }

                conn.commit();
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            }
        }
    }

   
    private  static Mouvement buildSortie(Mouvement sortie, Mouvement entree, int prise) {

        Mouvement m = new Mouvement();

        m.setArticle(sortie.getArticle());
        m.setTypeMouvement(new TypeMouvement("SORTIE"));
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

    
    public static  void insertMouvement(Object mouvement) throws Exception {

        
        if (!(mouvement instanceof Mouvement)) return;
        Mouvement m = (Mouvement) mouvement;
        Mouvement last = getLastMouvementInfo(m.getArticle(), m.getDateMouvement());
        makeDefaultValByLast(m, last);
        if (m.getArticle() != null && m.getArticle().getId() == null) {
            try (GenericDao dao = new GenericDao()) {
                Article where = new Article();
                where.setLibelle(m.getArticle().getLibelle());
                Vector<Object> found = dao.find(where);
                if (found != null && !found.isEmpty()) {
                    Article real = (Article) found.get(0);
                    m.setArticle(real);
                }
            }
        }

       
        m.setQuantitePrise(0);
        m.setTotalPriseForEntree(0);
        m.setValeur(m.getPu()*m.getQuantite());

        if (m.getArticle() == null || m.getArticle().getMethodGestionStock() == null
                || m.getArticle().getMethodGestionStock().getSigle() == null) {
            insertMouvementAsCUMP(m,last);
            return;
        }

        String sigle = m.getArticle().getMethodGestionStock().getSigle();
        String typeSigle = m.getTypeMouvement() != null ? m.getTypeMouvement().getSigle() : null;

        switch (sigle) {
        case "CUMP":
            insertMouvementAsCUMP(m,last);
            break;

        case "FIFO":
            if ("ENTREE".equalsIgnoreCase(typeSigle)) {
                insertMouvmentAsFIFOENTREE(m);
            } else {
                insertMouvmentAsFIFOSORTIE(m);
            }
            break;

        case "LIFO":
            if ("ENTREE".equalsIgnoreCase(typeSigle)) {
                insertMouvmentAsLIFOENTREE(m);
            } else {
                insertMouvmentAsLIFOSORTIE(m);
            }
            break;

        default:
            
            insertMouvementAsCUMP(m,last);
            break;
        }
    }
}