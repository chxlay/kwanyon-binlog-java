package com.kwanyon.common.binlog.core.message;

import lombok.Getter;

import java.io.Serializable;
import java.util.Map;

/**
 * 更新数据事件消息
 *
 * @author Alay
 * @since 2022-11-14 16:02
 */
@Getter
public class UpdateMessage extends BinlogMessage {
    /**
     * 修改前的数据
     */
    private Map<String, Serializable> beforeData;
    /**
     * 修改后的数据
     */
    private Map<String, Serializable> afterData;

    public static UpdateMessage create() {
        return new UpdateMessage();
    }

    public UpdateMessage beforeData(Map<String, Serializable> beforeData) {
        this.beforeData = beforeData;
        return this;
    }

    public UpdateMessage afterData(Map<String, Serializable> afterData) {
        this.afterData = afterData;
        return this;
    }

    @Override
    public String toString() {
        return "UpdateMessage{" +
                "tableId=" + this.getTableId() +
                ", schema=" + this.getSchema() +
                ", tableName=" + this.getTableName() +
                ", sql=" + this.getSql() +
                ", action=" + this.getAction() +
                ", beforeData=" + beforeData +
                ", afterData=" + afterData +
                '}';
    }
}
