package com.kwanyon.common.binlog.core.parser;

import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.kwanyon.common.binlog.core.message.UpdateMessage;
import com.kwanyon.common.binlog.model.TableSchema;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 更新消息解析器
 *
 * @author Alay
 * @since 2022-11-14 17:02
 */
public class UpdateEventParser implements BinlogEventParser<UpdateRowsEventData, UpdateMessage> {
    private static final String ACTION = "UPDATE";

    @Override
    public UpdateMessage parseEvent(UpdateRowsEventData event, Object... args) {
        // 表结构
        TableSchema tableSchema = (TableSchema) args[0];
        List<String> columnNames = tableSchema.getColumnNames();
        // 修改前的数据
        List<Map.Entry<Serializable[], Serializable[]>> rows = event.getRows();
        // 解析数据
        Map<String, Serializable>[] maps = this.tableDataParse(columnNames, rows);


        UpdateMessage updateMessage = UpdateMessage.create()
                .beforeData(maps[0])
                .afterData(maps[1]);

        updateMessage.tableId(event.getTableId())
                .tableName(tableSchema.getTableName())
                .schema(tableSchema.getTableSchema())
                .action(ACTION);

        return updateMessage;
    }


    private Map<String, Serializable>[] tableDataParse(List<String> columnNames, List<Map.Entry<Serializable[], Serializable[]>> rows) {
        Map<String, Serializable>[] dataMapArr = new HashMap[2];
        // 数据体
        Map.Entry<Serializable[], Serializable[]> entry = rows.get(0);

        // 修改前的数据
        Serializable[] before = entry.getKey();
        Map<String, Serializable> beforeData = new HashMap<>();
        for (int i = 0; i < columnNames.size(); i++) {
            String columName = columnNames.get(i);
            beforeData.put(columName, before[i]);
        }
        dataMapArr[0] = beforeData;


        // 修改后的数据
        Serializable[] after = entry.getValue();
        Map<String, Serializable> afterData = new HashMap<>();
        for (int i = 0; i < columnNames.size(); i++) {
            String columName = columnNames.get(i);
            afterData.put(columName, after[i]);
        }
        dataMapArr[1] = afterData;

        return dataMapArr;
    }

}
