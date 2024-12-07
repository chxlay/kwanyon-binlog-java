package com.kwanyon.common.binlog.core.message;

import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * 插入数据事件消息
 *
 * @author Alay
 * @since 2022-11-14 16:20
 */
@Setter
public class InsertMessage extends BinlogMessage {

    /**
     * 插入的数据内容
     */
    private Map<String, Serializable> insertData;


    public static InsertMessage create() {
        return new InsertMessage();
    }

    public InsertMessage insertData(Map<String, Serializable> insertData) {
        this.insertData = insertData;
        return this;
    }

    @Override
    public String toString() {
        return "InsertMessage{" +
                "tableId=" + this.getTableId() +
                ", schema=" + this.getSchema() +
                ", tableName=" + this.getTableName() +
                ", sql=" + this.getSql() +
                ", action=" + this.getAction() +
                ", insertData=" + insertData +
                '}';
    }
}
