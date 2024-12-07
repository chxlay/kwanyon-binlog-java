package com.kwanyon.common.binlog.core.parser;

import com.github.shyiko.mysql.binlog.event.QueryEventData;
import com.kwanyon.common.binlog.core.message.BinlogMessage;

/**
 * 删除表事件 消息解析
 *
 * @author Alay
 * @since 2022-11-14 17:17
 */
public class DropEventParser implements BinlogEventParser<QueryEventData, BinlogMessage> {
    private static final String ACTION = "DROP";

    @Override
    public BinlogMessage parseEvent(QueryEventData event, Object... args) {
        // DROP TABLE IF EXISTS `t_student`
        String tableName = this.parseTableName(event.getSql());
        // 暂时没有过多的数据封装
        return BinlogMessage.create()
                .sql(event.getSql())
                .schema(event.getDatabase())
                .tableName(tableName)
                .action(ACTION);
    }


    private String parseTableName(String sql) {
        // DROP TABLE IF EXISTS `t_student`
        sql = sql.replaceAll("`", "");

        String tableName = sql.substring(sql.lastIndexOf(" ") + 1);
        return tableName;
    }

}
