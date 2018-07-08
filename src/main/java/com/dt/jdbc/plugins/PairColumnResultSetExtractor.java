package com.dt.jdbc.plugins;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by 白超 on 2018/7/3.
 */
public class PairColumnResultSetExtractor<K, V> implements ResultSetExtractor<Map<K, V>> {

    //0 => index mode 1 => name mode
    private int mode = 0;

    private int keyIndex = 1;

    private int valueIndex = 2;

    private String keyColumnName;

    private String valueColumnName;

    public PairColumnResultSetExtractor() {
    }

    public PairColumnResultSetExtractor(int keyIndex, int valueIndex) {
        this.keyIndex = keyIndex;
        this.valueIndex = valueIndex;
    }

    public PairColumnResultSetExtractor(String keyColumnName, String valueColumnName) {
        this.keyColumnName = keyColumnName;
        this.valueColumnName = valueColumnName;
        this.mode = 1;
    }

    @Override
    public Map<K, V> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<K, V> result = new LinkedHashMap<>();
        Object key;
        Object value;
        if (mode == 0) {
            while (rs.next()) {
                key = null;
                value = null;
                ResultSetMetaData rsd = rs.getMetaData();
                int columnCount = rsd.getColumnCount();
                if (this.keyIndex <= columnCount) {
                    key = this.getColumnValue(rs, this.keyIndex);
                }
                if (this.valueIndex <= columnCount) {
                    value = this.getColumnValue(rs, this.valueIndex);
                }
                result.put((K) key, (V) value);
            }
        } else if (mode == 1) {
            String name;
            while (rs.next()) {
                key = null;
                value = null;
                ResultSetMetaData rsd = rs.getMetaData();
                int columnCount = rsd.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    name = getColumnKey(JdbcUtils.lookupColumnName(rsd, i));
                    if (name.equals(keyColumnName)) {
                        key = getColumnValue(rs, i);
                    }
                    if (name.equals(valueColumnName)) {
                        value = getColumnValue(rs, i);
                    }
                }
                result.put((K) key, (V) value);
            }
        }
        return result;
    }

    private String getColumnKey(String columnName) {
        return columnName;
    }

    private Object getColumnValue(ResultSet rs, int index) throws SQLException {
        return JdbcUtils.getResultSetValue(rs, index);
    }
}
