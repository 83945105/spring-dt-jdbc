package com.dt.jdbc.core;

import com.dt.core.data.ParseData;
import com.dt.core.engine.ColumnEngine;
import com.dt.core.engine.SelectEngine;
import com.dt.core.norm.Engine;
import com.dt.jdbc.mapper.PairColumnResultSetExtractor;
import com.dt.jdbc.norm.JdbcEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * Created by 白超 on 2018/7/3.
 */
public class SpringJdbcEngine implements JdbcEngine {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SelectEngine selectEngine = new SelectEngine();

    @Override
    public Map<String, Object> queryByPrimaryKey(Object key, ColumnEngine columnEngine) {
        String sql = this.selectEngine.selectByPrimaryKey(columnEngine);
        return this.jdbcTemplate.queryForMap(sql, key);
    }

    @Override
    public <T> T queryByPrimaryKey(Object key, Class<T> returnType, ColumnEngine columnEngine) {
        String sql = this.selectEngine.selectByPrimaryKey(columnEngine);
        return this.jdbcTemplate.queryForObject(sql, new Object[]{key}, new BeanPropertyRowMapper<>(returnType));

    }

    @Override
    public Map<String, Object> queryOne(Engine engine) {
        ParseData data = this.selectEngine.selectList(engine);
        return this.jdbcTemplate.queryForMap(data.getSql(), data.getArgs().toArray());
    }

    @Override
    public <T> T queryOne(Class<T> returnType, Engine engine) {
        ParseData data = this.selectEngine.selectList(engine);
        return this.jdbcTemplate.queryForObject(data.getSql(), data.getArgs().toArray(), new BeanPropertyRowMapper<>(returnType));
    }

    @Override
    public List<Map<String, Object>> queryForList(Engine engine) {
        ParseData data = this.selectEngine.selectList(engine);
        return this.jdbcTemplate.queryForList(data.getSql(), data.getArgs().toArray());
    }

    @Override
    public <T> List<T> queryForList(Class<T> returnType, Engine engine) {
        ParseData data = this.selectEngine.selectList(engine);
        return this.jdbcTemplate.query(data.getSql(), data.getArgs().toArray(), new BeanPropertyRowMapper<>(returnType));
    }

    @Override
    public int queryCount(Engine engine) {
        ParseData data = this.selectEngine.selectCount(engine);
        return this.jdbcTemplate.queryForObject(data.getSql(), Integer.class, data.getArgs().toArray());
    }

    @Override
    public <K, V> Map<K, V> queryPairColumnInMap(Engine engine) {
        ParseData data = this.selectEngine.selectList(engine);
        return this.jdbcTemplate.query(data.getSql(), data.getArgs().toArray(), new PairColumnResultSetExtractor<>());
    }

    @Override
    public <K, V> Map<K, V> queryPairColumnInMap(int keyIndex, int valueIndex, Engine engine) {
        ParseData data = this.selectEngine.selectList(engine);
        return this.jdbcTemplate.query(data.getSql(), data.getArgs().toArray(), new PairColumnResultSetExtractor<>(keyIndex, valueIndex));
    }

    @Override
    public <K, V> Map<K, V> queryPairColumnInMap(String keyColumnName, String valueColumnName, Engine engine) {
        ParseData data = this.selectEngine.selectList(engine);
        return this.jdbcTemplate.query(data.getSql(), data.getArgs().toArray(), new PairColumnResultSetExtractor<>(keyColumnName, valueColumnName));
    }

    public int insert() {
//        this.jdbcTemplate.
        return 0;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
