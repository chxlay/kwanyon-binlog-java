package com.kwanyon.common.binlog.config;

import com.kwanyon.common.binlog.component.DefBinlogListener;
import com.kwanyon.common.binlog.core.process.*;
import com.kwanyon.common.binlog.core.process.*;
import com.kwanyon.common.binlog.core.publisher.DefaultEventMessagePublisher;
import com.kwanyon.common.binlog.core.publisher.DelegatingMessagePublisher;
import com.kwanyon.common.binlog.core.publisher.MessagePublisher;
import com.kwanyon.common.binlog.core.table.InMemorySchemaManager;
import com.kwanyon.common.binlog.core.table.InRedisSchemaManage;
import com.kwanyon.common.binlog.core.table.SchemaStatement;
import com.kwanyon.common.binlog.core.table.TableSchemaManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 自动配置类
 *
 * @author Alay
 * @since 2022-11-14 13:08
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@ComponentScan(value = {"com.kwanyon.common.binlog.component"})
@EnableConfigurationProperties(IBinlogProperties.class)
public class BinlogAutoConfiguration {

    private final ApplicationContext context;
    private final IBinlogProperties binlogProperties;

    /**
     * binlog 启动器
     *
     * @param eventListener
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public IBinLogRunner binLogRunner(DefBinlogListener eventListener) {
        return new IBinLogRunner(eventListener, binlogProperties);
    }


    @Bean
    public SchemaStatement schemaStatement(IBinlogProperties binlogProperties) {
        return new SchemaStatement(binlogProperties);
    }


    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(value = {RedisTemplate.class})
    public TableSchemaManager tableSchemaManager(RedisTemplate redisTemplate, CacheKeyPrefix cacheKeyPrefix, SchemaStatement schemaStatement) {
        return new InRedisSchemaManage(redisTemplate, cacheKeyPrefix, schemaStatement);
    }

    @Bean
    @Order
    @ConditionalOnMissingBean
    public TableSchemaManager tableSchemaManager(SchemaStatement schemaStatement) {
        return new InMemorySchemaManager(schemaStatement);
    }


    /**
     * 日志事件消息推送，这个是默认实现，实际使用中请替换，使用 DelegatingMessagePublisher 优势在于可以同时包装多种消息推送事件
     */
    @Bean
    @ConditionalOnMissingBean
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
