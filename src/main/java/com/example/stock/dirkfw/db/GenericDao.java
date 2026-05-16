package com.example.stock.dirkfw.db;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import com.example.stock.context.DatabaseContext;
import com.example.stock.dirkfw.DirkFwConfig;
import com.example.stock.dirkfw.annotation.db.OneToMany;
import com.example.stock.dirkfw.db.query.*;
import com.example.stock.dirkfw.db.util.*;
import com.example.stock.dirkfw.start.mapping.*;

public class GenericDao {
    public DatabaseContext dbctx;

    private void executeWithConnection(ConnectionOperation operation) throws Exception {
        try (Connection conn = dbctx.getConnection()) {
            conn.setAutoCommit(false);
            operation.execute(conn);
            conn.commit();
        }
    }

    private <T> T executeQueryWithConnection(ConnectionQuery<T> query) throws Exception {
        try (Connection conn = dbctx.getConnection()) {
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

        String query = QueryMaker.getQueryForSelectWhereWithOperations(tableMap, e, operations);

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
        String request = QueryMaker.getQueryForGetAll(tableMap);
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

        String query = QueryMaker.getQueryForSelectWhere(tableMap, where);

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
        String request = QueryMaker.getQueryForInsert(tableMap, o);

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
        Class<?> parentClass = parent.getClass();
        TableMap parentMap = getTableMapInfo(parentClass);

        for (Field field : parentClass.getDeclaredFields()) {
            OneToMany relation = field.getAnnotation(OneToMany.class);
            if (relation == null) {
                continue;
            }

            Class<?> childClass = getCollectionGenericType(field);
            if (childClass == null) {
                continue;
            }

            TableMap childMap = getTableMapInfo(childClass);
            if (childMap == null) {
                continue;
            }

            Field mappedByField = childClass.getDeclaredField(relation.mappedBy());
            String foreignKeyColumn = FieldInfo.getTableColumnName(mappedByField);

            Object parentId = parentMap.getIdFieldValue(parent);
            if (parentId == null) {
                setCollectionValue(parent, field, new ArrayList<>());
                continue;
            }

            Vector<Object> children = new Vector<>();
            String query = "SELECT * FROM " + childMap.getTableName() + " WHERE " + foreignKeyColumn + " = ?";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setObject(1, parentId);

                try (ResultSet rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        Object child = childMap.getConstructor().newInstance();
                        childMap.mapResultIntoObject(child, rs);
                        children.add(child);
                    }
                }
            }

            setCollectionValue(parent, field, children);
        }
    }

    private Class<?> getCollectionGenericType(Field field) {
        Type genericType = field.getGenericType();
        if (!(genericType instanceof ParameterizedType)) {
            return null;
        }

        Type[] arguments = ((ParameterizedType) genericType).getActualTypeArguments();
        if (arguments.length == 0) {
            return null;
        }

        Type first = arguments[0];
        if (first instanceof Class<?>) {
            return (Class<?>) first;
        }

        return null;
    }

    private void setCollectionValue(Object parent, Field field, Collection<?> value) throws Exception {
        String fieldName = field.getName();
        String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

        try {
            parent.getClass().getMethod(setterName, field.getType()).invoke(parent, value);
        } catch (NoSuchMethodException ex) {
            field.setAccessible(true);
            field.set(parent, value);
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
                    tableMap.mapResultIntoObject(o, result);
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
        // Save the parent object
        save(o, conn);

        // Save all OneToMany related objects
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
                // Set the parent reference in the child
                info.getChildSetter().invoke(child, o);

                // Save the child
                save(child, conn);
            }
        }
    }

    public void updateWithOneToMany(Object o) throws Exception {
        executeWithConnection(conn -> updateWithOneToMany(o, conn));
    }

    public void updateWithOneToMany(Object o, Connection conn) throws Exception {
        // Update the parent object
        update(o, conn);

        // Update all OneToMany related objects
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
                // Set the parent reference in the child
                info.getChildSetter().invoke(child, o);

                // Update the child
                update(child, conn);
            }
        }
    }

}