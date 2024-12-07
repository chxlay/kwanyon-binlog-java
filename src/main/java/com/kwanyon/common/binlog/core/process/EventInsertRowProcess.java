package com.kwanyon.common.binlog.core.process;

import com.github.shyiko.mysql.binlog.event.EventType;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;
import com.kwanyon.common.binlog.core.message.InsertMessage;
import com.kwanyon.common.binlog.core.parser.InsertEventParser;
import com.kwanyon.common.binlog.core.table.TableSchemaManager;
import com.kwanyon.common.binlog.model.TableSchema;
import lombok.RequiredArgsConstructor;

/**
 * @author Alay
 * @since 2022-11-15 11:38
 */
@RequiredArgsConstructor
public class EventInsertRowProcess implements BinlogEventProcess<WriteRowsEventData> {

    private final TableSchemaManager tableSchemaManager;
    private final InsertEventParser insertEventParser = new InsertEventParser();

    @Override
    public boolean support(EventType eventType) {
        return EventType.EXT_WRITE_ROWS == eventType;
    }

    @Override
    public InsertMessage process(WriteRowsEventData eventData) {
        // 表结构数据获取
        TableSchema cacheSchema = tableSchemaManager.findSchema(eventData.getTableId());
        // 解析插入的数据事件
        return insertEventParser.parseEvent(eventData, cacheSchema);
    }
}
