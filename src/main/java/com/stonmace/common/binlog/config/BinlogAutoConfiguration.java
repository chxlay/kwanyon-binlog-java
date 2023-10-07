package com.stonmace.common.binlog.config;

import com.stonmace.common.binlog.component.DefBinlogListener;
import com.stonmace.common.binlog.component.TableSchemaManager;
import com.stonmace.common.binlog.core.process.*;
import com.xtechcn.common.binlog.core.process.*;
import com.stonmace.common.binlog.core.publisher.DefaultEventMessagePublisher;
import com.stonmace.common.binlog.core.publisher.DelegatingMessagePublisher;
import com.stonmace.common.binlog.core.publisher.MessagePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @author Alay
 * @date 2022-11-14 13:08
 */

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@ComponentScan(value = {"com.xtechcn.common.binlog.component"})
@EnableConfigurationProperties(IBinlogProperties.class)
public class BinlogAutoConfiguration {

    private final ApplicationContext context;
    private final IBinlogProperties binlogProperties;

    /**
     * binlog 启动器
     */
    @Bean
    @ConditionalOnMissingBean
    public IBinLogRunner binLogRunner(DefBinlogListener eventListener) {
        return new IBinLogRunner(eventListener, binlogProperties);
    }

    @Bean
    public TableSchemaManager tableSchemaManager() {
        return new TableSchemaManager(binlogProperties);
    }

    /**
     * 日志事件消息推送，这个是默认实现，实际使用中请替换，使用 DelegatingMessagePublisher 优势在于可以同时包装多种消息推送事件
     */
    @Bean
    @Order
    @ConditionalOnMissingBean(value = MessagePublisher.class)
    public MessagePublisher messagePublisher() {
        // 默认发布事件
        DefaultEventMessagePublisher defaultPublisher = new DefaultEventMessagePublisher(context);
        // 这里使用了委托者包装处理，这种方式优势在于可以同时多种方式推送事件消息
        return new DelegatingMessagePublisher(defaultPublisher);
    }


    @Bean
    @Order
    @ConditionalOnMissingBean(value = MessagePublisher.class)
    public BinlogEventProcess binlogEventProcess(TableSchemaManager tableSchemaManager) {
        return new DelegatingEventProcess(
                // 启动时 binlog 文件初始化事件
                new EventRotateProcess(),
                // 启动是 MySQL 版本信息事件
                new EventFormatDescProcess(),
                // 修改表结构事件
                new EventQueryProcess(binlogProperties, tableSchemaManager),
                // 表数据增删改 前置事件
                new EventTableMapProcess(binlogProperties, tableSchemaManager),
                // 表数据插入事件
                new EventInsertRowProcess(tableSchemaManager),
                // 表数据删除事件
                new EventDeleteRowProcess(tableSchemaManager),
                // 表数据修改事件
                new EventUpdateRowProcess(tableSchemaManager));
    }

}
