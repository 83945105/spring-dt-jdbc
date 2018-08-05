package com.dt.jdbc.norm;

import com.dt.core.engine.ColumnEngine;
import com.dt.core.engine.LimitEngine;
import com.dt.core.engine.WhereEngine;
import com.dt.core.norm.Engine;
import com.dt.core.norm.Model;
import com.dt.jdbc.bean.PageResultForBean;
import com.dt.jdbc.bean.PageResultForMap;
import com.dt.jdbc.bean.Pagination;
import com.dt.jdbc.core.SpringJdbcEngine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * jdbc 增删改查接口
 * <p>提供总计多达<b>100+</b>种方法
 * <p>注意,凡是涉及列名请使用驼峰命名法转义后的属性名,生成的模板类有对应的常量供使用
 *
 * @author 白超
 * @version 1.0
 * @see SpringJdbcEngine
 * @since 2018/7/10
 */
@SuppressWarnings("unused")
public interface JdbcEngine {

    /**
     * 复制一张表
     * <p>不会复制表的数据
     *
     * @param sourceTableName 源表名
     * @param targetTableName 目标表名
     * @return 不反回任何值, 这里返回int为占位用
     */
    int copyTable(String sourceTableName, String targetTableName);

    /**
     * 删除表
     *
     * @param tableName 表名
     * @return 不反回任何值, 这里返回int为占位用
     */
    int deleteTable(String tableName);

    /**
     * 重命名表
     *
     * @param sourceTableName 源表名
     * @param targetTableName 目标表名
     * @return 不反回任何值, 这里返回 {@code int} 为占位用
     */
    int renameTable(String sourceTableName, String targetTableName);

    /**
     * 查询表是否存在
     *
     * @param tableName 表名
     * @return 存在返回 {@code true}, 不存在返回 {@link false}
     */
    boolean isTableExist(String tableName);

    /**
     * 根据主键查询
     * <p>若查询不到对应数据,返回 {@code null}
     *
     * @param keyValue     主键值
     * @param columnEngine 用于构建查询SQL的字段引擎 {@link com.dt.core.engine.MySqlEngine}
     * @return 查询结果注入Map返回, key-属性名(驼峰命名法) value-属性值
     */
    Map<String, Object> queryByPrimaryKey(Object keyValue, ColumnEngine columnEngine);

    /**
     * 根据主键查询
     * <p>若查询不到对应数据,返回 {@code null}
     * <p>注意,用于接收数据的容器必须具备对应查询字段(驼峰命名法)的setter方法
     *
     * @param keyValue     主键值
     * @param returnType   返回容器类型,用于接收查询结果
     * @param columnEngine 用于构建查询SQL的字段引擎 {@link com.dt.core.engine.MySqlEngine}
     * @param <T>          与returnType指定数据类型一致
     * @return 查询结果注入指定的returnType对象
     */
    <T> T queryByPrimaryKey(Object keyValue, Class<T> returnType, ColumnEngine columnEngine);

    /**
     * 查询唯一一条数据
     * <p>若查询不到对应数据,返回 {@code null}
     * <p>若查询到多条数据,抛异常 {@link org.springframework.dao.IncorrectResultSizeDataAccessException}
     *
     * @param engine 用于构建查询SQL的引擎 {@link com.dt.core.engine.MySqlEngine}
     * @return 查询结果注入Map返回, key-属性名(驼峰命名法) value-属性值
     */
    Map<String, Object> queryOne(Engine engine);

    /**
     * 查询唯一一条数据
     * <p>若查询不到对应数据,返回 {@code null}
     * <p>若查询到多条数据,抛异常 {@link org.springframework.dao.IncorrectResultSizeDataAccessException}
     * <p>注意,用于接收数据的容器必须具备对应查询字段(驼峰命名法)的setter方法
     *
     * @param returnType 返回容器类型,用于接收查询结果
     * @param engine     用于构建查询SQL的引擎 {@link com.dt.core.engine.MySqlEngine}
     * @param <T>        与returnType指定数据类型一致
     * @return 查询结果注入指定的returnType对象
     */
    <T> T queryOne(Class<T> returnType, Engine engine);

    /**
     * 查询多条数据
     * <p>若查询不到对应数据,返回长度为0的空集合
     *
     * @param engine 用于构建查询SQL的引擎 {@link com.dt.core.engine.MySqlEngine}
     * @return 查询结果注入Map装入ArrayList返回, key-属性名(驼峰命名法) value-属性值
     */
    List<Map<String, Object>> queryForList(Engine engine);

