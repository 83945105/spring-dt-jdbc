package com.dt.jdbc.norm;

import com.dt.core.engine.ColumnEngine;
import com.dt.core.norm.Engine;

import java.util.List;
import java.util.Map;

/**
 * Created by 白超 on 2018/7/3.
 */
public interface JdbcEngine {

    Map<String, Object> queryByPrimaryKey(Object key, ColumnEngine columnEngine);

    <T> T queryByPrimaryKey(Object key, Class<T> returnType, ColumnEngine columnEngine);

    Map<String, Object> queryOne(Engine engine);

    <T> T queryOne(Class<T> returnType, Engine engine);

    List<Map<String, Object>> queryForList(Engine engine);

    <T> List<T> queryForList(Class<T> returnType, Engine engine);

    int queryCount(Engine engine);

    <K, V> Map<K, V> queryPairColumnInMap(Engine engine);

    <K, V> Map<K, V> queryPairColumnInMap(int keyIndex, int valueIndex, Engine engine);

    <K, V> Map<K, V> queryPairColumnInMap(String keyColumnName, String valueColumnName, Engine engine);

}
