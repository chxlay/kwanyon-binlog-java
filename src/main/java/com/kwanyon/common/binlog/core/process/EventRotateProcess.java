package com.kwanyon.common.binlog.core.process;

import com.github.shyiko.mysql.binlog.event.EventType;
import com.github.shyiko.mysql.binlog.event.RotateEventData;
import com.kwanyon.common.binlog.core.message.BinlogMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 启动时事件顺序：ROTATE、FORMAT_DESCRIPTION
 *
 * @author Alay
 * @since 2022-11-15 10:51
 */
public class EventRotateProcess implements BinlogEventProcess<RotateEventData> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean support(EventType eventType) {
        return EventType.ROTATE == eventType;
    }

    @Override
    public BinlogMessage process(RotateEventData eventData) {
        logger.info("---------------<<< 监听的 binlog 文件名:{} >>>--------------------", eventData.getBinlogFilename());
        logger.info("---------------<<< binlog 坐标 :{} >>>--------------------", eventData.getBinlogPosition());
        return null;
    }

}
