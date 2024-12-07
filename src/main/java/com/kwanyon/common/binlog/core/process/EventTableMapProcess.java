package com.kwanyon.common.binlog.core.process;

import com.github.shyiko.mysql.binlog.event.EventType;
import com.github.shyiko.mysql.binlog.event.TableMapEventData;
import com.kwanyon.common.binlog.config.IBinlogProperties;
import com.kwanyon.common.binlog.core.message.BinlogMessage;
import com.kwanyon.common.binlog.core.table.TableSchemaManager;
import com.kwanyon.common.binlog.model.TableSchema;
import lombok.RequiredArgsConstructor;

/**
 * INSERT / UPDATE / DELETE 将会触发此事件
 *
 * @author Alay
 * @since 2022-11-15 11:35
 */
@RequiredArgsConstructor
public class EventTableMapProcess implements BinlogEventProcess<TableMapEventData> {

    private final IBinlogProperties binlogProperties;
    private final TableSchemaManager tableSchemaManager;

    @Override
    public boolean support(EventType eventType) {
        return EventType.TABLE_MAP == eventType;
    }

    @Override
    public BinlogMessage process(TableMapEventData eventData) {
        /**
         * 表结构映射事件
         */
        TableMapEventData mapEventData = eventData;
        // tableId 并非表名
        long tableId = mapEventData.getTableId();
        boolean hasCache = tableSchemaManager.hasSchema(tableId);

        // 此表已经不是第一次触发该事件了，不需要重复的处理做准备的工作
        if (hasCache) return null;

        // 库名
        String database = mapEventData.getDatabase();
        // 表名
        String tableName = mapEventData.getTable();

        // 是否数据我们需要处理的表,不属于的不处理
        boolean needProcess = binlogProperties.tables().contains(database + "." + tableName);
        if (needProcess) return null;

        // 表结构对象封装
        TableSchema tableSchema = tableSchemaManager.findSchema(database, tableName);
        tableSchema.setTableId(tableId);

        tableSchemaManager.addSchema(tableSchema);

        return null;
    }
}
