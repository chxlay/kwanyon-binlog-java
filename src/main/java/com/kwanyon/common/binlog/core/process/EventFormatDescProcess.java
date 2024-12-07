package com.kwanyon.common.binlog.core.process;

import com.github.shyiko.mysql.binlog.event.EventType;
import com.github.shyiko.mysql.binlog.event.FormatDescriptionEventData;
import com.kwanyon.common.binlog.core.message.BinlogMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 启动时版本信息数据
 * 启动时事件顺序：ROTATE、FORMAT_DESCRIPTION
 *
 * @author Alay
 * @since 2022-11-15 10:53
 */
public class EventFormatDescProcess implements BinlogEventProcess<FormatDescriptionEventData> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final EventType eventType = EventType.FORMAT_DESCRIPTION;

    @Override
    public boolean support(EventType eventType) {
        return this.eventType == eventType;
    }

    @Override
    public BinlogMessage process(FormatDescriptionEventData eventData) {
        logger.info("---------------<<< binlog 启动并连接成功 >>>--------------------");
        logger.info("---------------<<< binlog 版本:{} >>>--------------------", eventData.getBinlogVersion());
        logger.info("---------------<<< MySQL 版本:{} >>>--------------------", eventData.getServerVersion());
        return null;
    }

}
