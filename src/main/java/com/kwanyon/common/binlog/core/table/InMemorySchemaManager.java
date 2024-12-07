package com.kwanyon.common.binlog.core.table;

import com.kwanyon.common.binlog.model.TableSchema;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 基于内存的表结构信息管理
 *
 * @author Alay
 * @since 2022-11-14 19:16
 */
@Component
@RequiredArgsConstructor
public class InMemorySchemaManager implements TableSchemaManager {
    /**
     * tableId 与 表全名的映射关系
     */
    private static final Map<Long, String> TABLE_ID_MAP = new HashMap<>(1 << 4);
    /**
     * 表名(全名)与表结构的映射
     */
    private static final Map<String, TableSchema> SCHEMA_MAP = new HashMap<>(1 << 4);
    private final SchemaStatement schemaStatement;


    @Override
    public TableSchema findSchema(String database, String tableName) {
        // 内存中获取
        TableSchema tableSchema = SCHEMA_MAP.get(database + "." + tableName);
        if (null != tableSchema) return tableSchema;

        // 通过 Jdbc 查询表结构
        return schemaStatement.querySchema(database, tableName);
    }

    @Override
    public TableSchema findSchema(long tableId) {
        String tableInfo = TABLE_ID_MAP.get(tableId);
        return SCHEMA_MAP.get(tableInfo);
    }

    @Override
    public void addSchema(TableSchema tableSchema) {
        // 本地缓存
        SCHEMA_MAP.put(tableSchema.getTableSchema() + "." + tableSchema.getTableName(), tableSchema);
        // tableId 映射关系缓存
        TABLE_ID_MAP.put(tableSchema.getTableId(), tableSchema.getTableSchema() + "." + tableSchema.getTableName());
    }

    @Override
    public void removeSchema(String database, String tableName) {
        String tableKey = database + "." + tableName;
        TableSchema tableSchema = SCHEMA_MAP.get(tableKey);
        if (null != tableSchema) {
            // 移除
            TABLE_ID_MAP.remove(tableSchema.getTableId());
            SCHEMA_MAP.remove(tableKey);
        }
    }

    @Override
    public boolean hasSchema(Long tableId) {
        return TABLE_ID_MAP.get(tableId) != null;
    }

}
