package com.dt.jdbc.norm;

import com.dt.core.engine.ColumnEngine;
import com.dt.core.engine.WhereEngine;
import com.dt.core.norm.Engine;
import com.dt.core.norm.Model;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by 白超 on 2018/7/3.
 */
public interface JdbcEngine {

    Map<String, Object> queryByPrimaryKey(Object keyValue, ColumnEngine columnEngine);

    <T> T queryByPrimaryKey(Object keyValue, Class<T> returnType, ColumnEngine columnEngine);

    Map<String, Object> queryOne(Engine engine);

    <T> T queryOne(Class<T> returnType, Engine engine);

    List<Map<String, Object>> queryForList(Engine engine);

    <T> List<T> queryForList(Class<T> returnType, Engine engine);

    int queryCount(Engine engine);

    <K, V> Map<K, V> queryPairColumnInMap(Engine engine);

    <K, V> Map<K, V> queryPairColumnInMap(int keyIndex, int valueIndex, Engine engine);

    <K, V> Map<K, V> queryPairColumnInMap(String keyColumnName, String valueColumnName, Engine engine);

    /**
     * 不推荐使用
     *
     * @param modelClass
     * @param args
     * @param <T>
     * @return
     */
    default <T extends Model> int insertArgs(Class<T> modelClass, Object... args) {
        return this.insertArgs(args, modelClass);
    }

    /**
     * 不推荐使用
     *
     * @param tableName
     * @param modelClass
     * @param args
     * @param <T>
     * @return
     */
    default <T extends Model> int insertArgs(String tableName, Class<T> modelClass, Object... args) {
        return this.insertArgs(args, tableName, modelClass);
    }

    /**
     * 不推荐使用
     *
     * @param args
     * @param modelClass
     * @param <T>
     * @return
     */
    <T extends Model> int insertArgs(Object[] args, Class<T> modelClass);

    /**
     * 不推荐使用
     *
     * @param args
     * @param tableName
     * @param modelClass
     * @param <T>
     * @return
     */
    <T extends Model> int insertArgs(Object[] args, String tableName, Class<T> modelClass);

    /**
     * 不推荐使用
     *
     * @param args
     * @param modelClass
     * @param <T>
     * @return
     */
    <T extends Model> int insertArgs(Collection<?> args, Class<T> modelClass);

    /**
     * 不推荐使用
     *
     * @param args
     * @param tableName
     * @param modelClass
     * @param <T>
     * @return
     */
    <T extends Model> int insertArgs(Collection<?> args, String tableName, Class<T> modelClass);

    default int insertArgs(ColumnEngine columnEngine, Object... args) {
        return this.insertArgs(args, columnEngine);
    }

    int insertArgs(Object[] args, ColumnEngine columnEngine);

    int insertArgs(Collection<?> args, ColumnEngine columnEngine);

    <T extends Model> int insertRecord(Map<String, ?> record, Class<T> modelClass);

    <T extends Model> int insertRecord(Map<String, ?> record, String tableName, Class<T> modelClass);

    <T extends Model> int insertRecord(Object record, Class<T> modelClass);

    <T extends Model> int insertRecord(Object record, String tableName, Class<T> modelClass);

    int insertRecord(Map<String, ?> record, ColumnEngine columnEngine);

    int insertRecord(Object record, ColumnEngine columnEngine);

    <T extends Model> int insertRecordSelective(Map<String, ?> record, Class<T> modelClass);

    <T extends Model> int insertRecordSelective(Map<String, ?> record, String tableName, Class<T> modelClass);

    <T extends Model> int insertRecordSelective(Object record, Class<T> modelClass);

    <T extends Model> int insertRecordSelective(Object record, String tableName, Class<T> modelClass);

    int insertRecordSelective(Map<String, ?> record, ColumnEngine columnEngine);

    int insertRecordSelective(Object record, ColumnEngine columnEngine);

    /**
     * 不推荐使用
     *
     * @param modelClass
     * @param batchArgs
     * @param <T>
     * @return
     */
    default <T extends Model> int batchInsertArgs(Class<T> modelClass, Object... batchArgs) {
        return this.batchInsertArgs(batchArgs, modelClass);
    }

    /**
     * 不推荐使用
     *
     * @param tableName
     * @param modelClass
     * @param batchArgs
     * @param <T>
     * @return
     */
    default <T extends Model> int batchInsertArgs(String tableName, Class<T> modelClass, Object... batchArgs) {
        return this.batchInsertArgs(batchArgs, tableName, modelClass);
    }

    /**
     * 不推荐使用
     *
     * @param batchArgs
     * @param modelClass
     * @param <T>
     * @return
     */
    <T extends Model> int batchInsertArgs(Object[] batchArgs, Class<T> modelClass);

    /**
     * 不推荐使用
     *
     * @param batchArgs
     * @param tableName
     * @param modelClass
     * @param <T>
     * @return
     */
    <T extends Model> int batchInsertArgs(Object[] batchArgs, String tableName, Class<T> modelClass);

    /**
     * 不推荐使用
     *
     * @param batchArgs
     * @param modelClass
     * @param <T>
     * @return
     */
    <T extends Model> int batchInsertArgs(Collection<?> batchArgs, Class<T> modelClass);

    /**
     * 不推荐使用
     *
     * @param batchArgs
     * @param tableName
     * @param modelClass
     * @param <T>
     * @return
     */
    <T extends Model> int batchInsertArgs(Collection<?> batchArgs, String tableName, Class<T> modelClass);

