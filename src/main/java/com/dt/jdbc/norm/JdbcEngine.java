package com.dt.jdbc.norm;

import com.dt.core.engine.ColumnEngine;
import com.dt.core.engine.LimitEngine;
import com.dt.core.engine.WhereEngine;
import com.dt.core.norm.Engine;
import com.dt.core.norm.Model;
import com.dt.jdbc.bean.PageSupport;
import com.dt.jdbc.core.SpringJdbcEngine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * jdbc 增删改查接口
 * <p>提供总计多达<b>100+</b>种方法
 *
 * @author 白超
 * @version 1.0
 * @see SpringJdbcEngine
 * @since 2018/7/10
 */
public interface JdbcEngine {

    Map<String, Object> queryByPrimaryKey(Object keyValue, ColumnEngine columnEngine);

    <T> T queryByPrimaryKey(Object keyValue, Class<T> returnType, ColumnEngine columnEngine);

    Map<String, Object> queryOne(Engine engine);

    <T> T queryOne(Class<T> returnType, Engine engine);

    List<Map<String, Object>> queryForList(Engine engine);

    <T> List<T> queryForList(Class<T> returnType, Engine engine);

    int queryCount(Engine engine);

    default List<Map<String, Object>> pageQueryForList(int currentPage, int pageSize, LimitEngine engine) {
        int count = this.queryCount(engine);
        if (count == 0) {
            return new ArrayList<>();
        }
        PageSupport pageSupport = new PageSupport(count, currentPage, pageSize);
        engine.limit(pageSupport.getLimitStart(), pageSupport.getLimitEnd());
        return this.queryForList(engine);
    }

    default <T> List<T> pageQueryForList(Class<T> returnType, int currentPage, int pageSize, LimitEngine engine) {
        int count = this.queryCount(engine);
        if (count == 0) {
            return new ArrayList<>();
        }
        PageSupport pageSupport = new PageSupport(count, currentPage, pageSize);
        engine.limit(pageSupport.getLimitStart(), pageSupport.getLimitEnd());
        return this.queryForList(returnType, engine);
    }

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

    default <T extends Model> int batchUpdateRecordsByPrimaryKeys(Class<T> modelClass, Object... records) {
        return this.batchUpdateRecordsByPrimaryKeys(records, modelClass);
    }

    default <T extends Model> int batchUpdateRecordsByPrimaryKeys(String tableName, Class<T> modelClass, Object... records) {
        return this.batchUpdateRecordsByPrimaryKeys(records, tableName, modelClass);
    }

    <T extends Model> int batchUpdateRecordsByPrimaryKeys(Object[] records, Class<T> modelClass);

    <T extends Model> int batchUpdateRecordsByPrimaryKeys(Object[] records, String tableName, Class<T> modelClass);

    <T extends Model> int batchUpdateRecordsByPrimaryKeys(Collection<?> records, Class<T> modelClass);

    <T extends Model> int batchUpdateRecordsByPrimaryKeys(Collection<?> records, String tableName, Class<T> modelClass);

    default int batchUpdateRecordsByPrimaryKeys(WhereEngine whereEngine, Object... records) {
        return this.batchUpdateRecordsByPrimaryKeys(records, whereEngine);
    }

    int batchUpdateRecordsByPrimaryKeys(Object[] records, WhereEngine whereEngine);

    int batchUpdateRecordsByPrimaryKeys(Collection<?> records, WhereEngine whereEngine);

    default <T extends Model> int updateOrInsertArgs(Class<T> modelClass, Object... batchArgs) {
        return this.updateOrInsertArgs(batchArgs, modelClass);
    }

    default <T extends Model> int updateOrInsertArgs(String tableName, Class<T> modelClass, Object... batchArgs) {
        return this.updateOrInsertArgs(batchArgs, tableName, modelClass);
    }

    <T extends Model> int updateOrInsertArgs(Object[] batchArgs, Class<T> modelClass);

    <T extends Model> int updateOrInsertArgs(Object[] batchArgs, String tableName, Class<T> modelClass);

    <T extends Model> int updateOrInsertArgs(Collection<?> batchArgs, Class<T> modelClass);

    <T extends Model> int updateOrInsertArgs(Collection<?> batchArgs, String tableName, Class<T> modelClass);

    default int updateOrInsertArgs(ColumnEngine columnEngine, Object... batchArgs) {
        return this.updateOrInsertArgs(batchArgs, columnEngine);
    }

    int updateOrInsertArgs(Object[] batchArgs, ColumnEngine columnEngine);

    int updateOrInsertArgs(Collection<?> batchArgs, ColumnEngine columnEngine);

    default <T extends Model> int updateOrInsertRecord(Class<T> modelClass, Object... records) {
        return this.updateOrInsertRecord(records, modelClass);
    }

    default <T extends Model> int updateOrInsertRecord(String tableName, Class<T> modelClass, Object... records) {
        return this.updateOrInsertRecord(records, tableName, modelClass);
    }

    <T extends Model> int updateOrInsertRecord(Object[] records, Class<T> modelClass);

    <T extends Model> int updateOrInsertRecord(Object[] records, String tableName, Class<T> modelClass);

    <T extends Model> int updateOrInsertRecord(Collection<?> records, Class<T> modelClass);

    <T extends Model> int updateOrInsertRecord(Collection<?> records, String tableName, Class<T> modelClass);

    default int updateOrInsertRecord(ColumnEngine columnEngine, Object... records) {
        return this.updateOrInsertRecord(records, columnEngine);
    }

    int updateOrInsertRecord(Object[] records, ColumnEngine columnEngine);

    int updateOrInsertRecord(Collection<?> records, ColumnEngine columnEngine);

    <T extends Model> int deleteByPrimaryKey(Object keyValue, Class<T> modelClass);

    <T extends Model> int deleteByPrimaryKey(Object keyValue, String tableName, Class<T> modelClass);

    default <T extends Model> int batchDeleteByPrimaryKeys(Class<T> modelClass, Object... keyValues) {
        return this.batchDeleteByPrimaryKeys(keyValues, modelClass);
    }

    default <T extends Model> int batchDeleteByPrimaryKeys(String tableName, Class<T> modelClass, Object... keyValues) {
        return this.batchDeleteByPrimaryKeys(keyValues, tableName, modelClass);
    }

    <T extends Model> int batchDeleteByPrimaryKeys(Object[] keyValues, Class<T> modelClass);

    <T extends Model> int batchDeleteByPrimaryKeys(Object[] keyValues, String tableName, Class<T> modelClass);

    <T extends Model> int batchDeleteByPrimaryKeys(Collection<?> keyValues, Class<T> modelClass);

    <T extends Model> int batchDeleteByPrimaryKeys(Collection<?> keyValues, String tableName, Class<T> modelClass);

    int delete(WhereEngine whereEngine);

}
