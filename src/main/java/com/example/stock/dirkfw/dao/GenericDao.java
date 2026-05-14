package com.example.stock.dirkfw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Vector;
import com.example.stock.dirkfw.dao.start.mapping.*;
import com.example.stock.dirkfw.dao.start.query.*;
import com.example.stock.context.DatabaseContext;
import com.example.stock.dirkfw.DirkFwObject;
import com.example.stock.dirkfw.dao.reflect.ReflectManager;

public class GenericDao  {
    public DatabaseContext dbctx;

    public void save(Object o) throws Exception {
        try (Connection conn = dbctx.getConnection()) {
            conn.setAutoCommit(false);
            save(o, conn);
            conn.commit();
        }
    }


    public static HashMap<String, TableMap> getClassinfos() {
        return DirkFwObject.classinfos;
    }

    public static TableMap getTableMapInfo(Class<?> clazz) {
        return DirkFwObject.classinfos.get(clazz.getName());
    }

    public static TableMap getTableMapInfo(String clazz) {
        return DirkFwObject.classinfos.get(clazz);
    }

    

    public void update(Object o)
            throws Exception {
        try (Connection conn = dbctx.getConnection()) {
            conn.setAutoCommit(false);
            update(o, conn);
            conn.commit();
        }
    }

    public Vector<Object> getAll(Class<?> clazz)
            throws Exception {
        try (Connection conn = dbctx.getConnection()) {
            return getAll(clazz, conn);
        }
    }

    public Vector<Object> getAll(Object where)
            throws Exception {
        try (Connection conn = dbctx.getConnection()) {
            return getAll(where, conn);
        }
    }

    public void findById(Object e)
            throws Exception {
        try (Connection conn = dbctx.getConnection()) {
            findById(e, conn);
        }
    }

    public Vector<Object> getAll(Class<?> clazz, Connection conn)
            throws Exception {
        Vector<Object> lo = new Vector<>();
        TableMap tableMap = getTableMapInfo(clazz);
        String request = QueryMaker.getQueryForGetAll(tableMap);
        try (PreparedStatement preparedStatement = conn.prepareStatement(request);
                ResultSet result = preparedStatement.executeQuery()) {
            while (result.next()) {
                Object obj = tableMap.getConstructor().newInstance();
                ReflectManager.mapResultIntoObject(obj, result, tableMap);
                lo.add(obj);
            }
        }
        return lo;
    }

    public Vector<Object> getAll(Object where, Connection conn)
            throws Exception {

        Vector<Object> results = new Vector<>();

        Class<?> clazz = where.getClass();
        TableMap tableMap = getTableMapInfo(clazz);

        String query = QueryMaker.getQueryForSelectWhere(tableMap, where);

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {

            QueryFiller.fillWhere(pstmt, tableMap, where);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Object obj = tableMap.getConstructor().newInstance();
                    ReflectManager.mapResultIntoObject(obj, rs, tableMap);
                    results.add(obj);
                }
            }
        }

        return results;
    }

    public void save(Object o, Connection conn)
            throws Exception {

        TableMap tableMap = getTableMapInfo(o.getClass());
        String request = QueryMaker.getQueryForInsert(tableMap, o);

        try (PreparedStatement preparedStatement = conn.prepareStatement(request,
                PreparedStatement.RETURN_GENERATED_KEYS)) {

            QueryFiller.fillpstmt(preparedStatement, tableMap, o);
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Object generatedId = generatedKeys.getObject(1, tableMap.getFieldID().getReflectField().getType());
                    tableMap.getFieldID().setFieldValue(o, generatedId);
                }
            }
        }
    }

    public void delete(Object o, Connection conn) throws Exception {
        TableMap tableMap = getTableMapInfo(o.getClass());
        Object idValue = tableMap.getIdFieldValue(o);
        String request = QueryMaker.getQueryForDelete(tableMap);
        try (PreparedStatement preparedStatement = conn.prepareStatement(request)) {
            preparedStatement.setObject(1, idValue);
            preparedStatement.executeUpdate();
        }
    }

    public void findById(Object o, Connection conn)
            throws Exception {
        TableMap tableMap = getTableMapInfo(o.getClass());
        Object idValue = tableMap.getIdFieldValue(o);
        String request = QueryMaker.getQueryForFindByID(tableMap);

        try (PreparedStatement preparedStatement = conn.prepareStatement(request)) {
            preparedStatement.setObject(1, idValue);
            try (ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    ReflectManager.mapResultIntoObject(o, result, tableMap);
                }
            }
        }
    }

    public void update(Object o, Connection conn)
            throws Exception {
        TableMap tableMap = getTableMapInfo(o.getClass());
        Object idValue = tableMap.getIdFieldValue(o);
        String request = QueryMaker.getQueryForUpdate(tableMap, o);
        try (PreparedStatement preparedStatement = conn.prepareStatement(request)) {
            int i = QueryFiller.fillpstmt(preparedStatement, tableMap, o);
            preparedStatement.setObject(i, idValue);
            preparedStatement.executeUpdate();
        }
    }

    
    public void delete(Object o) throws Exception {
        try (Connection conn = dbctx.getConnection()) {
           delete(o, conn);
        }
    }

}