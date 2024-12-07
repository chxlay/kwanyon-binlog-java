package com.kwanyon.common.binlog.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 连接监听的是MySQL 的主服务器
 *
 * @author Alay
 * @since 2022-11-14 13:06
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "kwanyon.binlog")
public class IBinlogProperties {
    /**
     * Master 主机连接地址
     */
    private String host;
    /**
     * 数据库名称
     */
    private String datebase;
    /**
     * 端口号
     */
    private int port = 3306;
    /**
     * 连接用户名
     */
    private String username;
    /**
     * 连接密码
     */
    private String password;

    /**
     * binlog 需要接收处理的 表名
     */
    private List<String> tables = new ArrayList<>();


    public String host() {
        return host;
    }

    public String datebase() {
        return datebase;
    }


    public int port() {
        return port;
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }

    public List<String> tables() {
        return tables;
    }
}
