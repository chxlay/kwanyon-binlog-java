package com.kwanyon.common.binlog.core.publisher;


import com.kwanyon.common.binlog.core.message.BinlogMessage;

/**
 * RabbitMQ 消息队列发送实现方案
 * 注入时 请使用注解 @Primary ,使其优先于 EventMessageSender
 *
 * @author Alay
 * @since 2022-11-17 21:55
 */
public class RabbitMessagePublisher implements MessagePublisher {
    @Override
    public void publish(BinlogMessage message) {
        // 这里只是示例,使用者请自行去实现,注入时 请使用注解 @Primary ,使其优先于 EventMessageSender
    }
}
