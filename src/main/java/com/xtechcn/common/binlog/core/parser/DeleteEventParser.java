package com.xtechcn.common.binlog.core.parser;

import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.xtechcn.common.binlog.core.message.DeleteMessage;
import com.xtechcn.common.binlog.model.TableSchema;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 删除数据事件 消息解析
 *
 * @author Alay
 * @date 2022-11-14 17:17
 */
public class DeleteEventParser implements BinlogEventParser<DeleteRowsEventData, DeleteMessage> {
    private static final String ACTION = "DELETE";


    @Override
    public DeleteMessage parseEvent(DeleteRowsEventData event, Object... args) {
        // 表结构
        TableSchema tableSchema = (TableSchema) args[0];
        // 删除的这一行数据
        List<Serializable[]> deleteRows = event.getRows();

        // 删除的数据转 Map
        Map<String, Serializable> insertData = this.parseTableData2Map(tableSchema.columnNames(), deleteRows);

        DeleteMessage deleteMessage = DeleteMessage.build().deleteData(insertData);
        deleteMessage.tableId(event.getTableId())
                .tableName(tableSchema.tableName())
                .schema(tableSchema.tableSchema())
                .action(ACTION);

        return deleteMessage;
    }

}
