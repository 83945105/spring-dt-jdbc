package com.dt.jdbc.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by 白超 on 2018/7/3.
 */
public class PairColumnResultSetExtractor<K, V> implements ResultSetExtractor<Map<K, V>> {

    @Override
    public Map<K, V> extractData(ResultSet rs) throws SQLException, DataAccessException {
        return null;
    }
}
