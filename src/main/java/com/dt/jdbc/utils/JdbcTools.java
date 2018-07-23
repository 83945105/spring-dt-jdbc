package com.dt.jdbc.utils;

import org.springframework.jdbc.support.JdbcUtils;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
