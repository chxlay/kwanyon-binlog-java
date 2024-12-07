package com.kwanyon.common.binlog.core.parser;

import com.github.shyiko.mysql.binlog.event.QueryEventData;
import com.kwanyon.common.binlog.core.message.BinlogMessage;

/**
 * 修改消息解析器
 *
 * @author Alay
 * @since 2022-11-14 17:02
 */
public class AlterEventParser implements BinlogEventParser<QueryEventData, BinlogMessage> {
    private static final String ACTION = "ALTER";


    @Override
    public BinlogMessage parseEvent(QueryEventData event, Object... args) {
        /**
         * 修改表结构的事件 ( db_name.table_name )
         * ALTER TABLE kwanyon_office.t_student ADD COLUMN `xxx`（这个是字段名称） varchar(255) NULL AFTER birthday
         * ALTER TABLE kwanyon_office.t_student MODIFY COLUMN `xxxx`
         * ALTER TABLE kwanyon_office.t_student DROP COLUMN `xxx` （这个是字段名称）
         */
        String tableName = this.parseTableName(event.getSql());

        return BinlogMessage.create()
                .schema(event.getDatabase())
                .tableName(tableName)
                .sql(event.getSql()).action(ACTION);
    }


    private String parseTableName(String sql) {
        sql = sql.replaceAll("`", "");
        /**
         * 修改表结构的事件 ( db_name.table_name )
         * ALTER TABLE kwanyon_office.t_student ADD COLUMN `xxx`（这个是字段名称） varchar(255) NULL AFTER birthday
         * ALTER TABLE kwanyon_office.t_student MODIFY COLUMN `xxxx`
         * ALTER TABLE kwanyon_office.t_student DROP COLUMN `xxx` （这个是字段名称）
         */
        String str = sql.substring(sql.indexOf("."));
        String tableName = str.substring(1,str.indexOf(" "));
        return tableName;
    }
}
