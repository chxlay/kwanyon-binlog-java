package com.kwanyon.common.binlog.core.table;

import com.kwanyon.common.binlog.config.IBinlogProperties;
import com.kwanyon.common.binlog.model.TableSchema;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.event.EventListener;

import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据中查询表结构
 *
 * @author Alay
 * @since 2023-10-07 11:38
 */
@RequiredArgsConstructor
public class SchemaStatement {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final IBinlogProperties binlogProperties;
    private String url;

    private static final String TABLE_SCHEMA_SQL =
            "SELECT " +
                "`TABLE_SCHEMA`," +
                "`TABLE_NAME`," +
                "`COLUMN_NAME`," +
                "`ORDINAL_POSITION`," +
                "`COLUMN_DEFAULT`," +
                "`IS_NULLABLE`," +
                "`DATA_TYPE`," +
                "`CHARACTER_MAXIMUM_LENGTH`," +
                "`CHARACTER_OCTET_LENGTH`," +
                "`NUMERIC_PRECISION`," +
                "`NUMERIC_SCALE`," +
                "`CHARACTER_SET_NAME`," +
                "`COLLATION_NAME` " +
            "FROM" +
                "`information_schema`.`columns` " +
            "WHERE " +
                "`TABLE_SCHEMA` = ? " +
            "AND `TABLE_NAME` = ?";

    @EventListener(value = WebServerInitializedEvent.class)
    public void initSqlUrl() {
        url = String.format("jdbc:mysql://%s:%s/information_schema", binlogProperties.host(), binlogProperties.port());
    }


    public TableSchema querySchema(String database, String tableName) {
        // 数据库中查询获取
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            connection = DriverManager.getConnection(url, binlogProperties.username(), binlogProperties.password());
            statement = connection.prepareStatement(TABLE_SCHEMA_SQL);
            statement.setString(1, database);
            statement.setString(2, tableName);

            // 执行update语句
            rs = statement.executeQuery();
            TableSchema tableSchema = TableSchema.create(database, tableName);
            while (rs.next()) {
                // 字段创建
                TableSchema.Column column = tableSchema.buildColumn(rs.getString(3));
                column.setOrdinalPosition(rs.getInt(4));
                column.setColumnDefault(rs.getString(5));
                column.setIsNullable(rs.getString(6));
                column.setDataType(rs.getString(7));
                column.setCharacterMaximumLength(rs.getString(8));
                column.setCharacterOctetLength(rs.getString(9));
                column.setNumericPrecision(rs.getString(10));
                column.setNumericScale(rs.getString(11));
                column.setCharacterSetName(rs.getString(12));
                column.setCollationName(rs.getString(13));

                // 每个字段
                tableSchema.addColumn(column);
            }
            // 排序
            List<TableSchema.Column> columns = tableSchema.getColumns().stream().sorted().collect(Collectors.toList());
            // 为了节省存储空间,将字段数据设置为 null
            tableSchema.setColumns(null);

            List<String> columnNames = columns.stream().map(TableSchema.Column::getColumnName).collect(Collectors.toList());
            tableSchema.setColumnNames(columnNames);

            return tableSchema;
        } catch (SQLException e) {
            logger.error("message:{}, case:{}", e.getMessage(), e.getCause());
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                if (null != rs) rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
            try {
                if (null != statement) statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
            try {
                if (null != connection) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}