    /**
     * 查询多条数据
     * <p>若查询不到对应数据,返回长度为0的空集合
     * <p>注意,用于接收数据的容器必须具备对应查询字段(驼峰命名法)的setter方法
     *
     * @param returnType 返回容器类型,用于接收查询结果
     * @param engine     用于构建查询SQL的引擎 {@link com.dt.core.engine.MySqlEngine}
     * @param <T>        与returnType指定数据类型一致
     * @return 查询结果注入指定的returnType对象装入ArrayList返回
     */
    <T> List<T> queryForList(Class<T> returnType, Engine engine);

    /**
     * 查询总数
     * <p>若查询不到对应数据,抛异常 {@link org.springframework.dao.EmptyResultDataAccessException}
     * <p>若查询到多条数据,抛异常 {@link org.springframework.dao.IncorrectResultSizeDataAccessException}
     *
     * @param engine 用于构建查询SQL的引擎 {@link com.dt.core.engine.MySqlEngine}
     * @return 总数
     */
    int queryCount(Engine engine);

    /**
     * 分页查询
     * <p>默认方法,内部先调用 {@link #queryCount(Engine)} 查询总数
     * <p>若总数为0,则直接返回结果
     * <p>若总数不为0,则根据参数构建分页对象 {@link Pagination} 并获取分页起始号
     * <p>最后调用 {@link #queryForList(Engine)} 查询数据
     *
     * @param currentPage 当前页号
     * @param pageSize    每页显示条数
     * @param engine      用于构建查询SQL的引擎 {@link com.dt.core.engine.MySqlEngine}
     * @return 分页结果 {@link PageResultForMap}
     */
    default PageResultForMap pageQueryForList(int currentPage, int pageSize, LimitEngine engine) {
        int count = this.queryCount(engine);
        Pagination pagination = new Pagination(count, currentPage, pageSize);
        PageResultForMap pageResult = new PageResultForMap();
        pageResult.setPagination(pagination);
        if (count == 0) {
            pageResult.setResult(new ArrayList<>());
            return pageResult;
        }
        engine.limit(pagination.getLimitStart(), pagination.getLimitEnd());
        pageResult.setResult(this.queryForList(engine));
        return pageResult;
    }

    /**
     * 分页查询
     * <p>默认方法,内部先调用 {@link #queryCount(Engine)} 查询总数
     * <p>若总数为0,则直接返回结果
     * <p>若总数不为0,则根据参数构建分页对象 {@link Pagination} 并获取分页起始号
     * <p>最后调用 {@link #queryForList(Engine)} 查询数据
     *
     * @param returnType  返回容器类型,用于接收查询结果
     * @param currentPage 当前页号
     * @param pageSize    每页显示条数
     * @param engine      用于构建查询SQL的引擎 {@link com.dt.core.engine.MySqlEngine}
     * @param <T>         与returnType指定数据类型一致
     * @return 分页结果 {@link PageResultForBean}
     */
    default <T> PageResultForBean<T> pageQueryForList(Class<T> returnType, int currentPage, int pageSize, LimitEngine engine) {
        int count = this.queryCount(engine);
        Pagination pagination = new Pagination(count, currentPage, pageSize);
        PageResultForBean<T> pageResult = new PageResultForBean<>();
        pageResult.setPagination(pagination);
        if (count == 0) {
            pageResult.setResult(new ArrayList<>());
            return pageResult;
        }
        engine.limit(pagination.getLimitStart(), pagination.getLimitEnd());
        pageResult.setResult(this.queryForList(returnType, engine));
        return pageResult;
    }

    /**
     * 查询一对列值存入Map(默认第一列为key,第二列为value)
     * <p>你可以使用该方法将某列值指定为key,另一列列值为value,结果集注入Map中
     * <p>注意,由于Map集合特性,作为key的列值,若重复出现,则会覆盖前者数据
     *
     * @param engine 用于构建查询SQL的引擎 {@link com.dt.core.engine.MySqlEngine}
     * @param <K>    作为key的列值类型
     * @param <V>    作为value的列值类型
     * @return 查询结果注入Map返回
     */
    <K, V> Map<K, V> queryPairColumnInMap(Engine engine);

