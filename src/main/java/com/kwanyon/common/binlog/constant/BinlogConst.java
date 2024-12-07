package com.kwanyon.common.binlog.constant;

/**
 * 常量
 *
 * @author Alay
 * @since 2022-11-14 15:47
 */
public interface BinlogConst {
    /**
     * INSERT、UPDATE、DELETE 的事件的 SQL
     */
    String EVENT_SQL_BEGIN = "BEGIN";
    /**
     * 创建表
     */
    String EVENT_SQL_CREATE = "CREATE";
    /**
     * 修改表
     */
    String EVENT_SQL_ALTER = "ALTER";
    /**
     * 删除表
     */
    String EVENT_SQL_DROP = "DROP";
    /**
     * 重命名 RENAME TABLE `database`.`table_name` TO `database`.`new_table_name`
     */
    String EVENT_SQL_RENAME = "RENAME";

}
