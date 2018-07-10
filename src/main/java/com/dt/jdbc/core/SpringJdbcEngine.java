package com.dt.jdbc.core;

import com.dt.core.data.ParseData;
import com.dt.core.engine.*;
import com.dt.core.norm.Engine;
import com.dt.core.norm.Model;
import com.dt.jdbc.parser.*;
import com.dt.jdbc.plugins.*;
import com.dt.jdbc.norm.JdbcEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * SpringJdbc引擎
 *
 * @author 白超
 * @version 1.0
 * @since 2018/7/10
 */
public class SpringJdbcEngine implements JdbcEngine {

    @Autowired
    @SuppressWarnings("all")
    private JdbcTemplate jdbcTemplate;

    private QueryParser queryParser = new QueryParser();

    private InsertParser insertParser = new InsertParser();

    private UpdateParser updateParser = new UpdateParser();

    private UpdateOrInsertParser updateOrInsertParser = new UpdateOrInsertParser();

    private DeleteParser deleteParser = new DeleteParser();

    @Override
    public Map<String, Object> queryByPrimaryKey(Object keyValue, ColumnEngine columnEngine) {
        String sql = this.queryParser.selectByPrimaryKey(columnEngine);
        return this.jdbcTemplate.queryForMap(sql, keyValue);
    }

    @Override
    public <T> T queryByPrimaryKey(Object keyValue, Class<T> returnType, ColumnEngine columnEngine) {
        String sql = this.queryParser.selectByPrimaryKey(columnEngine);
        return this.jdbcTemplate.queryForObject(sql, new Object[]{keyValue}, new BeanPropertyRowMapper<>(returnType));
    }

    @Override
    public Map<String, Object> queryOne(Engine engine) {
        ParseData data = this.queryParser.selectList(engine);
        return this.jdbcTemplate.queryForMap(data.getSql(), data.getArgs().toArray());
    }

    @Override
    public <T> T queryOne(Class<T> returnType, Engine engine) {
        ParseData data = this.queryParser.selectList(engine);
        return this.jdbcTemplate.queryForObject(data.getSql(), data.getArgs().toArray(), new BeanPropertyRowMapper<>(returnType));
    }

    @Override
    public List<Map<String, Object>> queryForList(Engine engine) {
        ParseData data = this.queryParser.selectList(engine);
        return this.jdbcTemplate.queryForList(data.getSql(), data.getArgs().toArray());
    }

    @Override
    public <T> List<T> queryForList(Class<T> returnType, Engine engine) {
        ParseData data = this.queryParser.selectList(engine);
        return this.jdbcTemplate.query(data.getSql(), data.getArgs().toArray(), new BeanPropertyRowMapper<>(returnType));
    }

    @Override
    public int queryCount(Engine engine) {
        ParseData data = this.queryParser.selectCount(engine);
        return this.jdbcTemplate.queryForObject(data.getSql(), Integer.class, data.getArgs().toArray());
    }

    @Override
    public <K, V> Map<K, V> queryPairColumnInMap(Engine engine) {
        ParseData data = this.queryParser.selectList(engine);
        return this.jdbcTemplate.query(data.getSql(), data.getArgs().toArray(), new PairColumnResultSetExtractor<>());
    }

    @Override
    public <K, V> Map<K, V> queryPairColumnInMap(int keyIndex, int valueIndex, Engine engine) {
        ParseData data = this.queryParser.selectList(engine);
        return this.jdbcTemplate.query(data.getSql(), data.getArgs().toArray(), new PairColumnResultSetExtractor<>(keyIndex, valueIndex));
    }

