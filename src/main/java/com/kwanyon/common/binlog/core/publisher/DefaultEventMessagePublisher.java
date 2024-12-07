package com.kwanyon.common.binlog.core.publisher;

import com.kwanyon.common.binlog.core.message.BinlogMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;

/**
 * Spring 事件发送实现方案（默认实现方案）
 *
 * @author Alay
 * @since 2022-11-17 20:23
 */
@RequiredArgsConstructor
public class DefaultEventMessagePublisher implements MessagePublisher {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ApplicationContext context;

    @Override
    public void publish(BinlogMessage message) {
        logger.info(message.toString());
        MessageEvent messageEvent = new MessageEvent(message);
        context.publishEvent(messageEvent);
    }


    public static class MessageEvent extends ApplicationEvent {
        public MessageEvent(Object source) {
            super(source);
        }
    }
}