    /**
     * 查询一对列值存入Map
     * <p>你可以使用该方法将某列值指定为key,另一列列值为value,结果集注入Map中
     * <p>注意,由于Map集合特性,作为key的列值,若重复出现,则会覆盖前者数据
     *
     * @param keyIndex   作为key的列下标(从1开始)
     * @param valueIndex 作为value的列下标(从1开始)
     * @param engine     用于构建查询SQL的引擎 {@link com.dt.core.engine.MySqlEngine}
     * @param <K>        作为key的列值类型
     * @param <V>        作为value的列值类型
     * @return 查询结果注入Map返回
     */
    <K, V> Map<K, V> queryPairColumnInMap(int keyIndex, int valueIndex, Engine engine);

    /**
     * 查询一对列值存入Map
     * <p>你可以使用该方法将某列值指定为key,另一列列值为value,结果集注入Map中
     * <p>注意,由于Map集合特性,作为key的列值,若重复出现,则会覆盖前者数据
     *
     * @param keyColumnName   作为key的列字段名(驼峰命名法)
     * @param valueColumnName 作为value的列字段名(驼峰命名法)
     * @param engine          用于构建查询SQL的引擎 {@link com.dt.core.engine.MySqlEngine}
     * @param <K>             作为key的列值类型
     * @param <V>             作为value的列值类型
     * @return 查询结果注入Map返回
     */
    <K, V> Map<K, V> queryPairColumnInMap(String keyColumnName, String valueColumnName, Engine engine);

    /**
     * 查询结果存入Map
     * <p>该方法类似于 {@link #queryPairColumnInMap(int, int, Engine)}
     * <p>你可以使用该方法将某列值指定为key,然后每一行的结果数据作为value,结果集注入Map中
     * <p>注意,由于Map集合特性,作为key的列值,若重复出现,则会覆盖前者数据
     *
     * @param keyIndex 作为key的列下标(从1开始)
     * @param engine   用于构建查询SQL的引擎 {@link com.dt.core.engine.MySqlEngine}
     * @param <K>      作为key的列值类型
     * @return 查询结果注入Map返回
     */
    <K> Map<K, Map<String, Object>> queryForListInMap(int keyIndex, Engine engine);

    /**
     * 查询结果存入Map
     * <p>该方法类似于 {@link #queryPairColumnInMap(int, int, Engine)}
     * <p>你可以使用该方法将某列值指定为key,然后每一行的结果数据作为value,结果集注入Map中
     * <p>注意,由于Map集合特性,作为key的列值,若重复出现,则会覆盖前者数据
     *
     * @param keyColumnName 作为key的列字段名(驼峰命名法)
     * @param engine        用于构建查询SQL的引擎 {@link com.dt.core.engine.MySqlEngine}
     * @param <K>           作为key的列值类型
     * @return 查询结果注入Map返回
     */
    <K> Map<K, Map<String, Object>> queryForListInMap(String keyColumnName, Engine engine);

    /**
     * 查询结果存入Map
     * <p>该方法类似于 {@link #queryPairColumnInMap(int, int, Engine)}
     * <p>你可以使用该方法将某列值指定为key,然后每一行的结果数据作为value,结果集注入Map中
     * <p>注意,由于Map集合特性,作为key的列值,若重复出现,则会覆盖前者数据
     *
     * @param keyIndex   作为key的列下标(从1开始)
     * @param returnType 返回容器类型,用于接收查询结果
     * @param engine     用于构建查询SQL的引擎 {@link com.dt.core.engine.MySqlEngine}
     * @param <K>        作为key的列值类型
     * @param <T>        与returnType指定数据类型一致
     * @return 查询结果注入Map返回
     */
    <K, T> Map<K, T> queryForListInMap(int keyIndex, Class<T> returnType, Engine engine);

    /**
     * 查询结果存入Map
     * <p>该方法类似于 {@link #queryPairColumnInMap(int, int, Engine)}
     * <p>你可以使用该方法将某列值指定为key,然后每一行的结果数据作为value,结果集注入Map中
     * <p>注意,由于Map集合特性,作为key的列值,若重复出现,则会覆盖前者数据
     *
     * @param keyColumnName 作为key的列字段名(驼峰命名法)
     * @param returnType    返回容器类型,用于接收查询结果
     * @param engine        用于构建查询SQL的引擎 {@link com.dt.core.engine.MySqlEngine}
     * @param <K>           作为key的列值类型
     * @param <T>           与returnType指定数据类型一致
     * @return 查询结果注入Map返回
     */
    <K, T> Map<K, T> queryForListInMap(String keyColumnName, Class<T> returnType, Engine engine);

