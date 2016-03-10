package com.panache.dao;


import com.panache.common.StrategyIntf;
import com.panache.common.StrategyListIntf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;


@Service
public class DaoService {

    @Autowired
    DataSource dataSource;

    public Integer executePrepare(String query, Map<String, Object> paramMap) throws Exception {
        try (Connection con = dataSource.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
            int ctr=1;
            for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                preparedStatement.setObject(ctr++, entry.getValue());
            }
            return preparedStatement.executeUpdate();
        }

    }

    public <T> T executePrepare(String query, StrategyIntf<T> strategyIntf) throws Exception {
        try (Connection con = dataSource.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
            return strategyIntf.strategy(preparedStatement);
        }
    }

    public <T> List<T> executePrepare(String query, StrategyListIntf<T> strategyListIntf) throws Exception {
        try (Connection con = dataSource.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {
            System.out.println(query);
            return strategyListIntf.strategy(preparedStatement);
        }
    }

    public <T> T findOneBy(StrategyIntf<T> strategyIntf, String tableName, Map<String, Object> whereConditionKeyValMap,
                           String orderByclause, String... selectCOlName) throws Exception {
        String query = buildSelectQuery(tableName, whereConditionKeyValMap, orderByclause, selectCOlName);
        return executePrepare(query, strategyIntf);
    }

    public Map<String, Object> findOneBy(String tableName, Map<String, Object> whereConditionKeyValMap,
                                         String orderByclause, String... selectCOlName) throws Exception {
        return findOneBy(getRsMapStrategyIntf(whereConditionKeyValMap, selectCOlName), tableName,
                whereConditionKeyValMap, orderByclause, selectCOlName);

    }

    public <T> List<T> findBy(StrategyListIntf<T> strategyListIntf, String tableName,
                              Map<String, Object> whereConditionKeyValMap,
                              String orderByclause, String... selectCOlName) throws Exception {
        String query = buildSelectQuery(tableName, whereConditionKeyValMap, orderByclause, selectCOlName);
        return executePrepare(query, strategyListIntf);
    }

    public List<Map<String, Object>>  findBy(String tableName,
                                            Map<String, Object> whereConditionKeyValMap,
                                            String orderByclause, String... selectCOlName) throws Exception {
        return findBy(getRsListMapStrategyListIntf(whereConditionKeyValMap, selectCOlName), tableName,
                whereConditionKeyValMap, orderByclause, selectCOlName);
    }

    public List<Map<String, Object>> findByQuery(String query,
                                            Map<String, Object> whereConditionKeyValMap, String... selectCOlName) throws Exception {
        return executePrepare(query, getRsListMapStrategyListIntf(whereConditionKeyValMap, selectCOlName));
    }

    public Map<String, Object> findOneByQuery(String query, Map<String, Object> whereConditionKeyValMap
            , String... selectCOlName) throws Exception {
        return executePrepare(query, getRsMapStrategyIntf(whereConditionKeyValMap, selectCOlName));

    }



    public Integer save(String tableName, String [] insertColNames, Object ... insertValues) throws Exception{
        String insQuery = buildInsertQuery(tableName, insertColNames) ;
        return executePrepare(insQuery, getInsertOrUpdateStrategyIntf(insertValues));
    }

    public Integer save(String tableName, Map<String,Object> paramMap) throws Exception{
        String insQuery = buildInsertQuery(tableName, paramMap.keySet()) ;
        return executePrepare(insQuery, paramMap);
    }

    public Integer saveAndGetId(String tableName, Map<String,Object> paramMap) throws Exception{
        String insQuery = buildInsertQuery(tableName, paramMap.keySet()) ;
        return executePrepareAndGetId(insQuery, paramMap);
    }

    public Integer update(String tableName, String [] setColNames, String [] conditionColNames,Object... updateValueParamValues) throws Exception{
        String insQuery = buildUpdateQuery(tableName, setColNames, conditionColNames) ;
        System.out.println(insQuery + " - " + Arrays.deepToString(updateValueParamValues));
        return executePrepare(insQuery, getInsertOrUpdateStrategyIntf(updateValueParamValues));
    }

    private StrategyListIntf<Map<String, Object>> getRsListMapStrategyListIntf(Map<String, Object> whereConditionKeyValMap,
                                                                               String... selectCOlNames) {
        return (PreparedStatement pstmt) -> {
            List<Map<String, Object>> list = new ArrayList<>();
            if (whereConditionKeyValMap != null) {
                int ctr = 1;
                for (Map.Entry<String, Object> entry : whereConditionKeyValMap.entrySet()) {
                    pstmt.setObject(ctr++, entry.getValue());
                }
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> map = new LinkedHashMap<>();
                for (String selectCOlName : selectCOlNames) {
                    if(selectCOlName.contains(" as ")){
                        String [] arr = getAsStringArr(selectCOlName);
                        map.put(arr[1].trim(), rs.getObject(arr[1].trim()));
                    }else{
                        map.put(selectCOlName, rs.getObject(selectCOlName));
                    }
                }
                list.add(map);
            }
            rs.close();
            return list;
        };
    }

    private String [] getAsStringArr(String selectCOlName){
        return selectCOlName.split(" as ");
    }

    private StrategyIntf<Map<String, Object>> getRsMapStrategyIntf(Map<String, Object> whereConditionKeyValMap,
                                                                   String... selectCOlNames) {
        return (PreparedStatement pstmt) -> {
            Map<String, Object> map = new LinkedHashMap<>();
            int ctr = 1;
            if(whereConditionKeyValMap!=null){
                for (Map.Entry<String, Object> entry : whereConditionKeyValMap.entrySet()) {
                    pstmt.setObject(ctr++, entry.getValue());
                }
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                for (String selectCOlName : selectCOlNames) {
                    if(selectCOlName.contains(" as ")){
                        String [] arr = getAsStringArr(selectCOlName);
                        map.put(arr[1].trim(), rs.getObject(arr[1].trim()));
                    }else{
                        map.put(selectCOlName, rs.getObject(selectCOlName));
                    }
                }
            }
            rs.close();
            return map;
        };
    }

    private StrategyIntf<Integer> getInsertOrUpdateStrategyIntf(Object ... insertValues) {
        return (PreparedStatement pstmt) -> {
            int ctr = 1;
            for (Object insertValue : insertValues) {
                pstmt.setObject(ctr++, insertValue);
            }
            return pstmt.executeUpdate();
        };
    }


    private String buildSelectQuery(String tableName, Map<String, Object> whereConditionKeyValMap,
                                    String orderByclause,
                                    String... selectCOlName) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT ");

        if (selectCOlName == null || selectCOlName.length <= 0) {
            query.append(" * ");
        } else {
            for (String colName : selectCOlName) {
                query.append(colName).append(",");
            }
            query.deleteCharAt(query.lastIndexOf(","));
        }
        query.append(" FROM ").append(tableName);

        if (whereConditionKeyValMap != null && whereConditionKeyValMap.size() > 0) {
            query.append(" WHERE ");
            for (String key : whereConditionKeyValMap.keySet()) {
                query.append(" ").append(key).append(" = ?").append(" AND");
            }
            String temp = query.substring(0, query.length() - 3);
            query = new StringBuilder();
            query.append(temp);
        }

        if (orderByclause != null) {
            query.append(orderByclause);
        }

        System.out.println(query);
        return query.toString();
    }

    private String buildInsertQuery(String tableName, String... insertColNames) {
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ").append(tableName).append(" (");
        StringBuilder sb = new StringBuilder();
        for (String insertColName : insertColNames) {
            query.append(insertColName).append(", ");
            sb.append("?,");
        }
        query.deleteCharAt(query.lastIndexOf(","));
        query.append(")");
        sb.deleteCharAt(sb.lastIndexOf(","));
        query.append(" VALUES (").append(sb.toString()).append(")");
        return query.toString();
    }

    private String buildInsertQuery(String tableName, Set<String> insertColNames) {
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ").append(tableName).append(" (");
        StringBuilder sb = new StringBuilder();
        for (String insertColName : insertColNames) {
            query.append(insertColName).append(", ");
            sb.append("?,");
        }
        query.deleteCharAt(query.lastIndexOf(","));
        query.append(")");
        sb.deleteCharAt(sb.lastIndexOf(","));
        query.append(" VALUES (").append(sb.toString()).append(")");
        return query.toString();
    }




    private String buildUpdateQuery(String tableName, String [] setColNames, String ... conditionColNames){
        StringBuilder query = new StringBuilder();
        query.append("UPDATE ").append(tableName).append(" SET ");
        for (String setColName : setColNames) {
            query.append(setColName).append("=?, ");
        }
        query.deleteCharAt(query.lastIndexOf(","));
        query.append("WHERE");
        for (String conditionColName : conditionColNames) {
            query.append(" ").append(conditionColName).append("=? AND");
        }
        String temp = query.substring(0, query.length() - 3);
        query = new StringBuilder();
        query.append(temp);
        return query.toString();
    }

    public Integer executePrepareAndGetId(String query, Map<String, Object> paramMap) throws Exception {
        System.out.println(query);
        try (Connection con = dataSource.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            int ctr = 1;
            for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                preparedStatement.setObject(ctr++, entry.getValue());
            }
            preparedStatement.execute();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            Integer id = null;
            if (rs.next()) {
                id = rs.getInt(1);
            }

            rs.close();
            return id;
        }

    }

}
