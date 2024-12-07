package com.kwanyon.common.binlog.core.parser;

import com.github.shyiko.mysql.binlog.event.QueryEventData;
import com.kwanyon.common.binlog.core.message.RenameMessage;

/**
 * 重命名表事件 消息解析
 *
 * @author Alay
 * @since 2022-11-14 17:17
 */
public class RenameEventParser implements BinlogEventParser<QueryEventData, RenameMessage> {
    private static final String ACTION = "RENAME";


    @Override
    public RenameMessage parseEvent(QueryEventData event, Object... args) {
        // DROP TABLE IF EXISTS `t_student`
        String[] tableNameArr = this.parseTableName(event.getSql());
        // 暂时没有过多的数据封装
        RenameMessage renameMessage = RenameMessage.create()
                .beforeTable(tableNameArr[0])
                .afterTable(tableNameArr[1]);

        renameMessage.sql(event.getSql())
                .schema(event.getDatabase())
                .tableName(tableNameArr[1])
                .action(ACTION);

        return renameMessage;
    }


    private String[] parseTableName(String sql) {
        // RENAME TABLE `database`.`table_name` TO `database`.`new_table_name`
        sql = sql.replaceAll("`", "");

        String[] tableNameArr = new String[2];

        // 去除 RENAME TABLE
        String str = sql.substring(13);
        String schema = str.substring(0, str.indexOf(" "));
        String beforeName = schema.substring(schema.indexOf('.') + 1);
        tableNameArr[0] = beforeName;

        // 新的表名
        String afterName = str.substring(str.lastIndexOf(".") + 1);
        tableNameArr[1] = afterName;

        return tableNameArr;
    }

}
