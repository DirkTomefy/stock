package com.example.stock.dirkfw;

import java.util.HashMap;
import java.util.Vector;

import com.example.stock.dirkfw.annotation.db.IgnoreDbOpperation;
import com.example.stock.dirkfw.annotation.display.IgnoreDisplayOpperation;
import com.example.stock.dirkfw.db.GenericDao;
import com.example.stock.dirkfw.db.start.mapping.TableMap;



public class DirkFwObject {
    @IgnoreDisplayOpperation
    @IgnoreDbOpperation
    public static HashMap<String, TableMap> classInfos;
    
    public static HashMap<String, TableMap> getClassInfos() {
        return classInfos;
    }

    public static void setClassInfos(HashMap<String, TableMap> classInfos) {
        DirkFwObject.classInfos = classInfos;
    }

    @IgnoreDisplayOpperation
    @IgnoreDbOpperation
    GenericDao dao;


    public GenericDao getDao() {
        return dao;
    }

    public void setDao(GenericDao dao) {
        this.dao = dao;
    }

    public DirkFwObject(GenericDao dao) {
        this.dao = dao;
    }

    public void save() throws Exception {
        this.dao.save(this);
    }

    public void update() throws Exception {
        this.dao.update(this);
    }

    public void delete() throws Exception {
            this.dao.delete(this);
    }

    public void findById() throws Exception {
        this.dao.findById(this);
    }

    public Vector<Object> getAll() throws Exception {
        return this.dao.getAll(this);
    }
}
