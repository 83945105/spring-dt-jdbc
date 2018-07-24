package com.dt.jdbc.utils;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.CollectionUtils;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Created by 白超 on 2018/7/23.
 */
public class JdbcTools {

    public static String getColumnKey(String columnName) {
        return columnName;
    }

    public static Object getColumnValue(ResultSet rs, int index) throws SQLException {
        Object value = JdbcUtils.getResultSetValue(rs, index);
        if (value instanceof BigInteger) {
            value = Long.valueOf(value.toString());
        }
        return value;
    }

    public static <T> T nullableSingleResult(Collection<T> results) throws IncorrectResultSizeDataAccessException {
        if (CollectionUtils.isEmpty(results)) {
            return null;
        }
        if (results.size() > 1) {
            throw new IncorrectResultSizeDataAccessException(1, results.size());
        }
        return results.iterator().next();
    }
}
