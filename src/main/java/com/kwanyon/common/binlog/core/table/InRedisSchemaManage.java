package com.kwanyon.common.binlog.core.table;

import com.kwanyon.common.binlog.model.TableSchema;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 基于Redis的表结构信息管理
 *
 * @author Alay
 * @since 2023-10-07 11:54
 */
@RequiredArgsConstructor
public class InRedisSchemaManage implements TableSchemaManager {

    private final RedisTemplate redisTemplate;
    /**
     * 由于项目中使用了多租户,租户之间隔离,所以注入 CacheKeyPrefix（实现类编写了基于租户的前缀处理,不用租户的则使用默认实现类）
     */
    private final CacheKeyPrefix cacheKeyPrefix;
    private final SchemaStatement schemaStatement;

    private static final String TABLE_ID_HASH_KEY = "binlog:table_id_hash";
    private static final String SCHEMA_HASH_KEY = "binlog:schema_hash";

    @Override
    public TableSchema findSchema(String database, String tableName) {
        String schemaHash = cacheKeyPrefix.compute(SCHEMA_HASH_KEY);
        TableSchema tableSchema = (TableSchema) redisTemplate.opsForHash().get(schemaHash, database + "." + tableName);
        if (null != tableSchema) return tableSchema;

        return schemaStatement.querySchema(database, tableName);
    }

    @Override
    public TableSchema findSchema(long tableId) {
        String tableIdHash = cacheKeyPrefix.compute(TABLE_ID_HASH_KEY);
        String tableKey = (String) redisTemplate.opsForHash().get(tableIdHash, tableId);

        String schemaHash = cacheKeyPrefix.compute(SCHEMA_HASH_KEY);
        return (TableSchema) redisTemplate.opsForHash().get(schemaHash, tableKey);
    }

    @Override
    public void addSchema(TableSchema tableSchema) {
        String tableKey = tableSchema.getTableSchema() + "." + tableSchema.getTableName();

        String tableIdHash = cacheKeyPrefix.compute(TABLE_ID_HASH_KEY);
        redisTemplate.opsForHash().put(tableIdHash, tableSchema.getTableId(), tableKey);

        String schemaHash = cacheKeyPrefix.compute(SCHEMA_HASH_KEY);
        redisTemplate.opsForHash().put(schemaHash, tableKey, tableSchema);
    }

    @Override
    public void removeSchema(String database, String tableName) {
        String tableKey = database + "." + tableName;

        String tableIdHash = cacheKeyPrefix.compute(TABLE_ID_HASH_KEY);
        String schemaHash = cacheKeyPrefix.compute(SCHEMA_HASH_KEY);

        TableSchema tableSchema = (TableSchema) redisTemplate.opsForHash().get(schemaHash, tableKey);
        if (null != tableSchema) {
            redisTemplate.opsForHash().delete(schemaHash, tableKey);
            redisTemplate.opsForHash().delete(tableIdHash, tableSchema.getTableId());
        }
    }

    @Override
    public boolean hasSchema(Long tableId) {
        String tableIdHash = cacheKeyPrefix.compute(TABLE_ID_HASH_KEY);
        return redisTemplate.opsForHash().hasKey(tableIdHash, tableId);
    }

}
