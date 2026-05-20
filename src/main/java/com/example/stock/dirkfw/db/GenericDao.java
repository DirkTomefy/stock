package com.example.stock.dirkfw.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import com.example.stock.context.DatabaseContext;
import com.example.stock.dirkfw.DirkFwConfig;
import com.example.stock.dirkfw.db.query.*;
import com.example.stock.dirkfw.db.util.*;
import com.example.stock.dirkfw.start.mapping.*;

public class GenericDao implements AutoCloseable {
    private String alterName;
    public String getAlterName() {
        return alterName;
    }

    public void setAlterName(String alterName) {
        this.alterName = alterName;
    }

    private String getQueryTableName(TableMap tableMap) {
        return alterName != null ? alterName : tableMap.getTableName();
    }

    private void executeWithConnection(ConnectionOperation operation) throws Exception {
      
            try (Connection conn = DatabaseContext.createNewConnection()) {
                conn.setAutoCommit(false);
                operation.execute(conn);
                conn.commit();
            }
        
    }

    private <T> T executeQueryWithConnection(ConnectionQuery<T> query) throws Exception {
       
        try (Connection conn = DatabaseContext.createNewConnection()) {
            return query.execute(conn);
        
    }
    }

    public void save(Object o) throws Exception {
        executeWithConnection(conn -> save(o, conn));
    }

    public static TableMap getTableMapInfo(Class<?> clazz) {
        return DirkFwConfig.classInfos.get(clazz.getName());
    }

    public static TableMap getTableMapInfo(String clazz) {
        return DirkFwConfig.classInfos.get(clazz);
    }

    public void update(Object o)
            throws Exception {
        executeWithConnection(conn -> update(o, conn));
    }

    public Vector<Object> getAll(Class<?> clazz)
            throws Exception {
        return executeQueryWithConnection(conn -> getAll(clazz, conn));
    }

    public Vector<Object> getAll(Object where)
            throws Exception {
        return executeQueryWithConnection(conn -> getAll(where, conn));
    }

    public Vector<Object> find(Object where)
            throws Exception {
        return getAll(where);
    }

    public Vector<Object> findAll(Object e, HashMap<String, ComparaisonOperation> operations)
            throws Exception {
        return executeQueryWithConnection(conn -> findAll(e, operations, conn));
    }

