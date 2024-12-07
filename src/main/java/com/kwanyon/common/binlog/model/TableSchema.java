package com.kwanyon.common.binlog.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库表结构实体对象
 *
 * @author Alay
 * @since 2022-11-14 19:20
 */
@Getter
@Setter
public class TableSchema implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 给binlog 使用的
     */
    private Long tableId;
    /**
     * 数据库名称
     */
    private String tableSchema;
    /**
     * 数据表名
     */
    private String tableName;
    /**
     * 字段
     */
    private List<Column> columns;
    /**
     * 字段名称
     */
    private List<String> columnNames;

    @Getter
    @Setter
    public static class Column implements Comparable<Column> {
        /**
         * 字段名
         */
        private String columnName;
        /**
         * 表结构字段排序 1,2,3, ....
         */
        private Integer ordinalPosition;
        /**
         * 默认值
         */
        private String columnDefault;
        /**
         * 是否可以为 null （YES/NO）
         */
        private String isNullable;
        /**
         * 数据类型 （varchar / int / date ....）
         */
        private String dataType;
        /**
         * 数据类型长度
         */
        private String characterMaximumLength;
        /**
         * 以字节为单位的最大长度
         */
        private String characterOctetLength;

        private String numericPrecision;
        private String numericScale;
        /**
         * 字符集 utf8
         */
        private String characterSetName;
        /**
         * utf8_general_ci
         */
        private String collationName;

        @Override
        public int compareTo(Column o) {
            return Integer.compare(this.ordinalPosition, o.ordinalPosition);
        }
    }


    public static TableSchema create(String dbname, String tableName) {
        TableSchema schema = new TableSchema();
        schema.tableSchema = dbname;
        schema.tableName = tableName;
        schema.columns = new ArrayList<>();
        schema.columnNames = new ArrayList<>();
        return schema;
    }


    public Column buildColumn(String columnName) {
        Column column = new Column();
        column.columnName = columnName;
        return column;
    }


    public void addColumn(Column column) {
        this.columns.add(column);
    }

}
