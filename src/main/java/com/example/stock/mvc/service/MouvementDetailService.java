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

            return dao.findAll(where, operations);
        }
    }
}
