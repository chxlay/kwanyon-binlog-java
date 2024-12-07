package com.kwanyon.common.binlog.component;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.Event;
import com.kwanyon.common.binlog.core.message.BinlogMessage;
import com.kwanyon.common.binlog.core.process.BinlogEventProcess;
import com.kwanyon.common.binlog.core.publisher.MessagePublisher;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

/**
 * Binlog 默认监听器
 *
 * @author Alay
 * @since 2022-11-14 13:30
 */
@Component
public class DefBinlogListener implements BinaryLogClient.EventListener {

    private final BinlogEventProcess eventProcess;
    private final MessagePublisher messagePublisher;

    public DefBinlogListener(ObjectProvider<MessagePublisher> publisherProvider, ObjectProvider<BinlogEventProcess> eventProcessProvider) {
        this.messagePublisher = publisherProvider.getIfAvailable();
        this.eventProcess = eventProcessProvider.getIfAvailable();
    }

    /**
     * 一次MySQL的修改、插入、删除,会触发多次事件,会调用方法多次,注意处理好逻辑优化性能
     * 启动时事件顺序：ROTATE、FORMAT_DESCRIPTION
     * 启动后事件触发顺序：
     * 1、每次操作只触发一次(批量操作也只会触发一次)：ANONYMOUS_GTID,QUERY,
     * 2、每条记录row 触发一次独立的事件：TABLE_MAP(携带表库字段信息)批量也会触发多次
     * 3、多选一具体事件：EXT_UPDATE_ROWS（执行更新）,EXT_DELETE_ROWS，EXT_WRITE_ROWS
     * 4、每次操作只触发一次(批量操作也只会触发一次) XID
     *
     * @param event
     */
    @Override
    public void onEvent(Event event) {
        // Binlog 事件处理
        BinlogMessage eventMessage = eventProcess.process(event);

        // 没有需要处理发布的消息
        if (null == eventMessage) return;

        // 解析得到的事件消息进行发布
        messagePublisher.publish(eventMessage);
    }


}
