package com.dt.jdbc.core;

import com.dt.core.data.ParseData;
import com.dt.core.engine.*;
import com.dt.core.norm.Engine;
import com.dt.core.norm.Model;
import com.dt.jdbc.plugins.*;
import com.dt.jdbc.norm.JdbcEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by 白超 on 2018/7/3.
 */
public class SpringJdbcEngine implements JdbcEngine {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SelectEngine selectEngine = new SelectEngine();

    private InsertEngine insertEngine = new InsertEngine();

    private UpdateEngine updateEngine = new UpdateEngine();

    @Override
    public Map<String, Object> queryByPrimaryKey(Object keyValue, ColumnEngine columnEngine) {
        String sql = this.selectEngine.selectByPrimaryKey(columnEngine);
        return this.jdbcTemplate.queryForMap(sql, keyValue);
    }

    @Override
    public <T> T queryByPrimaryKey(Object keyValue, Class<T> returnType, ColumnEngine columnEngine) {
        String sql = this.selectEngine.selectByPrimaryKey(columnEngine);
        return this.jdbcTemplate.queryForObject(sql, new Object[]{keyValue}, new BeanPropertyRowMapper<>(returnType));
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

    private <T extends Model> Model newModel(Class<T> modelClass) {
        Model model = null;
        try {
            model = modelClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return model;
    }

    @Override
    public <T extends Model> int insertArgs(Object[] args, Class<T> modelClass) {
        Model model = newModel(modelClass);
        String sql = this.insertEngine.insert(model.getTableName(), model.getColumnAliasMap());
        return this.jdbcTemplate.update(sql, args);
    }

    @Override
    public <T extends Model> int insertArgs(Object[] args, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        String sql = this.insertEngine.insert(tableName, model.getColumnAliasMap());
        return this.jdbcTemplate.update(sql, args);
    }

    @Override
    public <T extends Model> int insertArgs(Collection<?> args, Class<T> modelClass) {
        Model model = newModel(modelClass);
        String sql = this.insertEngine.insert(model.getTableName(), model.getColumnAliasMap());
        return this.jdbcTemplate.update(sql, new CollectionArgumentPreparedStatementSetter(args));
    }

    @Override
    public <T extends Model> int insertArgs(Collection<?> args, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        String sql = this.insertEngine.insert(tableName, model.getColumnAliasMap());
        return this.jdbcTemplate.update(sql, new CollectionArgumentPreparedStatementSetter(args));
    }

    @Override
    public int insertArgs(Object[] args, ColumnEngine columnEngine) {
        String tableName = columnEngine.getTableName();
        Map<String, String> columnAliasMap = columnEngine.getColumnAliasMap();
        if (columnAliasMap.size() == 0) {
            return this.insertArgs(args, tableName, columnEngine.getTableClass());
        }
        String sql = this.insertEngine.insert(tableName, columnAliasMap);
        return this.jdbcTemplate.update(sql, args);
    }

    @Override
    public int insertArgs(Collection<?> args, ColumnEngine columnEngine) {
        String tableName = columnEngine.getTableName();
        Map<String, String> columnAliasMap = columnEngine.getColumnAliasMap();
        if (columnAliasMap.size() == 0) {
            return this.insertArgs(args, tableName, columnEngine.getTableClass());
        }
        String sql = this.insertEngine.insert(tableName, columnAliasMap);
        return this.jdbcTemplate.update(sql, new CollectionArgumentPreparedStatementSetter(args));
    }

    @Override
    public <T extends Model> int insertRecord(Map<String, ?> record, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData data = this.insertEngine.insertMap(model.getTableName(), model.getColumnAliasMap(), record);
        return this.jdbcTemplate.update(data.getSql(), new CollectionArgumentPreparedStatementSetter(data.getArgs()));
    }

    @Override
    public <T extends Model> int insertRecord(Map<String, ?> record, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData data = this.insertEngine.insertMap(tableName, model.getColumnAliasMap(), record);
        return this.jdbcTemplate.update(data.getSql(), new CollectionArgumentPreparedStatementSetter(data.getArgs()));
    }

    @Override
    public <T extends Model> int insertRecord(Object record, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData data = this.insertEngine.insertObject(model.getTableName(), model.getColumnAliasMap(), record);
        return this.jdbcTemplate.update(data.getSql(), new CollectionArgumentPreparedStatementSetter(data.getArgs()));
    }

    @Override
    public <T extends Model> int insertRecord(Object record, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData data = this.insertEngine.insertObject(tableName, model.getColumnAliasMap(), record);
        return this.jdbcTemplate.update(data.getSql(), new CollectionArgumentPreparedStatementSetter(data.getArgs()));
    }

    @Override
    public int insertRecord(Map<String, ?> record, ColumnEngine columnEngine) {
        String tableName = columnEngine.getTableName();
        Map<String, String> columnAliasMap = columnEngine.getColumnAliasMap();
        if (columnAliasMap.size() == 0) {
            return this.insertRecord(record, tableName, columnEngine.getTableClass());
        }
        ParseData data = this.insertEngine.insertMap(tableName, columnAliasMap, record);
        return this.jdbcTemplate.update(data.getSql(), new CollectionArgumentPreparedStatementSetter(data.getArgs()));
    }

    @Override
    public int insertRecord(Object record, ColumnEngine columnEngine) {
        String tableName = columnEngine.getTableName();
        Map<String, String> columnAliasMap = columnEngine.getColumnAliasMap();
        if (columnAliasMap.size() == 0) {
            return this.insertRecord(record, tableName, columnEngine.getTableClass());
        }
        ParseData data = this.insertEngine.insertObject(tableName, columnAliasMap, record);
        return this.jdbcTemplate.update(data.getSql(), new CollectionArgumentPreparedStatementSetter(data.getArgs()));
    }

    @Override
    public <T extends Model> int insertRecordSelective(Map<String, ?> record, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData data = this.insertEngine.insertMapSelective(model.getTableName(), model.getColumnAliasMap(), record);
        return this.jdbcTemplate.update(data.getSql(), new CollectionArgumentPreparedStatementSetter(data.getArgs()));
    }

    @Override
    public <T extends Model> int insertRecordSelective(Map<String, ?> record, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData data = this.insertEngine.insertMapSelective(tableName, model.getColumnAliasMap(), record);
        return this.jdbcTemplate.update(data.getSql(), new CollectionArgumentPreparedStatementSetter(data.getArgs()));
    }

    @Override
    public <T extends Model> int insertRecordSelective(Object record, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData data = this.insertEngine.insertObjectSelective(model.getTableName(), model.getColumnAliasMap(), record);
        return this.jdbcTemplate.update(data.getSql(), new CollectionArgumentPreparedStatementSetter(data.getArgs()));
    }

    @Override
    public <T extends Model> int insertRecordSelective(Object record, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData data = this.insertEngine.insertObjectSelective(tableName, model.getColumnAliasMap(), record);
        return this.jdbcTemplate.update(data.getSql(), new CollectionArgumentPreparedStatementSetter(data.getArgs()));
    }

    @Override
    public int insertRecordSelective(Map<String, ?> record, ColumnEngine columnEngine) {
        String tableName = columnEngine.getTableName();
        Map<String, String> columnAliasMap = columnEngine.getColumnAliasMap();
        if (columnAliasMap.size() == 0) {
            return this.insertRecordSelective(record, tableName, columnEngine.getTableClass());
        }
        ParseData data = this.insertEngine.insertMapSelective(tableName, columnAliasMap, record);
        return this.jdbcTemplate.update(data.getSql(), new CollectionArgumentPreparedStatementSetter(data.getArgs()));
    }

    @Override
    public int insertRecordSelective(Object record, ColumnEngine columnEngine) {
        String tableName = columnEngine.getTableName();
        Map<String, String> columnAliasMap = columnEngine.getColumnAliasMap();
        if (columnAliasMap.size() == 0) {
            return this.insertRecordSelective(record, tableName, columnEngine.getTableClass());
        }
        ParseData data = this.insertEngine.insertObjectSelective(tableName, columnAliasMap, record);
        return this.jdbcTemplate.update(data.getSql(), new CollectionArgumentPreparedStatementSetter(data.getArgs()));
    }

    @Override
    public <T extends Model> int batchInsertArgs(Object[] batchArgs, Class<T> modelClass) {
        Model model = newModel(modelClass);
        Map<String, String> columnAliasMap = model.getColumnAliasMap();
        String sql = this.insertEngine.batchInsert(model.getTableName(), columnAliasMap, batchArgs.length);
        return this.jdbcTemplate.update(sql, new BatchArgumentPreparedStatementSetter(batchArgs, columnAliasMap.size()));
    }

    @Override
    public <T extends Model> int batchInsertArgs(Object[] batchArgs, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        Map<String, String> columnAliasMap = model.getColumnAliasMap();
        String sql = this.insertEngine.batchInsert(tableName, columnAliasMap, batchArgs.length);
        return this.jdbcTemplate.update(sql, new BatchArgumentPreparedStatementSetter(batchArgs, columnAliasMap.size()));
    }

    @Override
    public <T extends Model> int batchInsertArgs(Collection<?> batchArgs, Class<T> modelClass) {
        Model model = newModel(modelClass);
        Map<String, String> columnAliasMap = model.getColumnAliasMap();
        String sql = this.insertEngine.batchInsert(model.getTableName(), columnAliasMap, batchArgs.size());
        return this.jdbcTemplate.update(sql, new BatchArgumentPreparedStatementSetter(batchArgs, columnAliasMap.size()));
    }

    @Override
    public <T extends Model> int batchInsertArgs(Collection<?> batchArgs, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        Map<String, String> columnAliasMap = model.getColumnAliasMap();
        String sql = this.insertEngine.batchInsert(tableName, columnAliasMap, batchArgs.size());
        return this.jdbcTemplate.update(sql, new BatchArgumentPreparedStatementSetter(batchArgs, columnAliasMap.size()));
    }

    @Override
    public int batchInsertArgs(Object[] batchArgs, ColumnEngine columnEngine) {
        String tableName = columnEngine.getTableName();
        Map<String, String> columnAliasMap = columnEngine.getColumnAliasMap();
        if (columnAliasMap.size() == 0) {
            return this.batchInsertArgs(batchArgs, tableName, columnEngine.getTableClass());
        }
        String sql = this.insertEngine.batchInsert(tableName, columnAliasMap, batchArgs.length);
        return this.jdbcTemplate.update(sql, new BatchArgumentPreparedStatementSetter(batchArgs, columnAliasMap.size()));
    }

    @Override
    public int batchInsertArgs(Collection<?> batchArgs, ColumnEngine columnEngine) {
        String tableName = columnEngine.getTableName();
        Map<String, String> columnAliasMap = columnEngine.getColumnAliasMap();
        if (columnAliasMap.size() == 0) {
            return this.batchInsertArgs(batchArgs, tableName, columnEngine.getTableClass());
        }
        String sql = this.insertEngine.batchInsert(tableName, columnAliasMap, batchArgs.size());
        return this.jdbcTemplate.update(sql, new BatchArgumentPreparedStatementSetter(batchArgs, columnAliasMap.size()));
    }

    @Override
    public <T extends Model> int batchInsertRecords(Object[] records, Class<T> modelClass) {
        Model model = newModel(modelClass);
        Map<String, String> columnAliasMap = model.getColumnAliasMap();
        String sql = this.insertEngine.batchInsert(model.getTableName(), columnAliasMap, records.length);
        return this.jdbcTemplate.update(sql, new BatchArrayRecordPreparedStatementSetter(records, columnAliasMap));
    }

    @Override
    public <T extends Model> int batchInsertRecords(Object[] records, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        Map<String, String> columnAliasMap = model.getColumnAliasMap();
        String sql = this.insertEngine.batchInsert(tableName, columnAliasMap, records.length);
        return this.jdbcTemplate.update(sql, new BatchArrayRecordPreparedStatementSetter(records, columnAliasMap));
    }

    @Override
    public <T extends Model> int batchInsertRecords(Collection<?> records, Class<T> modelClass) {
        Model model = newModel(modelClass);
        Map<String, String> columnAliasMap = model.getColumnAliasMap();
        String sql = this.insertEngine.batchInsert(model.getTableName(), columnAliasMap, records.size());
        return this.jdbcTemplate.update(sql, new BatchCollectionRecordPreparedStatementSetter(records, columnAliasMap));
    }

    @Override
    public <T extends Model> int batchInsertRecords(Collection<?> records, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        Map<String, String> columnAliasMap = model.getColumnAliasMap();
        String sql = this.insertEngine.batchInsert(tableName, columnAliasMap, records.size());
        return this.jdbcTemplate.update(sql, new BatchCollectionRecordPreparedStatementSetter(records, columnAliasMap));
    }

    @Override
    public int batchInsertRecords(Object[] records, ColumnEngine columnEngine) {
        String tableName = columnEngine.getTableName();
        Map<String, String> columnAliasMap = columnEngine.getColumnAliasMap();
        if (columnAliasMap.size() == 0) {
            return this.batchInsertRecords(records, tableName, columnEngine.getTableClass());
        }
        String sql = this.insertEngine.batchInsert(tableName, columnAliasMap, records.length);
        return this.jdbcTemplate.update(sql, new BatchArrayRecordPreparedStatementSetter(records, columnAliasMap));
    }

    @Override
    public int batchInsertRecords(Collection<?> records, ColumnEngine columnEngine) {
        String tableName = columnEngine.getTableName();
        Map<String, String> columnAliasMap = columnEngine.getColumnAliasMap();
        if (columnAliasMap.size() == 0) {
            return this.batchInsertRecords(records, tableName, columnEngine.getTableClass());
        }
        String sql = this.insertEngine.batchInsert(tableName, columnAliasMap, records.size());
        return this.jdbcTemplate.update(sql, new BatchCollectionRecordPreparedStatementSetter(records, columnAliasMap));
    }

    @Override
    public <T extends Model> int updateArgsByPrimaryKey(Object keyValue, Object[] args, Class<T> modelClass) {
        Model model = newModel(modelClass);
        String sql = this.updateEngine.updateByPrimaryKey(model.getTableName(), model.getPrimaryKeyName(), model.getColumnAliasMap());
        return this.jdbcTemplate.update(sql, new AppendCollectionArgumentPreparedStatementSetter(args, keyValue));
    }

    @Override
    public <T extends Model> int updateArgsByPrimaryKey(Object keyValue, Object[] args, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        String sql = this.updateEngine.updateByPrimaryKey(tableName, model.getPrimaryKeyName(), model.getColumnAliasMap());
        return this.jdbcTemplate.update(sql, new AppendCollectionArgumentPreparedStatementSetter(args, keyValue));
    }

    @Override
    public <T extends Model> int updateArgsByPrimaryKey(Object keyValue, Collection<?> args, Class<T> modelClass) {
        Model model = newModel(modelClass);
        String sql = this.updateEngine.updateByPrimaryKey(model.getTableName(), model.getPrimaryKeyName(), model.getColumnAliasMap());
        return this.jdbcTemplate.update(sql, new AppendCollectionArgumentPreparedStatementSetter(args, keyValue));
    }

    @Override
    public <T extends Model> int updateArgsByPrimaryKey(Object keyValue, Collection<?> args, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        String sql = this.updateEngine.updateByPrimaryKey(tableName, model.getPrimaryKeyName(), model.getColumnAliasMap());
        return this.jdbcTemplate.update(sql, new AppendCollectionArgumentPreparedStatementSetter(args, keyValue));
    }

    @Override
    public int updateArgsByPrimaryKey(Object keyValue, Object[] args, ColumnEngine columnEngine) {
        String tableName = columnEngine.getTableName();
        Map<String, String> columnAliasMap = columnEngine.getColumnAliasMap();
        if (columnAliasMap.size() == 0) {
            return this.updateArgsByPrimaryKey(keyValue, args, tableName, columnEngine.getTableClass());
        }
        String sql = this.updateEngine.updateByPrimaryKey(tableName, columnEngine.getPrimaryKeyName(), columnAliasMap);
        return this.jdbcTemplate.update(sql, new AppendCollectionArgumentPreparedStatementSetter(args, keyValue));
    }

    @Override
    public int updateArgsByPrimaryKey(Object keyValue, Collection<?> args, ColumnEngine columnEngine) {
        String tableName = columnEngine.getTableName();
        Map<String, String> columnAliasMap = columnEngine.getColumnAliasMap();
        if (columnAliasMap.size() == 0) {
            return this.updateArgsByPrimaryKey(keyValue, args, tableName, columnEngine.getTableClass());
        }
        String sql = this.updateEngine.updateByPrimaryKey(tableName, columnEngine.getPrimaryKeyName(), columnAliasMap);
        return this.jdbcTemplate.update(sql, new AppendCollectionArgumentPreparedStatementSetter(args, keyValue));
    }

    @Override
    public <T extends Model> int updateRecordByPrimaryKey(Object keyValue, Map<String, ?> record, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData parseData = this.updateEngine.updateMapByPrimaryKey(model.getTableName(),
                model.getPrimaryKeyName(), keyValue, model.getColumnAliasMap(), record);
        return this.jdbcTemplate.update(parseData.getSql(), new CollectionArgumentPreparedStatementSetter(parseData.getArgs()));
    }

    @Override
    public <T extends Model> int updateRecordByPrimaryKey(Object keyValue, Object record, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData parseData = this.updateEngine.updateObjectByPrimaryKey(model.getTableName(),
                model.getPrimaryKeyName(), keyValue, model.getColumnAliasMap(), record);
        return this.jdbcTemplate.update(parseData.getSql(), new CollectionArgumentPreparedStatementSetter(parseData.getArgs()));
    }

    @Override
    public <T extends Model> int updateRecordByPrimaryKey(Object keyValue, Map<String, ?> record, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData parseData = this.updateEngine.updateMapByPrimaryKey(tableName,
                model.getPrimaryKeyName(), keyValue, model.getColumnAliasMap(), record);
        return this.jdbcTemplate.update(parseData.getSql(), new CollectionArgumentPreparedStatementSetter(parseData.getArgs()));
    }

    @Override
    public <T extends Model> int updateRecordByPrimaryKey(Object keyValue, Object record, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData parseData = this.updateEngine.updateObjectByPrimaryKey(tableName,
                model.getPrimaryKeyName(), keyValue, model.getColumnAliasMap(), record);
        return this.jdbcTemplate.update(parseData.getSql(), new CollectionArgumentPreparedStatementSetter(parseData.getArgs()));
    }

    @Override
    public int updateRecordByPrimaryKey(Object keyValue, Map<String, ?> record, ColumnEngine columnEngine) {
        String tableName = columnEngine.getTableName();
        Map<String, String> columnAliasMap = columnEngine.getColumnAliasMap();
        if (columnAliasMap.size() == 0) {
            return this.updateRecordByPrimaryKey(keyValue, record, tableName, columnEngine.getTableClass());
        }
        ParseData parseData = this.updateEngine.updateMapByPrimaryKey(tableName,
                columnEngine.getPrimaryKeyName(), keyValue, columnAliasMap, record);
        return this.jdbcTemplate.update(parseData.getSql(), new CollectionArgumentPreparedStatementSetter(parseData.getArgs()));
    }

    @Override
    public int updateRecordByPrimaryKey(Object keyValue, Object record, ColumnEngine columnEngine) {
        String tableName = columnEngine.getTableName();
        Map<String, String> columnAliasMap = columnEngine.getColumnAliasMap();
        if (columnAliasMap.size() == 0) {
            return this.updateRecordByPrimaryKey(keyValue, record, tableName, columnEngine.getTableClass());
        }
        ParseData parseData = this.updateEngine.updateObjectByPrimaryKey(tableName,
                columnEngine.getPrimaryKeyName(), keyValue, columnAliasMap, record);
        return this.jdbcTemplate.update(parseData.getSql(), new CollectionArgumentPreparedStatementSetter(parseData.getArgs()));
    }

    @Override
    public <T extends Model> int updateRecordByPrimaryKeySelective(Object keyValue, Map<String, ?> record, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData parseData = this.updateEngine.updateMapByPrimaryKeySelective(model.getTableName(),
                model.getPrimaryKeyName(), keyValue, model.getColumnAliasMap(), record);
        return this.jdbcTemplate.update(parseData.getSql(), new CollectionArgumentPreparedStatementSetter(parseData.getArgs()));
    }

    @Override
    public <T extends Model> int updateRecordByPrimaryKeySelective(Object keyValue, Map<String, ?> record, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData parseData = this.updateEngine.updateMapByPrimaryKeySelective(tableName,
                model.getPrimaryKeyName(), keyValue, model.getColumnAliasMap(), record);
        return this.jdbcTemplate.update(parseData.getSql(), new CollectionArgumentPreparedStatementSetter(parseData.getArgs()));
    }

    @Override
    public <T extends Model> int updateRecordByPrimaryKeySelective(Object keyValue, Object record, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData parseData = this.updateEngine.updateObjectByPrimaryKeySelective(model.getTableName(),
                model.getPrimaryKeyName(), keyValue, model.getColumnAliasMap(), record);
        return this.jdbcTemplate.update(parseData.getSql(), new CollectionArgumentPreparedStatementSetter(parseData.getArgs()));
    }

    @Override
    public <T extends Model> int updateRecordByPrimaryKeySelective(Object keyValue, Object record, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData parseData = this.updateEngine.updateObjectByPrimaryKeySelective(tableName,
                model.getPrimaryKeyName(), keyValue, model.getColumnAliasMap(), record);
        return this.jdbcTemplate.update(parseData.getSql(), new CollectionArgumentPreparedStatementSetter(parseData.getArgs()));
    }

    @Override
    public int updateRecordByPrimaryKeySelective(Object keyValue, Map<String, ?> record, ColumnEngine columnEngine) {
        return 0;
    }

    @Override
    public int updateRecordByPrimaryKeySelective(Object keyValue, Object record, ColumnEngine columnEngine) {
        return 0;
    }

    @Override
    public int updateRecord(Map<String, ?> record, WhereEngine whereEngine) {
        return 0;
    }

    @Override
    public int updateRecord(Object record, WhereEngine whereEngine) {
        return 0;
    }

    @Override
    public int updateRecordSelective(Map<String, ?> record, WhereEngine whereEngine) {
        return 0;
    }

    @Override
    public int updateRecordSelective(Object record, WhereEngine whereEngine) {
        return 0;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
