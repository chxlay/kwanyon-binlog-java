package com.kwanyon.common.binlog.config;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.github.shyiko.mysql.binlog.event.TableMapEventData;
import com.github.shyiko.mysql.binlog.event.deserialization.*;
import com.kwanyon.common.binlog.component.DefBinlogListener;
import com.kwanyon.common.binlog.deserialization.DeleteRowsDeserializer;
import com.kwanyon.common.binlog.deserialization.UpdateRowsDeserializer;
import com.kwanyon.common.binlog.deserialization.WriteRowsDeserializer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Binlog 启动运行
 *
 * @author Alay
 * @since 2022-11-14 13:28
 */
@RequiredArgsConstructor
public class IBinLogRunner implements CommandLineRunner {
    /**
     * Binlog 监听器作为集群的一个从节点角色,必须具备一个服务唯一ID
     */
    private static final long SERVER_ID = 100000L;

    private final DefBinlogListener binlogListener;
    private final IBinlogProperties binlogProperties;


    @Async
    @Override
    public void run(String... args) throws Exception {
        BinaryLogClient client = new BinaryLogClient(binlogProperties.host(), binlogProperties.port(),
                binlogProperties.datebase(), binlogProperties.username(), binlogProperties.password());
        /**
         * Binlog 监听器作为集群的一个从节点角色,必须具备一个服务唯一ID
         */
        client.setServerId(SERVER_ID);

        // 自定义系列化
        EventHeaderV4Deserializer eventHeaderV4Deserializer = new EventHeaderV4Deserializer();
        NullEventDataDeserializer nullEventDataDeserializer = new NullEventDataDeserializer();
        IdentityHashMap<EventType, EventDataDeserializer> eventDataDeserializers = new IdentityHashMap<>();
        Map<Long, TableMapEventData> tableMapEventByTableId = new HashMap<>();
        this.registerDefaultEventDataDeserializers(eventDataDeserializers, tableMapEventByTableId);

        EventDeserializer eventDeserializer = new EventDeserializer(eventHeaderV4Deserializer, nullEventDataDeserializer,
                eventDataDeserializers, tableMapEventByTableId);

        client.setEventDeserializer(eventDeserializer);
        // 监听器
        client.registerEventListener(binlogListener);

        client.connect();
    }

    /**
     * 如果没有这个需求可以忽略这部分
     * 源码中的注册事件类型（这里自定义编写了 bit --> Boolean 的反系列化规则）
     *
     * @param eventDataDeserializers
     * @param tableMapEventByTableId
     * @see com.github.shyiko.mysql.binlog.event.deserialization.EventDeserializer 源码中的注册事件类型
     */
    private void registerDefaultEventDataDeserializers(IdentityHashMap<EventType, EventDataDeserializer> eventDataDeserializers, Map<Long, TableMapEventData> tableMapEventByTableId) {
        // 启动完成后 binlog 日志文件信息事件（日志文件索引，日志文件坐标）
        eventDataDeserializers.put(EventType.ROTATE, new RotateEventDataDeserializer());
        // 启动时连接成功后的版本相关描述信息事件
        eventDataDeserializers.put(EventType.FORMAT_DESCRIPTION, new FormatDescriptionEventDataDeserializer());

        eventDataDeserializers.put(EventType.INTVAR, new IntVarEventDataDeserializer());
        eventDataDeserializers.put(EventType.QUERY, new QueryEventDataDeserializer());
        eventDataDeserializers.put(EventType.TABLE_MAP, new TableMapEventDataDeserializer());
        eventDataDeserializers.put(EventType.XID, new XidEventDataDeserializer());
        eventDataDeserializers.put(EventType.WRITE_ROWS, new WriteRowsEventDataDeserializer(tableMapEventByTableId));
        eventDataDeserializers.put(EventType.UPDATE_ROWS, new UpdateRowsEventDataDeserializer(tableMapEventByTableId));
        eventDataDeserializers.put(EventType.DELETE_ROWS, new DeleteRowsEventDataDeserializer(tableMapEventByTableId));
        // 自定义反系列化类 (读写更新)
        eventDataDeserializers.put(EventType.EXT_WRITE_ROWS, (new WriteRowsDeserializer(tableMapEventByTableId)).setMayContainExtraInformation(true));
        eventDataDeserializers.put(EventType.EXT_UPDATE_ROWS, (new UpdateRowsDeserializer(tableMapEventByTableId)).setMayContainExtraInformation(true));
        eventDataDeserializers.put(EventType.EXT_DELETE_ROWS, (new DeleteRowsDeserializer(tableMapEventByTableId)).setMayContainExtraInformation(true));

        eventDataDeserializers.put(EventType.ROWS_QUERY, new RowsQueryEventDataDeserializer());
        eventDataDeserializers.put(EventType.GTID, new GtidEventDataDeserializer());
        eventDataDeserializers.put(EventType.PREVIOUS_GTIDS, new PreviousGtidSetDeserializer());
        eventDataDeserializers.put(EventType.XA_PREPARE, new XAPrepareEventDataDeserializer());

        eventDataDeserializers.put(EventType.ANNOTATE_ROWS, new AnnotateRowsEventDataDeserializer());
        eventDataDeserializers.put(EventType.MARIADB_GTID, new MariadbGtidEventDataDeserializer());
        eventDataDeserializers.put(EventType.BINLOG_CHECKPOINT, new BinlogCheckpointEventDataDeserializer());
        eventDataDeserializers.put(EventType.MARIADB_GTID_LIST, new MariadbGtidListEventDataDeserializer());
        eventDataDeserializers.put(EventType.TRANSACTION_PAYLOAD, new TransactionPayloadEventDataDeserializer());
    }

}
