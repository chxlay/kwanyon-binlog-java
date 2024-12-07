package com.kwanyon.common.binlog.core.process;

import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.kwanyon.common.binlog.core.message.DeleteMessage;
import com.kwanyon.common.binlog.core.parser.DeleteEventParser;
import com.kwanyon.common.binlog.core.table.TableSchemaManager;
import com.kwanyon.common.binlog.model.TableSchema;
import lombok.RequiredArgsConstructor;

/**
 * @author Alay
 * @since 2022-11-15 11:38
 */
@RequiredArgsConstructor
public class EventDeleteRowProcess implements BinlogEventProcess<DeleteRowsEventData> {

    private final TableSchemaManager tableSchemaManager;
    private final DeleteEventParser deleteEventParser = new DeleteEventParser();

    @Override
    public boolean support(EventType eventType) {
        return EventType.EXT_DELETE_ROWS == eventType;
    }

    @Override
    public DeleteMessage process(DeleteRowsEventData eventData) {
        long tableId = eventData.getTableId();
        // 表结构数据
        TableSchema cacheSchema = tableSchemaManager.findSchema(tableId);
        // 发送解析后的消息数据
        return deleteEventParser.parseEvent(eventData, cacheSchema);
    }

}
