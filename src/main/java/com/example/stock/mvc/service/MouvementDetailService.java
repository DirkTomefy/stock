package com.example.stock.mvc.service;
import java.util.HashMap;
import java.util.Vector;
import com.example.stock.dirkfw.db.GenericDao;
import com.example.stock.dirkfw.db.util.ComparaisonOperation;
import com.example.stock.mvc.model.DetailFormInput;
import com.example.stock.mvc.model.Mouvement;

public class MouvementDetailService {

     public static Vector<Object> findDetails(DetailFormInput input) throws Exception {
        try (GenericDao dao = new GenericDao()) {
            HashMap<String, ComparaisonOperation> operations = new HashMap<>();
            operations.put("date_mouvement", ComparaisonOperation.INFEQ);

            Mouvement where = new Mouvement();
            where.setArticle(input.getArticle());
            where.setDateMouvement(input.getDate());
            
            Vector<Object> list=dao.findAll(where,operations);
            handleTotalEntreeAll(list);
            return list;
        }
    }

    public static int getTotalPriseForEntree(Mouvement maybeEntree,Vector<Object> maybeSorties){
        int retour=0;
        for (Object object : maybeSorties) {
            Mouvement m = (Mouvement) object;
            if(m.getSource()!=null && m.getTypeMouvement().getSigle().equals("SORTIE")){
                   if(m.getSource().getId().equals(maybeEntree.getId())) retour+=m.getQuantitePrise(); 
            }
         
        }
        return retour;
    }
    public static void handleTotalSortieForEntreeByDate(Mouvement maybeEntree,Vector<Object> maybeSorties){
        if(maybeEntree.getTypeMouvement().getSigle().equalsIgnoreCase("SORTIE")) return;
       
       maybeEntree.setTotalPriseForEntree(getTotalPriseForEntree(maybeEntree, maybeSorties));
    }

    public static void handleTotalEntreeAll(Vector<Object> maybeEntrees){
        for (Object object : maybeEntrees) {
            handleTotalSortieForEntreeByDate((Mouvement) object, maybeEntrees);
        }
    }
}