    /**
     * 不推荐使用该方法
     * <p>根据参数插入一条数据
     * <p>参数顺序需要对应列顺序
     *
     * @param modelClass 生成的表模型类
     * @param args       参数
     * @param <T>        用于限定参数类型 {@link Model}
     * @return 影响的行数
     */
    default <T extends Model> int insertArgs(Class<T> modelClass, Object... args) {
        return this.insertArgs(args, modelClass);
    }

    /**
     * 不推荐使用该方法
     * <p>根据参数插入一条数据
     * <p>参数顺序需要对应列顺序
     *
     * @param tableName  表名
     * @param modelClass 生成的表模型类
     * @param args       参数
     * @param <T>        用于限定参数类型 {@link Model}
     * @return 影响的行数
     */
    default <T extends Model> int insertArgs(String tableName, Class<T> modelClass, Object... args) {
        return this.insertArgs(args, tableName, modelClass);
    }

    /**
     * 不推荐使用该方法
     * <p>根据参数插入一条数据
     * <p>参数顺序需要对应列顺序
     *
     * @param args       参数
     * @param modelClass 生成的表模型类
     * @param <T>        用于限定参数类型 {@link Model}
     * @return 影响的行数
     */
    <T extends Model> int insertArgs(Object[] args, Class<T> modelClass);

    /**
     * 不推荐使用该方法
     * <p>根据参数插入一条数据
     * <p>参数顺序需要对应列顺序
     *
     * @param args       参数
     * @param tableName  表名
     * @param modelClass 生成的表模型类
     * @param <T>        用于限定参数类型 {@link Model}
     * @return 影响的行数
     */
    <T extends Model> int insertArgs(Object[] args, String tableName, Class<T> modelClass);

    /**
     * 不推荐使用该方法
     * <p>根据参数插入一条数据
     * <p>参数顺序需要对应列顺序
     *
     * @param args       参数
     * @param modelClass 生成的表模型类
     * @param <T>        用于限定参数类型 {@link Model}
     * @return 影响的行数
     */
    <T extends Model> int insertArgs(Collection<?> args, Class<T> modelClass);

    /**
     * 不推荐使用该方法
     * <p>根据参数插入一条数据
     * <p>参数顺序需要对应列顺序
     *
     * @param args       参数
     * @param tableName  表名
     * @param modelClass 生成的表模型类
     * @param <T>        用于限定参数类型 {@link Model}
     * @return 影响的行数
     */
    <T extends Model> int insertArgs(Collection<?> args, String tableName, Class<T> modelClass);

    /**
     * 指定列及参数插入一条数据
     * <p>列顺序必须和参数顺序一致
     *
     * @param columnEngine 用于构建查询SQL的字段引擎 {@link com.dt.core.engine.MySqlEngine}
     * @param args         参数
     * @return 影响的行数
     */
    default int insertArgs(ColumnEngine columnEngine, Object... args) {
        return this.insertArgs(args, columnEngine);
    }

    /**
     * 指定列及参数插入一条数据
     * <p>列顺序必须和参数顺序一致
     *
     * @param args         参数
     * @param columnEngine 用于构建查询SQL的字段引擎 {@link com.dt.core.engine.MySqlEngine}
     * @return 影响的行数
     */
    int insertArgs(Object[] args, ColumnEngine columnEngine);

    /**
     * 指定列及参数插入一条数据
     * <p>列顺序必须和参数顺序一致
     *
     * @param args         参数
     * @param columnEngine 用于构建查询SQL的字段引擎 {@link com.dt.core.engine.MySqlEngine}
     * @return 影响的行数
     */
    int insertArgs(Collection<?> args, ColumnEngine columnEngine);

    /**
     * 使用数据容器插入一条数据
     * <p>数据容器属性如果为 {@code null},则属性对应列值也为 {@code null}
     * <p>数据容器与列名使用驼峰命名法进行映射
     *
     * @param record     数据容器
     * @param modelClass 生成的表模型类
     * @param <T>        用于限定参数类型 {@link Model}
     * @return 影响的行数
     */
    <T extends Model> int insertRecord(Map<String, ?> record, Class<T> modelClass);

