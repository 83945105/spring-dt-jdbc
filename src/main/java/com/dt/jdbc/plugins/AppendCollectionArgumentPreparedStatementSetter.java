package com.dt.jdbc.plugins;

import org.springframework.jdbc.core.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author 白超
 * @version 1.0
 * @since 2018/7/10
 */
public final class AppendCollectionArgumentPreparedStatementSetter implements PreparedStatementSetter, ParameterDisposer {

    private Collection<Object> args;

    public AppendCollectionArgumentPreparedStatementSetter(Object[] args, Object... appendArgs) {
        this.args = new ArrayList<>();
        for (Object arg : args) {
            this.args.add(arg);
        }
        this.args.addAll(Arrays.asList(appendArgs));
    }

    public AppendCollectionArgumentPreparedStatementSetter(Collection args, Object... appendArgs) {
        this.args = args;
        this.args.addAll(Arrays.asList(appendArgs));
    }

    @Override
    public void setValues(PreparedStatement ps) throws SQLException {
        if (this.args != null) {
            int i = 0;
            Iterator<Object> iterator = this.args.iterator();
            while (iterator.hasNext()) {
                doSetValue(ps, i++ + 1, iterator.next());
            }
        }
    }

    private void doSetValue(PreparedStatement ps, int parameterPosition, Object argValue) throws SQLException {
        if (argValue instanceof SqlParameterValue) {
            SqlParameterValue paramValue = (SqlParameterValue) argValue;
            StatementCreatorUtils.setParameterValue(ps, parameterPosition, paramValue, paramValue.getValue());
        } else {
            StatementCreatorUtils.setParameterValue(ps, parameterPosition, SqlTypeValue.TYPE_UNKNOWN, argValue);
        }
    }

    @Override
    public void cleanupParameters() {
        StatementCreatorUtils.cleanupParameters(this.args);
    }
}
