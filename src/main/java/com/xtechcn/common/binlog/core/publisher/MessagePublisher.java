package com.xtechcn.common.binlog.core.publisher;

import com.xtechcn.common.binlog.core.message.BinlogMessage;

/**
 * 日志消息发送器(使用什么方式发送可自行实现 kafka 等)
 *
 * @author Alay
 * @date 2022-11-17 20:21
 */
public interface MessagePublisher {
    /**
     * 日志消息发送器
     *
     * @param message
     */
    void publish(BinlogMessage message);

}