    /**
     * 使用数据容器插入一条数据
     * <p>数据容器属性如果为 {@code null},则属性对应列值也为 {@code null}
     * <p>数据容器与列名使用驼峰命名法进行映射
     *
     * @param record     数据容器
     * @param tableName  表名
     * @param modelClass 生成的表模型类
     * @param <T>        用于限定参数类型 {@link Model}
     * @return 影响的行数
     */
    <T extends Model> int insertRecord(Map<String, ?> record, String tableName, Class<T> modelClass);

    /**
     * 使用数据容器插入一条数据
     * <p>数据容器属性如果为 {@code null},则属性对应列值也为 {@code null}
     * <p>数据容器与列名使用驼峰命名法进行映射
     *
     * @param record     数据容器
     * @param modelClass 生成的表模型类
     * @param <T>        用于限定参数类型 {@link Model}
     * @return 影响的行数
     */
    <T extends Model> int insertRecord(Object record, Class<T> modelClass);

    /**
     * 使用数据容器插入一条数据
     * <p>数据容器属性如果为 {@code null},则属性对应列值也为 {@code null}
     * <p>数据容器与列名使用驼峰命名法进行映射
     *
     * @param record     数据容器
     * @param tableName  表名
     * @param modelClass 生成的表模型类
     * @param <T>        用于限定参数类型 {@link Model}
     * @return 影响的行数
     */
    <T extends Model> int insertRecord(Object record, String tableName, Class<T> modelClass);

    /**
     * 使用数据容器指定列名插入一条数据
     * <p>数据容器属性如果为 {@code null},则属性对应列值也为 {@code null}
     * <p>数据容器与列名使用驼峰命名法进行映射
     *
     * @param record       数据容器
     * @param columnEngine 用于构建查询SQL的字段引擎 {@link com.dt.core.engine.MySqlEngine}
     * @return 影响的行数
     */
    int insertRecord(Map<String, ?> record, ColumnEngine columnEngine);

    /**
     * 使用数据容器指定列名插入一条数据
     * <p>数据容器属性如果为 {@code null},则属性对应列值也为 {@code null}
     * <p>数据容器与列名使用驼峰命名法进行映射
     *
     * @param record       数据容器
     * @param columnEngine 用于构建查询SQL的字段引擎 {@link com.dt.core.engine.MySqlEngine}
     * @return 影响的行数
     */
    int insertRecord(Object record, ColumnEngine columnEngine);

    /**
     * 使用数据容器指定列名插入一条数据
     * <p>数据容器属性如果为 {@code null},则不插入该属性对应列
     * <p>数据容器与列名使用驼峰命名法进行映射
     *
     * @param record     数据容器
     * @param modelClass 生成的表模型类
     * @param <T>        用于限定参数类型 {@link Model}
     * @return 影响的行数
     */
    <T extends Model> int insertRecordSelective(Map<String, ?> record, Class<T> modelClass);

    /**
     * 使用数据容器指定列名插入一条数据
     * <p>数据容器属性如果为 {@code null},则不插入该属性对应列
     * <p>数据容器与列名使用驼峰命名法进行映射
     *
     * @param record     数据容器
     * @param tableName  表名
     * @param modelClass 生成的表模型类
     * @param <T>        用于限定参数类型 {@link Model}
     * @return 影响的行数
     */
    <T extends Model> int insertRecordSelective(Map<String, ?> record, String tableName, Class<T> modelClass);

    /**
     * 使用数据容器指定列名插入一条数据
     * <p>数据容器属性如果为 {@code null},则不插入该属性对应列
     * <p>数据容器与列名使用驼峰命名法进行映射
     *
     * @param record     数据容器
     * @param modelClass 生成的表模型类
     * @param <T>        用于限定参数类型 {@link Model}
     * @return 影响的行数
     */
    <T extends Model> int insertRecordSelective(Object record, Class<T> modelClass);

    /**
     * 使用数据容器指定列名插入一条数据
     * <p>数据容器属性如果为 {@code null},则不插入该属性对应列
     * <p>数据容器与列名使用驼峰命名法进行映射
     *
     * @param record     数据容器
     * @param tableName  表名
     * @param modelClass 生成的表模型类
     * @param <T>        用于限定参数类型 {@link Model}
     * @return 影响的行数
     */
    <T extends Model> int insertRecordSelective(Object record, String tableName, Class<T> modelClass);

