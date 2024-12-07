package com.kwanyon.common.binlog.core.publisher;

import com.kwanyon.common.binlog.core.message.BinlogMessage;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息发送(如果想同时使用多种消息发送器，进行消息推送，请注入此对象进行包装委托处理)
 *
 * @author Alay
 * @since 2023-05-25 13:07
 */
public class DelegatingMessagePublisher implements MessagePublisher {

    private final List<MessagePublisher> messagePublishers;


    public DelegatingMessagePublisher(MessagePublisher... messagePublishers) {
        Assert.notEmpty(messagePublishers, "messagePublishers cannot be empty");
        this.messagePublishers = new ArrayList<>(messagePublishers.length);
        for (MessagePublisher messagePublisher : messagePublishers) {
            this.messagePublishers.add(messagePublisher);
        }
    }

    @Override
    public void publish(BinlogMessage message) {
        for (MessagePublisher publisher : messagePublishers) {
            publisher.publish(message);
        }
    }


    public void addPublisher(MessagePublisher... publishers) {
        Assert.notNull(publishers, "publisher cannot be empty");
        List<MessagePublisher> addPublishers = new ArrayList<>(publishers.length);
        this.messagePublishers.addAll(addPublishers);
    }

}
