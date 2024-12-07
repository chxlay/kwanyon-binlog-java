package com.kwanyon.common.binlog.core.message;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Binlog 监听到的数据实体类
 *
 * @author Alay
 * @since 2022-11-14 13:18
 */
@Getter
@ToString
public class BinlogMessage implements Serializable {
    /**
     * 事件的 TableId, 可能为null( CREATE/ALTER/RENAME时候为 null)
     */
    private Long tableId;
    /**
     * 库名(不会为 null )
     */
    private String schema;
    /**
     * 表名(不会为 null )
     *
     * @return
     */
    private String tableName;

    /**
     * SQL 语句，可能为null( 只有 CREATE TABLE / DROP TABLE / ALTER TABLE / RENAME TABLE 时候才有 )
     *
     * @return
     */
    private String sql;
    /**
     * 操作的动作
     */
    private String action;

    public static BinlogMessage create() {
        return new BinlogMessage();
    }


    public BinlogMessage tableId(long tableId) {
        this.tableId = tableId;
        return this;
    }

    public BinlogMessage schema(String schema) {
        this.schema = schema;
        return this;
    }

    public BinlogMessage tableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public BinlogMessage sql(String sql) {
        this.sql = sql;
        return this;
    }

    public BinlogMessage action(String action) {
        this.action = action;
        return this;
    }
}