    /**
     * 使用数据容器指定列名插入一条数据
     * <p>数据容器属性如果为 {@code null},则不插入该属性对应列
     * <p>数据容器与列名使用驼峰命名法进行映射
     *
     * @param record       数据容器
     * @param columnEngine 用于构建查询SQL的字段引擎 {@link com.dt.core.engine.MySqlEngine}
     * @return 影响的行数
     */
    int insertRecordSelective(Map<String, ?> record, ColumnEngine columnEngine);

    /**
     * 使用数据容器指定列名插入一条数据
     * <p>数据容器属性如果为 {@code null},则不插入该属性对应列
     * <p>数据容器与列名使用驼峰命名法进行映射
     *
     * @param record       数据容器
     * @param columnEngine 用于构建查询SQL的字段引擎 {@link com.dt.core.engine.MySqlEngine}
     * @return 影响的行数
     */
    int insertRecordSelective(Object record, ColumnEngine columnEngine);

    /**
     * 不推荐使用
     * <p>根据参数批量插入数据
     *
     * @param modelClass 生成的表模型类
     * @param batchArgs  参数
     * @param <T>        用于限定参数类型 {@link Model}
     * @return 影响的行数
     */
    default <T extends Model> int batchInsertArgs(Class<T> modelClass, Object... batchArgs) {
        return this.batchInsertArgs(batchArgs, modelClass);
    }

    /**
     * 不推荐使用
     * <p>根据参数批量插入数据
     *
     * @param tableName  表名
     * @param modelClass 生成的表模型类
     * @param batchArgs  参数
     * @param <T>        用于限定参数类型 {@link Model}
     * @return 影响的行数
     */
    default <T extends Model> int batchInsertArgs(String tableName, Class<T> modelClass, Object... batchArgs) {
        return this.batchInsertArgs(batchArgs, tableName, modelClass);
    }

    /**
     * 不推荐使用
     * <p>根据参数批量插入数据
     *
     * @param batchArgs  参数
     * @param modelClass 生成的表模型类
     * @param <T>        用于限定参数类型 {@link Model}
     * @return 影响的行数
     */
    <T extends Model> int batchInsertArgs(Object[] batchArgs, Class<T> modelClass);

    /**
     * 不推荐使用
     * <p>根据参数批量插入数据
     *
     * @param batchArgs  参数
     * @param tableName  表名
     * @param modelClass 生成的表模型类
     * @param <T>        用于限定参数类型 {@link Model}
     * @return 影响的行数
     */
    <T extends Model> int batchInsertArgs(Object[] batchArgs, String tableName, Class<T> modelClass);

    /**
     * 不推荐使用
     * <p>根据参数批量插入数据
     *
     * @param batchArgs  参数
     * @param modelClass 生成的表模型类
     * @param <T>        用于限定参数类型 {@link Model}
     * @return 影响的行数
     */
    <T extends Model> int batchInsertArgs(Collection<?> batchArgs, Class<T> modelClass);

    /**
     * 不推荐使用
     * <p>根据参数批量插入数据
     *
     * @param batchArgs  参数
     * @param tableName  表名
     * @param modelClass 生成的表模型类
     * @param <T>        用于限定参数类型 {@link Model}
     * @return 影响的行数
     */
    <T extends Model> int batchInsertArgs(Collection<?> batchArgs, String tableName, Class<T> modelClass);

    /**
     * 指定列名及参数批量插入数据
     *
     * @param columnEngine 用于构建查询SQL的字段引擎 {@link com.dt.core.engine.MySqlEngine}
     * @param batchArgs    参数
     * @return 影响的行数
     */
    default int batchInsertArgs(ColumnEngine columnEngine, Object... batchArgs) {
        return this.batchInsertArgs(batchArgs, columnEngine);
    }

    /**
     * 指定列名及参数批量插入数据
     *
     * @param batchArgs    参数
     * @param columnEngine 用于构建查询SQL的字段引擎 {@link com.dt.core.engine.MySqlEngine}
     * @return 影响的行数
     */
    int batchInsertArgs(Object[] batchArgs, ColumnEngine columnEngine);

    /**
     * 指定列名及参数批量插入数据
     *
     * @param batchArgs    参数
     * @param columnEngine 用于构建查询SQL的字段引擎 {@link com.dt.core.engine.MySqlEngine}
     * @return 影响的行数
     */
    int batchInsertArgs(Collection<?> batchArgs, ColumnEngine columnEngine);

