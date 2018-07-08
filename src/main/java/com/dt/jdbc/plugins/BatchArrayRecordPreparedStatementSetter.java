package com.dt.jdbc.plugins;

import com.dt.beans.BeanUtils;
import com.dt.beans.ClassMethodAccessCache;
import com.esotericsoftware.reflectasm.MethodAccess;
import org.springframework.jdbc.core.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by 白超 on 2018/7/7.
 */
public class BatchArrayRecordPreparedStatementSetter implements PreparedStatementSetter, ParameterDisposer {

    private Object[] recordArray;

    private Map<String, String> columnAliasMap;

    private ClassMethodAccessCache cache = new ClassMethodAccessCache();

    public BatchArrayRecordPreparedStatementSetter(Object[] recordArray, Map<String, String> columnAliasMap) {
        this.recordArray = recordArray;
        this.columnAliasMap = columnAliasMap;
    }

    @Override
    public void setValues(PreparedStatement ps) throws SQLException {
        if (this.recordArray != null) {
            int i = 0;
            MethodAccess methodAccess;
            for (Object record : this.recordArray) {
                if (record instanceof Map) {
                    for (Map.Entry<String, String> entry : this.columnAliasMap.entrySet()) {
                        doSetValue(ps, i++ + 1, ((Map) record).get(entry.getKey()));
                    }
                } else {
                    methodAccess = this.cache.getMethodAccess(record.getClass());
                    for (Map.Entry<String, String> entry : this.columnAliasMap.entrySet()) {
                        doSetValue(ps, i++ + 1, methodAccess.invoke(record, BeanUtils.getGetterMethodName(entry.getValue(), false)));
                    }
                }
            }
        }
    }

    protected void doSetValue(PreparedStatement ps, int parameterPosition, Object argValue) throws SQLException {
        if (argValue instanceof SqlParameterValue) {
            SqlParameterValue paramValue = (SqlParameterValue) argValue;
            StatementCreatorUtils.setParameterValue(ps, parameterPosition, paramValue, paramValue.getValue());
        } else {
            StatementCreatorUtils.setParameterValue(ps, parameterPosition, SqlTypeValue.TYPE_UNKNOWN, argValue);
        }
    }

    @Override
    public void cleanupParameters() {
        if (this.recordArray != null) {
            this.recordArray = null;
        }
    }

    public void setCache(ClassMethodAccessCache cache) {
        this.cache = cache;
    }
}
