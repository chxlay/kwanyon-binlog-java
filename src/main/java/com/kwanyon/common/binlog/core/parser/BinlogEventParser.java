package com.kwanyon.common.binlog.core.parser;

import com.github.shyiko.mysql.binlog.event.EventData;
import com.kwanyon.common.binlog.core.message.BinlogMessage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Event 解析器
 *
 * @author Alay
 * @since 2022-11-14 15:23
 */
public interface BinlogEventParser<E extends EventData, M extends BinlogMessage> {

    /**
     * 解析 Binlog 事件
     *
     * @param event Binlog 事件数据
     * @param args  其他参数数据(可能为null,只是部分事件解析器需要使用)
     * @return
     */
    M parseEvent(E event, Object... args);

    /**
     * 解析表数据Map类型 （使用于 新增和删除,单数据记录，修改则是双数据记录(修改前，修改后),不适用）
     *
     * @param columnNames
     * @param newRows
     * @return
     */
    default Map<String, Serializable> parseTableData2Map(List<String> columnNames, List<Serializable[]> newRows) {
        Map<String, Serializable> data = new HashMap<>();
        Serializable[] valueArr = newRows.get(0);
        for (int i = 0; i < columnNames.size(); i++) {
            // 字段名
            String columName = columnNames.get(i);
            // 字段对应的值
            data.put(columName, valueArr[i]);
        }
        return data;
    }
}
