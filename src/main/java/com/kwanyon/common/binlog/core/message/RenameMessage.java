package com.kwanyon.common.binlog.core.message;

import lombok.Getter;

/**
 * 表重命名 事件
 *
 * @author Alay
 * @since 2022-11-14 13:18
 */
@Getter
public class RenameMessage extends BinlogMessage {
    /**
     * 修改前的表名
     */
    private String beforeTable;
    /**
     * 修改后的表名
     */
    private String afterTable;


    public static RenameMessage create() {
        return new RenameMessage();
    }

    public RenameMessage beforeTable(String beforeTable) {
        this.beforeTable = beforeTable;
        return this;
    }

    public RenameMessage afterTable(String afterTable) {
        this.afterTable = afterTable;
        return this;
    }

    @Override
    public String toString() {
        return "RenameMessage{" +
                "tableId=" + this.getTableId() +
                ", schema=" + this.getSchema() +
                ", tableName=" + this.getTableName() +
                ", sql=" + this.getSql() +
                ", action=" + this.getAction() +
                ", beforeTable='" + beforeTable + '\'' +
                ", afterTable='" + afterTable + '\'' +
                '}';
    }
}
