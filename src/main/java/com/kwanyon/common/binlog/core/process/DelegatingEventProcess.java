package com.kwanyon.common.binlog.core.process;

import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.kwanyon.common.binlog.core.message.BinlogMessage;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Alay
 * @since 2023-05-25 12:36
 */
public class DelegatingEventProcess implements BinlogEventProcess<Event> {

    private final List<BinlogEventProcess> eventProcesses;
    private final Comparator<BinlogEventProcess> comparator = Comparator.comparingInt(BinlogEventProcess::getOrder);


    public DelegatingEventProcess(BinlogEventProcess... processes) {
        Assert.notEmpty(processes, "processes cannot be empty");
        this.eventProcesses = new ArrayList<>(processes.length);
        for (BinlogEventProcess process : processes) {
            eventProcesses.add(process);
        }
        this.eventProcesses.sort(comparator);
    }

    @Override
    public boolean support(EventType eventType) {
        return false;
    }

    @Override
    public BinlogMessage process(Event event) {
        EventData eventData = event.getData();
        // 无需处理
        if (null == eventData) return null;

        // 事件类型
        EventType eventType = event.getHeader().getEventType();
        for (BinlogEventProcess process : eventProcesses) {
            if (process.support(eventType)) {
                // 执行后的消息
                BinlogMessage message = process.process(eventData);
                if (null != message) return message;
            }
        }
        return null;
    }

    public void addProcess(BinlogEventProcess... processes) {
        Assert.notEmpty(processes, "processes cannot be empty");
        List<BinlogEventProcess> eventProcesses = new ArrayList<>(processes.length);
        for (BinlogEventProcess process : processes) {
            eventProcesses.add(process);
        }
        this.eventProcesses.addAll(eventProcesses);
        // 排序
        this.eventProcesses.sort(comparator);
    }

}
