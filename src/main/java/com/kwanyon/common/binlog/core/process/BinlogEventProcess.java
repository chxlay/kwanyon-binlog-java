package com.kwanyon.common.binlog.core.process;

import com.github.shyiko.mysql.binlog.event.EventType;
import com.kwanyon.common.binlog.core.message.BinlogMessage;
import org.springframework.core.Ordered;

import java.util.Comparator;

/**
 * 事件处理器
 *
 * @author Alay
 * @since 2022-11-15 11:31
 */
public interface BinlogEventProcess<T> extends Comparator<Ordered>, Ordered{
    /**
     * 是否支持的类型
     *
     * @param eventType
     * @return
     */
    boolean support(EventType eventType);

    /**
     * 执行binlog 事件处理
     *
     * @param event
     * @param <M>
     * @return
     */
    <M extends BinlogMessage> M process(T event);


    /**
     * 排序比较
     *
     * @return
     */
    @Override
    default int compare(Ordered p1, Ordered p2) {
        return Integer.compare(p1.getOrder(), p2.getOrder());
    }

    /**
     * 排序值
     *
     * @return
     */
    @Override
    default int getOrder() {
        return Integer.MAX_VALUE;
    }

}
