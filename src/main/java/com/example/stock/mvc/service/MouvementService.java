package com.example.stock.mvc.service;

import java.util.Vector;

import com.example.stock.dirkfw.db.GenericDao;
import com.example.stock.mvc.model.Mouvement;

public class MouvementService {
    public static Mouvement getLastMouvementInfo() throws Exception {
        try (GenericDao dao = new GenericDao()) {
            dao.setAlterName("last_mouvement");

            Vector<Object> lastMouvements = dao.find(new Mouvement());

            if (lastMouvements.isEmpty()) {
                return Mouvement.defaultMouvement();
            }

            return (Mouvement) lastMouvements.get(0);

        } catch (Exception e) {
            throw e;
        }
    }

    public static void insertMouvementAsCUMP(Object mouvement) throws Exception {

        if (mouvement instanceof Mouvement) {

            Mouvement m = (Mouvement) mouvement;

            // ! initialisation de la valeur du mouvement
            m.setValeur(m.getPu() * m.getQuantite());

            Mouvement lastMouvement = getLastMouvementInfo();

            if (m.getTypeMouvement().equals("ENTREE")) {

                m.setQteStock(
                        lastMouvement.getQteStock() + m.getQuantite());

                m.setMoneyValueStock(
                        lastMouvement.getMoneyValueStock() + m.getValeur());

            } else if (m.getTypeMouvement().equals("SORTIE")) {

                m.setQteStock(
                        lastMouvement.getQteStock() - m.getQuantite());

                m.setMoneyValueStock(
                        lastMouvement.getMoneyValueStock() - m.getValeur());
            }

            m.setCump(
                    m.getMoneyValueStock() / m.getQteStock());
        }
    }

    public static void insertMouvmentAsLIFO(Object mouvement) {

    }

    public static void insertMouvmentAsFIFO(Object mouvement) {

    }
}