    @Override
    public <K, V> Map<K, V> queryPairColumnInMap(String keyColumnName, String valueColumnName, Engine engine) {
        ParseData data = this.queryParser.selectList(engine);
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
    @SuppressWarnings("unchecked")
    public <T extends Model> int insertArgs(Object[] args, Class<T> modelClass) {
        Model model = newModel(modelClass);
        String sql = this.insertParser.insert(model.getTableName(), model.getColumnAliasMap());
        return this.jdbcTemplate.update(sql, args);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int insertArgs(Object[] args, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        String sql = this.insertParser.insert(tableName, model.getColumnAliasMap());
        return this.jdbcTemplate.update(sql, args);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int insertArgs(Collection<?> args, Class<T> modelClass) {
        Model model = newModel(modelClass);
        String sql = this.insertParser.insert(model.getTableName(), model.getColumnAliasMap());
        return this.jdbcTemplate.update(sql, new CollectionArgumentPreparedStatementSetter(args));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int insertArgs(Collection<?> args, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        String sql = this.insertParser.insert(tableName, model.getColumnAliasMap());
        return this.jdbcTemplate.update(sql, new CollectionArgumentPreparedStatementSetter(args));
    }

    @Override
    @SuppressWarnings("unchecked")
    public int insertArgs(Object[] args, ColumnEngine columnEngine) {
        Map<String, String> columnAliasMap = columnEngine.getColumnAliasMap();
        if (columnAliasMap.size() == 0) {
            columnAliasMap = columnEngine.getTable().getColumnAliasMap();
        }
        String sql = this.insertParser.insert(columnEngine.getTableName(), columnAliasMap);
        return this.jdbcTemplate.update(sql, args);
    }

    @Override
    @SuppressWarnings("unchecked")
    public int insertArgs(Collection<?> args, ColumnEngine columnEngine) {
        Map<String, String> columnAliasMap = columnEngine.getColumnAliasMap();
        if (columnAliasMap.size() == 0) {
            columnAliasMap = columnEngine.getTable().getColumnAliasMap();
        }
        String sql = this.insertParser.insert(columnEngine.getTableName(), columnAliasMap);
        return this.jdbcTemplate.update(sql, new CollectionArgumentPreparedStatementSetter(args));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int insertRecord(Map<String, ?> record, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData data = this.insertParser.insertMap(model.getTableName(), model.getColumnAliasMap(), record);
        return this.jdbcTemplate.update(data.getSql(), new CollectionArgumentPreparedStatementSetter(data.getArgs()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int insertRecord(Map<String, ?> record, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData data = this.insertParser.insertMap(tableName, model.getColumnAliasMap(), record);
        return this.jdbcTemplate.update(data.getSql(), new CollectionArgumentPreparedStatementSetter(data.getArgs()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int insertRecord(Object record, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData data = this.insertParser.insertObject(model.getTableName(), model.getColumnAliasMap(), record);
        return this.jdbcTemplate.update(data.getSql(), new CollectionArgumentPreparedStatementSetter(data.getArgs()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int insertRecord(Object record, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData data = this.insertParser.insertObject(tableName, model.getColumnAliasMap(), record);
        return this.jdbcTemplate.update(data.getSql(), new CollectionArgumentPreparedStatementSetter(data.getArgs()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public int insertRecord(Map<String, ?> record, ColumnEngine columnEngine) {
        Map<String, String> columnAliasMap = columnEngine.getColumnAliasMap();
        if (columnAliasMap.size() == 0) {
            columnAliasMap = columnEngine.getTable().getColumnAliasMap();
        }
        ParseData data = this.insertParser.insertMap(columnEngine.getTableName(), columnAliasMap, record);
        return this.jdbcTemplate.update(data.getSql(), new CollectionArgumentPreparedStatementSetter(data.getArgs()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public int insertRecord(Object record, ColumnEngine columnEngine) {
        Map<String, String> columnAliasMap = columnEngine.getColumnAliasMap();
        if (columnAliasMap.size() == 0) {
            columnAliasMap = columnEngine.getTable().getColumnAliasMap();
        }
        ParseData data = this.insertParser.insertObject(columnEngine.getTableName(), columnAliasMap, record);
        return this.jdbcTemplate.update(data.getSql(), new CollectionArgumentPreparedStatementSetter(data.getArgs()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int insertRecordSelective(Map<String, ?> record, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData data = this.insertParser.insertMapSelective(model.getTableName(), model.getColumnAliasMap(), record);
        return this.jdbcTemplate.update(data.getSql(), new CollectionArgumentPreparedStatementSetter(data.getArgs()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int insertRecordSelective(Map<String, ?> record, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData data = this.insertParser.insertMapSelective(tableName, model.getColumnAliasMap(), record);
        return this.jdbcTemplate.update(data.getSql(), new CollectionArgumentPreparedStatementSetter(data.getArgs()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int insertRecordSelective(Object record, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData data = this.insertParser.insertObjectSelective(model.getTableName(), model.getColumnAliasMap(), record);
        return this.jdbcTemplate.update(data.getSql(), new CollectionArgumentPreparedStatementSetter(data.getArgs()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int insertRecordSelective(Object record, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData data = this.insertParser.insertObjectSelective(tableName, model.getColumnAliasMap(), record);
        return this.jdbcTemplate.update(data.getSql(), new CollectionArgumentPreparedStatementSetter(data.getArgs()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public int insertRecordSelective(Map<String, ?> record, ColumnEngine columnEngine) {
        Map<String, String> columnAliasMap = columnEngine.getColumnAliasMap();
        if (columnAliasMap.size() == 0) {
            columnAliasMap = columnEngine.getTable().getColumnAliasMap();
        }
        ParseData data = this.insertParser.insertMapSelective(columnEngine.getTableName(), columnAliasMap, record);
        return this.jdbcTemplate.update(data.getSql(), new CollectionArgumentPreparedStatementSetter(data.getArgs()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public int insertRecordSelective(Object record, ColumnEngine columnEngine) {
        Map<String, String> columnAliasMap = columnEngine.getColumnAliasMap();
        if (columnAliasMap.size() == 0) {
            columnAliasMap = columnEngine.getTable().getColumnAliasMap();
        }
        ParseData data = this.insertParser.insertObjectSelective(columnEngine.getTableName(), columnAliasMap, record);
        return this.jdbcTemplate.update(data.getSql(), new CollectionArgumentPreparedStatementSetter(data.getArgs()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int batchInsertArgs(Object[] batchArgs, Class<T> modelClass) {
        Model model = newModel(modelClass);
        Map<String, String> columnAliasMap = model.getColumnAliasMap();
        String sql = this.insertParser.batchInsert(model.getTableName(), columnAliasMap, batchArgs.length);
        return this.jdbcTemplate.update(sql, new BatchArgumentPreparedStatementSetter(batchArgs, columnAliasMap.size()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int batchInsertArgs(Object[] batchArgs, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        Map<String, String> columnAliasMap = model.getColumnAliasMap();
        String sql = this.insertParser.batchInsert(tableName, columnAliasMap, batchArgs.length);
        return this.jdbcTemplate.update(sql, new BatchArgumentPreparedStatementSetter(batchArgs, columnAliasMap.size()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int batchInsertArgs(Collection<?> batchArgs, Class<T> modelClass) {
        Model model = newModel(modelClass);
        Map<String, String> columnAliasMap = model.getColumnAliasMap();
        String sql = this.insertParser.batchInsert(model.getTableName(), columnAliasMap, batchArgs.size());
        return this.jdbcTemplate.update(sql, new BatchArgumentPreparedStatementSetter(batchArgs, columnAliasMap.size()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int batchInsertArgs(Collection<?> batchArgs, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        Map<String, String> columnAliasMap = model.getColumnAliasMap();
        String sql = this.insertParser.batchInsert(tableName, columnAliasMap, batchArgs.size());
        return this.jdbcTemplate.update(sql, new BatchArgumentPreparedStatementSetter(batchArgs, columnAliasMap.size()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public int batchInsertArgs(Object[] batchArgs, ColumnEngine columnEngine) {
        Map<String, String> columnAliasMap = columnEngine.getColumnAliasMap();
        if (columnAliasMap.size() == 0) {
            columnAliasMap = columnEngine.getTable().getColumnAliasMap();
        }
        String sql = this.insertParser.batchInsert(columnEngine.getTableName(), columnAliasMap, batchArgs.length);
        return this.jdbcTemplate.update(sql, new BatchArgumentPreparedStatementSetter(batchArgs, columnAliasMap.size()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public int batchInsertArgs(Collection<?> batchArgs, ColumnEngine columnEngine) {
        Map<String, String> columnAliasMap = columnEngine.getColumnAliasMap();
        if (columnAliasMap.size() == 0) {
            columnAliasMap = columnEngine.getTable().getColumnAliasMap();
        }
        String sql = this.insertParser.batchInsert(columnEngine.getTableName(), columnAliasMap, batchArgs.size());
        return this.jdbcTemplate.update(sql, new BatchArgumentPreparedStatementSetter(batchArgs, columnAliasMap.size()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int batchInsertRecords(Object[] records, Class<T> modelClass) {
        Model model = newModel(modelClass);
        Map<String, String> columnAliasMap = model.getColumnAliasMap();
        String sql = this.insertParser.batchInsert(model.getTableName(), columnAliasMap, records.length);
        return this.jdbcTemplate.update(sql, new BatchArrayRecordPreparedStatementSetter(records, columnAliasMap));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int batchInsertRecords(Object[] records, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        Map<String, String> columnAliasMap = model.getColumnAliasMap();
        String sql = this.insertParser.batchInsert(tableName, columnAliasMap, records.length);
        return this.jdbcTemplate.update(sql, new BatchArrayRecordPreparedStatementSetter(records, columnAliasMap));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int batchInsertRecords(Collection<?> records, Class<T> modelClass) {
        Model model = newModel(modelClass);
        Map<String, String> columnAliasMap = model.getColumnAliasMap();
        String sql = this.insertParser.batchInsert(model.getTableName(), columnAliasMap, records.size());
        return this.jdbcTemplate.update(sql, new BatchCollectionRecordPreparedStatementSetter(records, columnAliasMap));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int batchInsertRecords(Collection<?> records, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        Map<String, String> columnAliasMap = model.getColumnAliasMap();
        String sql = this.insertParser.batchInsert(tableName, columnAliasMap, records.size());
        return this.jdbcTemplate.update(sql, new BatchCollectionRecordPreparedStatementSetter(records, columnAliasMap));
    }

    @Override
    @SuppressWarnings("unchecked")
    public int batchInsertRecords(Object[] records, ColumnEngine columnEngine) {
        Map<String, String> columnAliasMap = columnEngine.getColumnAliasMap();
        if (columnAliasMap.size() == 0) {
            columnAliasMap = columnEngine.getTable().getColumnAliasMap();
        }
        String sql = this.insertParser.batchInsert(columnEngine.getTableName(), columnAliasMap, records.length);
        return this.jdbcTemplate.update(sql, new BatchArrayRecordPreparedStatementSetter(records, columnAliasMap));
    }

    @Override
    @SuppressWarnings("unchecked")
    public int batchInsertRecords(Collection<?> records, ColumnEngine columnEngine) {
        Map<String, String> columnAliasMap = columnEngine.getColumnAliasMap();
        if (columnAliasMap.size() == 0) {
            columnAliasMap = columnEngine.getTable().getColumnAliasMap();
        }
        String sql = this.insertParser.batchInsert(columnEngine.getTableName(), columnAliasMap, records.size());
        return this.jdbcTemplate.update(sql, new BatchCollectionRecordPreparedStatementSetter(records, columnAliasMap));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int updateArgsByPrimaryKey(Object keyValue, Object[] args, Class<T> modelClass) {
        Model model = newModel(modelClass);
        String sql = this.updateParser.updateByPrimaryKey(model.getTableName(), model.getPrimaryKeyName(), model.getColumnAliasMap());
        return this.jdbcTemplate.update(sql, new AppendCollectionArgumentPreparedStatementSetter(args, keyValue));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int updateArgsByPrimaryKey(Object keyValue, Object[] args, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        String sql = this.updateParser.updateByPrimaryKey(tableName, model.getPrimaryKeyName(), model.getColumnAliasMap());
        return this.jdbcTemplate.update(sql, new AppendCollectionArgumentPreparedStatementSetter(args, keyValue));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int updateArgsByPrimaryKey(Object keyValue, Collection<?> args, Class<T> modelClass) {
        Model model = newModel(modelClass);
        String sql = this.updateParser.updateByPrimaryKey(model.getTableName(), model.getPrimaryKeyName(), model.getColumnAliasMap());
        return this.jdbcTemplate.update(sql, new AppendCollectionArgumentPreparedStatementSetter(args, keyValue));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int updateArgsByPrimaryKey(Object keyValue, Collection<?> args, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        String sql = this.updateParser.updateByPrimaryKey(tableName, model.getPrimaryKeyName(), model.getColumnAliasMap());
        return this.jdbcTemplate.update(sql, new AppendCollectionArgumentPreparedStatementSetter(args, keyValue));
    }

    @Override
    @SuppressWarnings("unchecked")
    public int updateArgsByPrimaryKey(Object keyValue, Object[] args, ColumnEngine columnEngine) {
        Map<String, String> columnAliasMap = columnEngine.getColumnAliasMap();
        if (columnAliasMap.size() == 0) {
            columnAliasMap = columnEngine.getTable().getColumnAliasMap();
        }
        String sql = this.updateParser.updateByPrimaryKey(columnEngine.getTableName(), columnEngine.getPrimaryKeyName(), columnAliasMap);
        return this.jdbcTemplate.update(sql, new AppendCollectionArgumentPreparedStatementSetter(args, keyValue));
    }

    @Override
    @SuppressWarnings("unchecked")
    public int updateArgsByPrimaryKey(Object keyValue, Collection<?> args, ColumnEngine columnEngine) {
        Map<String, String> columnAliasMap = columnEngine.getColumnAliasMap();
        if (columnAliasMap.size() == 0) {
            columnAliasMap = columnEngine.getTable().getColumnAliasMap();
        }
        String sql = this.updateParser.updateByPrimaryKey(columnEngine.getTableName(), columnEngine.getPrimaryKeyName(), columnAliasMap);
        return this.jdbcTemplate.update(sql, new AppendCollectionArgumentPreparedStatementSetter(args, keyValue));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int updateRecordByPrimaryKey(Object keyValue, Map<String, ?> record, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData parseData = this.updateParser.updateMapByPrimaryKey(model.getTableName(),
                model.getPrimaryKeyName(), keyValue, model.getColumnAliasMap(), record);
        return this.jdbcTemplate.update(parseData.getSql(), new CollectionArgumentPreparedStatementSetter(parseData.getArgs()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int updateRecordByPrimaryKey(Object keyValue, Object record, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData parseData = this.updateParser.updateObjectByPrimaryKey(model.getTableName(),
                model.getPrimaryKeyName(), keyValue, model.getColumnAliasMap(), record);
        return this.jdbcTemplate.update(parseData.getSql(), new CollectionArgumentPreparedStatementSetter(parseData.getArgs()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int updateRecordByPrimaryKey(Object keyValue, Map<String, ?> record, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData parseData = this.updateParser.updateMapByPrimaryKey(tableName,
                model.getPrimaryKeyName(), keyValue, model.getColumnAliasMap(), record);
        return this.jdbcTemplate.update(parseData.getSql(), new CollectionArgumentPreparedStatementSetter(parseData.getArgs()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int updateRecordByPrimaryKey(Object keyValue, Object record, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData parseData = this.updateParser.updateObjectByPrimaryKey(tableName,
                model.getPrimaryKeyName(), keyValue, model.getColumnAliasMap(), record);
        return this.jdbcTemplate.update(parseData.getSql(), new CollectionArgumentPreparedStatementSetter(parseData.getArgs()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public int updateRecordByPrimaryKey(Object keyValue, Map<String, ?> record, ColumnEngine columnEngine) {
        Map<String, String> columnAliasMap = columnEngine.getColumnAliasMap();
        if (columnAliasMap.size() == 0) {
            columnAliasMap = columnEngine.getTable().getColumnAliasMap();
        }
        ParseData parseData = this.updateParser.updateMapByPrimaryKey(columnEngine.getTableName(),
                columnEngine.getPrimaryKeyName(), keyValue, columnAliasMap, record);
        return this.jdbcTemplate.update(parseData.getSql(), new CollectionArgumentPreparedStatementSetter(parseData.getArgs()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public int updateRecordByPrimaryKey(Object keyValue, Object record, ColumnEngine columnEngine) {
        Map<String, String> columnAliasMap = columnEngine.getColumnAliasMap();
        if (columnAliasMap.size() == 0) {
            columnAliasMap = columnEngine.getTable().getColumnAliasMap();
        }
        ParseData parseData = this.updateParser.updateObjectByPrimaryKey(columnEngine.getTableName(),
                columnEngine.getPrimaryKeyName(), keyValue, columnAliasMap, record);
        return this.jdbcTemplate.update(parseData.getSql(), new CollectionArgumentPreparedStatementSetter(parseData.getArgs()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int updateRecordByPrimaryKeySelective(Object keyValue, Map<String, ?> record, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData parseData = this.updateParser.updateMapByPrimaryKeySelective(model.getTableName(),
                model.getPrimaryKeyName(), keyValue, model.getColumnAliasMap(), record);
        return this.jdbcTemplate.update(parseData.getSql(), new CollectionArgumentPreparedStatementSetter(parseData.getArgs()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int updateRecordByPrimaryKeySelective(Object keyValue, Map<String, ?> record, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData parseData = this.updateParser.updateMapByPrimaryKeySelective(tableName,
                model.getPrimaryKeyName(), keyValue, model.getColumnAliasMap(), record);
        return this.jdbcTemplate.update(parseData.getSql(), new CollectionArgumentPreparedStatementSetter(parseData.getArgs()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int updateRecordByPrimaryKeySelective(Object keyValue, Object record, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData parseData = this.updateParser.updateObjectByPrimaryKeySelective(model.getTableName(),
                model.getPrimaryKeyName(), keyValue, model.getColumnAliasMap(), record);
        return this.jdbcTemplate.update(parseData.getSql(), new CollectionArgumentPreparedStatementSetter(parseData.getArgs()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int updateRecordByPrimaryKeySelective(Object keyValue, Object record, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData parseData = this.updateParser.updateObjectByPrimaryKeySelective(tableName,
                model.getPrimaryKeyName(), keyValue, model.getColumnAliasMap(), record);
        return this.jdbcTemplate.update(parseData.getSql(), new CollectionArgumentPreparedStatementSetter(parseData.getArgs()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public int updateRecordByPrimaryKeySelective(Object keyValue, Map<String, ?> record, ColumnEngine columnEngine) {
        Map<String, String> columnAliasMap = columnEngine.getColumnAliasMap();
        if (columnAliasMap.size() == 0) {
            columnAliasMap = columnEngine.getTable().getColumnAliasMap();
        }
        ParseData parseData = this.updateParser.updateMapByPrimaryKeySelective(columnEngine.getTableName(),
                columnEngine.getPrimaryKeyName(), keyValue, columnAliasMap, record);
        return this.jdbcTemplate.update(parseData.getSql(), new CollectionArgumentPreparedStatementSetter(parseData.getArgs()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public int updateRecordByPrimaryKeySelective(Object keyValue, Object record, ColumnEngine columnEngine) {
        Map<String, String> columnAliasMap = columnEngine.getColumnAliasMap();
        if (columnAliasMap.size() == 0) {
            columnAliasMap = columnEngine.getTable().getColumnAliasMap();
        }
        ParseData parseData = this.updateParser.updateObjectByPrimaryKeySelective(columnEngine.getTableName(),
                columnEngine.getPrimaryKeyName(), keyValue, columnAliasMap, record);
        return this.jdbcTemplate.update(parseData.getSql(), new CollectionArgumentPreparedStatementSetter(parseData.getArgs()));
    }

    @Override
    public int updateRecord(Map<String, ?> record, WhereEngine whereEngine) {
        ParseData parseData = this.updateParser.updateMap(record, whereEngine);
        return this.jdbcTemplate.update(parseData.getSql(), new CollectionArgumentPreparedStatementSetter(parseData.getArgs()));
    }

    @Override
    public int updateRecord(Object record, WhereEngine whereEngine) {
        ParseData parseData = this.updateParser.updateObject(record, whereEngine);
        return this.jdbcTemplate.update(parseData.getSql(), new CollectionArgumentPreparedStatementSetter(parseData.getArgs()));
    }

    @Override
    public int updateRecordSelective(Map<String, ?> record, WhereEngine whereEngine) {
        ParseData parseData = this.updateParser.updateMapSelective(record, whereEngine);
        return this.jdbcTemplate.update(parseData.getSql(), new CollectionArgumentPreparedStatementSetter(parseData.getArgs()));
    }

    @Override
    public int updateRecordSelective(Object record, WhereEngine whereEngine) {
        ParseData parseData = this.updateParser.updateObjectSelective(record, whereEngine);
        return this.jdbcTemplate.update(parseData.getSql(), new CollectionArgumentPreparedStatementSetter(parseData.getArgs()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int batchUpdateRecordsByPrimaryKeys(Object[] records, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData parseData = this.updateParser.batchUpdateByPrimaryKeys(model.getTableName(),
                model.getPrimaryKeyName(), model.getPrimaryKeyAlias(), model.getColumnAliasMap(), records);
        return this.jdbcTemplate.update(parseData.getSql(), new CollectionArgumentPreparedStatementSetter(parseData.getArgs()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int batchUpdateRecordsByPrimaryKeys(Object[] records, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData parseData = this.updateParser.batchUpdateByPrimaryKeys(tableName,
                model.getPrimaryKeyName(), model.getPrimaryKeyAlias(), model.getColumnAliasMap(), records);
        return this.jdbcTemplate.update(parseData.getSql(), new CollectionArgumentPreparedStatementSetter(parseData.getArgs()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int batchUpdateRecordsByPrimaryKeys(Collection<?> records, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData parseData = this.updateParser.batchUpdateByPrimaryKeys(model.getTableName(),
                model.getPrimaryKeyName(), model.getPrimaryKeyAlias(), model.getColumnAliasMap(), records);
        return this.jdbcTemplate.update(parseData.getSql(), new CollectionArgumentPreparedStatementSetter(parseData.getArgs()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int batchUpdateRecordsByPrimaryKeys(Collection<?> records, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        ParseData parseData = this.updateParser.batchUpdateByPrimaryKeys(tableName,
                model.getPrimaryKeyName(), model.getPrimaryKeyAlias(), model.getColumnAliasMap(), records);
        return this.jdbcTemplate.update(parseData.getSql(), new CollectionArgumentPreparedStatementSetter(parseData.getArgs()));
    }

    @Override
    public int batchUpdateRecordsByPrimaryKeys(Object[] records, WhereEngine whereEngine) {
        ParseData parseData = this.updateParser.batchUpdateByPrimaryKeys(records, whereEngine);
        return this.jdbcTemplate.update(parseData.getSql(), new CollectionArgumentPreparedStatementSetter(parseData.getArgs()));
    }

    @Override
    public int batchUpdateRecordsByPrimaryKeys(Collection<?> records, WhereEngine whereEngine) {
        ParseData parseData = this.updateParser.batchUpdateByPrimaryKeys(records, whereEngine);
        return this.jdbcTemplate.update(parseData.getSql(), new CollectionArgumentPreparedStatementSetter(parseData.getArgs()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int updateOrInsertArgs(Object[] batchArgs, Class<T> modelClass) {
        Model model = newModel(modelClass);
        Map<String, String> columnAliasMap = model.getColumnAliasMap();
        String sql = this.updateOrInsertParser.updateOrInsert(model.getTableName(), columnAliasMap, batchArgs.length);
        return this.jdbcTemplate.update(sql, new BatchArgumentPreparedStatementSetter(batchArgs, columnAliasMap.size()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int updateOrInsertArgs(Object[] batchArgs, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        Map<String, String> columnAliasMap = model.getColumnAliasMap();
        String sql = this.updateOrInsertParser.updateOrInsert(tableName, columnAliasMap, batchArgs.length);
        return this.jdbcTemplate.update(sql, new BatchArgumentPreparedStatementSetter(batchArgs, columnAliasMap.size()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int updateOrInsertArgs(Collection<?> batchArgs, Class<T> modelClass) {
        Model model = newModel(modelClass);
        Map<String, String> columnAliasMap = model.getColumnAliasMap();
        String sql = this.updateOrInsertParser.updateOrInsert(model.getTableName(), columnAliasMap, batchArgs.size());
        return this.jdbcTemplate.update(sql, new BatchArgumentPreparedStatementSetter(batchArgs, columnAliasMap.size()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int updateOrInsertArgs(Collection<?> batchArgs, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        Map<String, String> columnAliasMap = model.getColumnAliasMap();
        String sql = this.updateOrInsertParser.updateOrInsert(tableName, columnAliasMap, batchArgs.size());
        return this.jdbcTemplate.update(sql, new BatchArgumentPreparedStatementSetter(batchArgs, columnAliasMap.size()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public int updateOrInsertArgs(Object[] batchArgs, ColumnEngine columnEngine) {
        Map<String, String> columnAliasMap = columnEngine.getColumnAliasMap();
        if (columnAliasMap.size() == 0) {
            columnAliasMap = columnEngine.getTable().getColumnAliasMap();
        }
        String sql = this.updateOrInsertParser.updateOrInsert(columnEngine.getTableName(), columnAliasMap, batchArgs.length);
        return this.jdbcTemplate.update(sql, new BatchArgumentPreparedStatementSetter(batchArgs, columnAliasMap.size()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public int updateOrInsertArgs(Collection<?> batchArgs, ColumnEngine columnEngine) {
        Map<String, String> columnAliasMap = columnEngine.getColumnAliasMap();
        if (columnAliasMap.size() == 0) {
            columnAliasMap = columnEngine.getTable().getColumnAliasMap();
        }
        String sql = this.updateOrInsertParser.updateOrInsert(columnEngine.getTableName(), columnAliasMap, batchArgs.size());
        return this.jdbcTemplate.update(sql, new BatchArgumentPreparedStatementSetter(batchArgs, columnAliasMap.size()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int updateOrInsertRecord(Object[] records, Class<T> modelClass) {
        Model model = newModel(modelClass);
        Map<String, String> columnAliasMap = model.getColumnAliasMap();
        String sql = this.updateOrInsertParser.updateOrInsert(model.getTableName(), columnAliasMap, records.length);
        return this.jdbcTemplate.update(sql, new BatchArrayRecordPreparedStatementSetter(records, columnAliasMap));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int updateOrInsertRecord(Object[] records, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        Map<String, String> columnAliasMap = model.getColumnAliasMap();
        String sql = this.updateOrInsertParser.updateOrInsert(tableName, columnAliasMap, records.length);
        return this.jdbcTemplate.update(sql, new BatchArrayRecordPreparedStatementSetter(records, columnAliasMap));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int updateOrInsertRecord(Collection<?> records, Class<T> modelClass) {
        Model model = newModel(modelClass);
        Map<String, String> columnAliasMap = model.getColumnAliasMap();
        String sql = this.updateOrInsertParser.updateOrInsert(model.getTableName(), columnAliasMap, records.size());
        return this.jdbcTemplate.update(sql, new BatchCollectionRecordPreparedStatementSetter(records, columnAliasMap));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Model> int updateOrInsertRecord(Collection<?> records, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        Map<String, String> columnAliasMap = model.getColumnAliasMap();
        String sql = this.updateOrInsertParser.updateOrInsert(tableName, columnAliasMap, records.size());
        return this.jdbcTemplate.update(sql, new BatchCollectionRecordPreparedStatementSetter(records, columnAliasMap));
    }

    @Override
    @SuppressWarnings("unchecked")
    public int updateOrInsertRecord(Object[] records, ColumnEngine columnEngine) {
        Map<String, String> columnAliasMap = columnEngine.getColumnAliasMap();
        if (columnAliasMap.size() == 0) {
            columnAliasMap = columnEngine.getTable().getColumnAliasMap();
        }
        String sql = this.updateOrInsertParser.updateOrInsert(columnEngine.getTableName(), columnAliasMap, records.length);
        return this.jdbcTemplate.update(sql, new BatchArrayRecordPreparedStatementSetter(records, columnAliasMap));
    }

    @Override
    @SuppressWarnings("unchecked")
    public int updateOrInsertRecord(Collection<?> records, ColumnEngine columnEngine) {
        Map<String, String> columnAliasMap = columnEngine.getColumnAliasMap();
        if (columnAliasMap.size() == 0) {
            columnAliasMap = columnEngine.getTable().getColumnAliasMap();
        }
        String sql = this.updateOrInsertParser.updateOrInsert(columnEngine.getTableName(), columnAliasMap, records.size());
        return this.jdbcTemplate.update(sql, new BatchCollectionRecordPreparedStatementSetter(records, columnAliasMap));
    }

    @Override
    public <T extends Model> int deleteByPrimaryKey(Object keyValue, Class<T> modelClass) {
        Model model = newModel(modelClass);
        String sql = this.deleteParser.deleteByPrimaryKey(model.getTableName(), model.getPrimaryKeyName());
        return this.jdbcTemplate.update(sql, keyValue);
    }

    @Override
    public <T extends Model> int deleteByPrimaryKey(Object keyValue, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        String sql = this.deleteParser.deleteByPrimaryKey(tableName, model.getPrimaryKeyName());
        return this.jdbcTemplate.update(sql, keyValue);
    }

    @Override
    public <T extends Model> int batchDeleteByPrimaryKeys(Object[] keyValues, Class<T> modelClass) {
        Model model = newModel(modelClass);
        String sql = this.deleteParser.batchDeleteByPrimaryKeys(model.getTableName(), model.getPrimaryKeyName(), keyValues.length);
        return this.jdbcTemplate.update(sql, keyValues);
    }

    @Override
    public <T extends Model> int batchDeleteByPrimaryKeys(Object[] keyValues, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        String sql = this.deleteParser.batchDeleteByPrimaryKeys(tableName, model.getPrimaryKeyName(), keyValues.length);
        return this.jdbcTemplate.update(sql, keyValues);
    }

    @Override
    public <T extends Model> int batchDeleteByPrimaryKeys(Collection<?> keyValues, Class<T> modelClass) {
        Model model = newModel(modelClass);
        String sql = this.deleteParser.batchDeleteByPrimaryKeys(model.getTableName(), model.getPrimaryKeyName(), keyValues.size());
        return this.jdbcTemplate.update(sql, new CollectionArgumentPreparedStatementSetter(keyValues));
    }

    @Override
    public <T extends Model> int batchDeleteByPrimaryKeys(Collection<?> keyValues, String tableName, Class<T> modelClass) {
        Model model = newModel(modelClass);
        String sql = this.deleteParser.batchDeleteByPrimaryKeys(tableName, model.getPrimaryKeyName(), keyValues.size());
        return this.jdbcTemplate.update(sql, new CollectionArgumentPreparedStatementSetter(keyValues));
    }

    @Override
    public int delete(WhereEngine whereEngine) {
        ParseData parseData = this.deleteParser.delete(whereEngine);
        System.out.println(parseData.getSql());
        return this.jdbcTemplate.update(parseData.getSql(), new CollectionArgumentPreparedStatementSetter(parseData.getArgs()));
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
