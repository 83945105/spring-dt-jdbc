package com.dt.jdbc.parser;

import java.util.Map;

/**
 * Created by 白超 on 2018/7/9.
 */
public class UpdateOrInsertParser {

    public String updateOrInsert(String tableName, Map<String, String> columnAliasMap, int recordSize) {
        StringBuilder sql = new StringBuilder(64);
        sql.append("insert into ")
                .append(tableName)
                .append(" (");
        int i = 0;
        StringBuilder on = new StringBuilder(64);
        for (Map.Entry<String, String> entry : columnAliasMap.entrySet()) {
            if (i++ == 0) {
                sql.append("`").append(entry.getKey()).append("`");
                on.append("`").append(entry.getKey()).append("` = values(`").append(entry.getKey()).append("`)");
            } else {
                sql.append(",`").append(entry.getKey()).append("`");
                on.append(",`").append(entry.getKey()).append("` = values(`").append(entry.getKey()).append("`)");
            }
        }
        sql.append(") values ");
        StringBuilder values = new StringBuilder(32).append("(");
        for (; i > 0; i--) {
            if (i == 1) {
                values.append("?)");
            } else {
                values.append("?,");
            }
        }
        for (; recordSize > 0; recordSize--) {
            if (recordSize == 1) {
                sql.append(values.toString());
            } else {
                sql.append(values.toString()).append(",");
            }
        }
        return sql.append(" on duplicate key update ").append(on).toString();
    }

}
