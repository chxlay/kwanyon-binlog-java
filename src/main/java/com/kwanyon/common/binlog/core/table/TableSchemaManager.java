package com.kwanyon.common.binlog.core.table;


import com.kwanyon.common.binlog.model.TableSchema;

/**
 * 表结构管理
 *
 * @author Alay
 * @since 2023-10-07 11:27
 */
public interface TableSchemaManager {

    /**
     * 查询表结构数据
     *
     * @param database  数据库名
     * @param tableName 数据库表名
     * @return 表结构对象
     */
    TableSchema findSchema(String database, String tableName);

    /**
     * 查询表结构数据
     *
     * @param tableId binlog数据中表Id
     * @return 表结构对象
     */
    TableSchema findSchema(long tableId);

    /**
     * 添加表
     *
     * @param tableSchema 表结构实体对象
     */
    void addSchema(TableSchema tableSchema);

    /**
     * 移除表结构
     *
     * @param database  数据库名
     * @param tableName 数据库表名
     */
    void removeSchema(String database, String tableName);

    /**
     * 是否存在该表的数据
     *
     * @param tableId binlog数据中表Id
     * @return true为存在
     */
    boolean hasSchema(Long tableId);
}