    public Vector<Object> findAll(Object e, HashMap<String, ComparaisonOperation> operations, Connection conn)
            throws Exception {

        Vector<Object> results = new Vector<>();

        Class<?> clazz = e.getClass();
        TableMap tableMap = getTableMapInfo(clazz);

        String query = QueryMaker.getQueryForSelectWhereWithOperations(tableMap, getQueryTableName(tableMap), e, operations);

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {

            QueryFiller.fillWhereWithOperations(pstmt, tableMap, e, operations);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Object obj = tableMap.getConstructor().newInstance();
                    tableMap.mapResultIntoObject(obj, rs);
                    hydrateOneToManyRelations(obj, conn);
                    results.add(obj);
                }
            }
        }

        return results;
    }

    public void findById(Object e)
            throws Exception {
        executeQueryWithConnection(conn -> {
            findById(e, conn);
            return null;
        });
    }

    public Vector<Object> getAll(Class<?> clazz, Connection conn)
            throws Exception {
        Vector<Object> lo = new Vector<>();
        TableMap tableMap = getTableMapInfo(clazz);
        String request = QueryMaker.getQueryForGetAll(tableMap, getQueryTableName(tableMap));
        try (PreparedStatement preparedStatement = conn.prepareStatement(request);
                ResultSet result = preparedStatement.executeQuery()) {
            while (result.next()) {
                Object obj = tableMap.getConstructor().newInstance();
                tableMap.mapResultIntoObject(obj, result);
                hydrateOneToManyRelations(obj, conn);
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

        String query = QueryMaker.getQueryForSelectWhere(tableMap, getQueryTableName(tableMap), where);

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {

            QueryFiller.fillWhere(pstmt, tableMap, where);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Object obj = tableMap.getConstructor().newInstance();
                    tableMap.mapResultIntoObject(obj, rs);
                    hydrateOneToManyRelations(obj, conn);
                    results.add(obj);
                }
            }
        }

        return results;
    }

    public void save(Object o, Connection conn)
            throws Exception {

        TableMap tableMap = getTableMapInfo(o.getClass());
        String request = QueryMaker.getQueryForInsert(tableMap, getQueryTableName(tableMap), o);

        try (PreparedStatement preparedStatement = conn.prepareStatement(request,
                PreparedStatement.RETURN_GENERATED_KEYS)) {

            QueryFiller.fillpstmtForInsert(preparedStatement, tableMap, o, 1);
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Object generatedId = generatedKeys.getObject(1, tableMap.getFieldID().getReflectField().getType());
                    tableMap.getFieldID().setFieldValue(o, generatedId);
                }
            }
        }
    }

    private void hydrateOneToManyRelations(Object parent, Connection conn) throws Exception {
        TableMap parentMap = getTableMapInfo(parent.getClass());

        for (OneToManyInfo info : parentMap.getOneToManyInfos()) {
            TableMap childMap = getTableMapInfo(info.getChildClass());
            if (childMap == null) {
                continue;
            }
            
            String foreignKeyColumn = FieldInfo.getTableColumnName(info.getMappedByField());

            Object parentId = parentMap.getIdFieldValue(parent);
            if (parentId == null) {
                info.getSetter().invoke(parent, new ArrayList<>());
                continue;
            }

            Vector<Object> children = new Vector<>();
            String query = QueryMaker.getQueryForHydrateOneToMany(parentMap, childMap, foreignKeyColumn) ;

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setObject(1, parentId);

                try (ResultSet rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        Object child = info.getChildConstructor().newInstance();
                        childMap.mapResultIntoObject(child, rs);
                        children.add(child);
                    }
                }
            }
            info.getSetter().invoke(parent, children);
        }
    }

    public void delete(Object o, Connection conn) throws Exception {
        TableMap tableMap = getTableMapInfo(o.getClass());
        Object idValue = tableMap.getIdFieldValue(o);
        String request = QueryMaker.getQueryForDelete(tableMap, getQueryTableName(tableMap));
        try (PreparedStatement preparedStatement = conn.prepareStatement(request)) {
            preparedStatement.setObject(1, idValue);
            preparedStatement.executeUpdate();
        }
    }

    public void findById(Object o, Connection conn)
            throws Exception {
        TableMap tableMap = getTableMapInfo(o.getClass());
        Object idValue = tableMap.getIdFieldValue(o);
        String request = QueryMaker.getQueryForFindByID(tableMap, getQueryTableName(tableMap));

        try (PreparedStatement preparedStatement = conn.prepareStatement(request)) {
            preparedStatement.setObject(1, idValue);
            try (ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    tableMap.mapResultIntoObject(o, result);
                }
            }
        }
    }

    public void update(Object o, Connection conn)
            throws Exception {
        TableMap tableMap = getTableMapInfo(o.getClass());
        Object idValue = tableMap.getIdFieldValue(o);
        String request = QueryMaker.getQueryForUpdate(tableMap, getQueryTableName(tableMap), o);
        try (PreparedStatement preparedStatement = conn.prepareStatement(request)) {
            int i = QueryFiller.fillpstmtForUpdates(preparedStatement, tableMap, o);
            preparedStatement.setObject(i, idValue);
            preparedStatement.executeUpdate();
        }
    }

    public void delete(Object o) throws Exception {
        executeQueryWithConnection(conn -> {
            delete(o, conn);
            return null;
        });
    }

    public void saveWithOneToMany(Object o) throws Exception {
        executeWithConnection(conn -> saveWithOneToMany(o, conn));
    }

    public void saveWithOneToMany(Object o, Connection conn) throws Exception {
        save(o, conn);
        TableMap parentMap = getTableMapInfo(o.getClass());
        OneToManyInfo[] oneToManyInfos = parentMap.getOneToManyInfos();

        if (oneToManyInfos == null || oneToManyInfos.length == 0) {
            return;
        }

        for (OneToManyInfo info : oneToManyInfos) {
            Collection<?> childCollection = (Collection<?>) info.getGetter().invoke(o);
            if (childCollection == null || childCollection.isEmpty()) {
                continue;
            }

            for (Object child : childCollection) {
                info.getChildSetter().invoke(child, o);
                save(child, conn);
            }
        }
    }

    public void updateWithOneToMany(Object o) throws Exception {
        executeWithConnection(conn -> updateWithOneToMany(o, conn));
    }

    public void updateWithOneToMany(Object o, Connection conn) throws Exception {
        
        update(o, conn);
        TableMap parentMap = getTableMapInfo(o.getClass());
        OneToManyInfo[] oneToManyInfos = parentMap.getOneToManyInfos();

        if (oneToManyInfos == null || oneToManyInfos.length == 0) {
            return;
        }

        for (OneToManyInfo info : oneToManyInfos) {
            Collection<?> childCollection = (Collection<?>) info.getGetter().invoke(o);
            if (childCollection == null || childCollection.isEmpty()) {
                continue;
            }
            for (Object child : childCollection) {
                info.getChildSetter().invoke(child, o);
                update(child, conn);
            }
        }
    }

    @Override
    public void close() throws Exception {
        //ICI
    }

}