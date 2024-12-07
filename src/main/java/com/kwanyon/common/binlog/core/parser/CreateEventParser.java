package com.kwanyon.common.binlog.core.parser;

import com.github.shyiko.mysql.binlog.event.QueryEventData;
import com.kwanyon.common.binlog.core.message.BinlogMessage;

/**
 * 创建表事件消息解析
 *
 * @author Alay
 * @since 2022-11-14 17:16
 */
public class CreateEventParser implements BinlogEventParser<QueryEventData, BinlogMessage> {
    private static final String ACTION = "CREATE";


    @Override
    public BinlogMessage parseEvent(QueryEventData event, Object... args) {
        // 解析表名
        String tableName = this.parseTableName(event.getSql());
        // 暂时没有过多的数据封装
        return BinlogMessage.create()
                .sql(event.getSql())
                .schema(event.getDatabase())
                .tableName(tableName)
                .action(ACTION);
    }

    /**
     * CREATE TABLE `t_student` ( `id` INT, `name` VARCHAR ( 20 ), `sex` VARCHAR ( 2 ), `password` VARCHAR ( 32 ), `birthday` DATETIME )
     *
     * @param sql
     * @return
     */
    private String parseTableName(String sql) {
        // 去除 符号 `
        sql = sql.replaceAll("`", "");
        // 去除 CREATE TABLE
        String str = sql.substring(13);
        // 得到表名
        String tableName = str.substring(0, str.indexOf(" "));

        return tableName;
    }

}