    default int batchInsertArgs(ColumnEngine columnEngine, Object... batchArgs) {
        return this.batchInsertArgs(batchArgs, columnEngine);
    }

    int batchInsertArgs(Object[] batchArgs, ColumnEngine columnEngine);

    int batchInsertArgs(Collection<?> batchArgs, ColumnEngine columnEngine);

    default <T extends Model> int batchInsertRecords(Class<T> modelClass, Object... records) {
        return this.batchInsertRecords(records, modelClass);
    }

    default <T extends Model> int batchInsertRecords(String tableName, Class<T> modelClass, Object... records) {
        return this.batchInsertRecords(records, tableName, modelClass);
    }

    <T extends Model> int batchInsertRecords(Object[] records, Class<T> modelClass);

    <T extends Model> int batchInsertRecords(Object[] records, String tableName, Class<T> modelClass);

    <T extends Model> int batchInsertRecords(Collection<?> records, Class<T> modelClass);

    <T extends Model> int batchInsertRecords(Collection<?> records, String tableName, Class<T> modelClass);

    default int batchInsertRecords(ColumnEngine columnEngine, Object... records) {
        return this.batchInsertRecords(records, columnEngine);
    }

    int batchInsertRecords(Object[] records, ColumnEngine columnEngine);

    int batchInsertRecords(Collection<?> records, ColumnEngine columnEngine);

    /**
     * 不推荐使用
     *
     * @param keyValue
     * @param modelClass
     * @param args
     * @param <T>
     * @return
     */
    default <T extends Model> int updateArgsByPrimaryKey(Object keyValue, Class<T> modelClass, Object... args) {
        return this.updateArgsByPrimaryKey(keyValue, args, modelClass);
    }

    /**
     * 不推荐使用
     *
     * @param keyValue
     * @param tableName
     * @param modelClass
     * @param args
     * @param <T>
     * @return
     */
    default <T extends Model> int updateArgsByPrimaryKey(Object keyValue, String tableName, Class<T> modelClass, Object... args) {
        return this.updateArgsByPrimaryKey(keyValue, args, tableName, modelClass);
    }

    /**
     * 不推荐使用
     *
     * @param keyValue
     * @param args
     * @param modelClass
     * @param <T>
     * @return
     */
    <T extends Model> int updateArgsByPrimaryKey(Object keyValue, Object[] args, Class<T> modelClass);

    /**
     * 不推荐使用
     *
     * @param keyValue
     * @param args
     * @param tableName
     * @param modelClass
     * @param <T>
     * @return
     */
    <T extends Model> int updateArgsByPrimaryKey(Object keyValue, Object[] args, String tableName, Class<T> modelClass);

    /**
     * 不推荐使用
     *
     * @param keyValue
     * @param args
     * @param modelClass
     * @param <T>
     * @return
     */
    <T extends Model> int updateArgsByPrimaryKey(Object keyValue, Collection<?> args, Class<T> modelClass);

    /**
     * 不推荐使用
     *
     * @param keyValue
     * @param args
     * @param tableName
     * @param modelClass
     * @param <T>
     * @return
     */
    <T extends Model> int updateArgsByPrimaryKey(Object keyValue, Collection<?> args, String tableName, Class<T> modelClass);

    default int updateArgsByPrimaryKey(Object keyValue, ColumnEngine columnEngine, Object... args) {
        return this.updateArgsByPrimaryKey(keyValue, args, columnEngine);
    }

    int updateArgsByPrimaryKey(Object keyValue, Object[] args, ColumnEngine columnEngine);

    int updateArgsByPrimaryKey(Object keyValue, Collection<?> args, ColumnEngine columnEngine);

    <T extends Model> int updateRecordByPrimaryKey(Object keyValue, Map<String, ?> record, Class<T> modelClass);

    <T extends Model> int updateRecordByPrimaryKey(Object keyValue, Object record, Class<T> modelClass);

    <T extends Model> int updateRecordByPrimaryKey(Object keyValue, Map<String, ?> record, String tableName, Class<T> modelClass);

    <T extends Model> int updateRecordByPrimaryKey(Object keyValue, Object record, String tableName, Class<T> modelClass);

    int updateRecordByPrimaryKey(Object keyValue, Map<String, ?> record, ColumnEngine columnEngine);

    int updateRecordByPrimaryKey(Object keyValue, Object record, ColumnEngine columnEngine);

    <T extends Model> int updateRecordByPrimaryKeySelective(Object keyValue, Map<String, ?> record, Class<T> modelClass);

    <T extends Model> int updateRecordByPrimaryKeySelective(Object keyValue, Map<String, ?> record, String tableName, Class<T> modelClass);

    <T extends Model> int updateRecordByPrimaryKeySelective(Object keyValue, Object record, Class<T> modelClass);

    <T extends Model> int updateRecordByPrimaryKeySelective(Object keyValue, Object record, String tableName, Class<T> modelClass);

    int updateRecordByPrimaryKeySelective(Object keyValue, Map<String, ?> record, ColumnEngine columnEngine);

    int updateRecordByPrimaryKeySelective(Object keyValue, Object record, ColumnEngine columnEngine);

    int updateRecord(Map<String, ?> record, WhereEngine whereEngine);

    int updateRecord(Object record, WhereEngine whereEngine);

    int updateRecordSelective(Map<String, ?> record, WhereEngine whereEngine);

    int updateRecordSelective(Object record, WhereEngine whereEngine);

}