    /**
     * 使用数据容器批量插入数据
     * <p>数据容器属性如果为 {@code null},则不插入该属性对应列
     * <p>数据容器与列名使用驼峰命名法进行映射
     *
     * @param modelClass 生成的表模型类
     * @param records    数据容器
     * @param <T>        用于限定参数类型 {@link Model}
     * @return 影响的行数
     */
    default <T extends Model> int batchInsertRecords(Class<T> modelClass, Object... records) {
        return this.batchInsertRecords(records, modelClass);
    }

    /**
     * 使用数据容器批量插入数据
     * <p>数据容器属性如果为 {@code null},则不插入该属性对应列
     * <p>数据容器与列名使用驼峰命名法进行映射
     *
     * @param tableName  表名
     * @param modelClass 生成的表模型类
     * @param records    数据容器
     * @param <T>        用于限定参数类型 {@link Model}
     * @return 影响的行数
     */
    default <T extends Model> int batchInsertRecords(String tableName, Class<T> modelClass, Object... records) {
        return this.batchInsertRecords(records, tableName, modelClass);
    }

    /**
     * 使用数据容器批量插入数据
     * <p>数据容器属性如果为 {@code null},则不插入该属性对应列
     * <p>数据容器与列名使用驼峰命名法进行映射
     *
     * @param records    数据容器
     * @param modelClass 生成的表模型类
     * @param <T>        用于限定参数类型 {@link Model}
     * @return 影响的行数
     */
    <T extends Model> int batchInsertRecords(Object[] records, Class<T> modelClass);

    /**
     * 使用数据容器批量插入数据
     * <p>数据容器属性如果为 {@code null},则不插入该属性对应列
     * <p>数据容器与列名使用驼峰命名法进行映射
     *
     * @param records    数据容器
     * @param tableName  表名
     * @param modelClass 生成的表模型类
     * @param <T>        用于限定参数类型 {@link Model}
     * @return 影响的行数
     */
    <T extends Model> int batchInsertRecords(Object[] records, String tableName, Class<T> modelClass);

    /**
     * 使用数据容器批量插入数据
     * <p>数据容器属性如果为 {@code null},则不插入该属性对应列
     * <p>数据容器与列名使用驼峰命名法进行映射
     *
     * @param records    数据容器
     * @param modelClass 生成的表模型类
     * @param <T>        用于限定参数类型 {@link Model}
     * @return 影响的行数
     */
    <T extends Model> int batchInsertRecords(Collection<?> records, Class<T> modelClass);

    /**
     * 使用数据容器批量插入数据
     * <p>数据容器属性如果为 {@code null},则不插入该属性对应列
     * <p>数据容器与列名使用驼峰命名法进行映射
     *
     * @param records    数据容器
     * @param tableName  表名
     * @param modelClass 生成的表模型类
     * @param <T>        用于限定参数类型 {@link Model}
     * @return 影响的行数
     */
    <T extends Model> int batchInsertRecords(Collection<?> records, String tableName, Class<T> modelClass);

    /**
     * 使用数据容器指定列名批量插入数据
     * <p>数据容器属性如果为 {@code null},则不插入该属性对应列
     * <p>数据容器与列名使用驼峰命名法进行映射
     *
     * @param columnEngine 用于构建查询SQL的字段引擎 {@link com.dt.core.engine.MySqlEngine}
     * @param records      数据容器
     * @return 影响的行数
     */
    default int batchInsertRecords(ColumnEngine columnEngine, Object... records) {
        return this.batchInsertRecords(records, columnEngine);
    }

    /**
     * 使用数据容器指定列名批量插入数据
     * <p>数据容器属性如果为 {@code null},则不插入该属性对应列
     * <p>数据容器与列名使用驼峰命名法进行映射
     *
     * @param records      数据容器
     * @param columnEngine 用于构建查询SQL的字段引擎 {@link com.dt.core.engine.MySqlEngine}
     * @return 影响的行数
     */
    int batchInsertRecords(Object[] records, ColumnEngine columnEngine);

    /**
     * 使用数据容器指定列名批量插入数据
     * <p>数据容器属性如果为 {@code null},则不插入该属性对应列
     * <p>数据容器与列名使用驼峰命名法进行映射
     *
     * @param records      数据容器
     * @param columnEngine 用于构建查询SQL的字段引擎 {@link com.dt.core.engine.MySqlEngine}
     * @return 影响的行数
     */
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
