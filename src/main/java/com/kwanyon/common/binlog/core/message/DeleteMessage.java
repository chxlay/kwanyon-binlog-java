package com.kwanyon.common.binlog.core.message;

import lombok.Getter;

import java.io.Serializable;
import java.util.Map;

/**
 * 删除事件消息
 *
 * @author Alay
 * @since 2022-11-14 16:19
 */
@Getter
public class DeleteMessage extends BinlogMessage {

    private Map<String, Serializable> deleteData;

    public static DeleteMessage create() {
        return new DeleteMessage();
    }

    public DeleteMessage deleteData(Map<String, Serializable> deleteData) {
        this.deleteData = deleteData;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteMessage{" +
                "tableId=" + this.getTableId() +
                ", schema=" + this.getSchema() +
                ", tableName=" + this.getTableName() +
                ", sql=" + this.getSql() +
                ", action=" + this.getAction() +
                ", deleteData=" + deleteData +
                '}';
    }
}
