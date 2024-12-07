package com.kwanyon.common.binlog.deserialization;

import com.github.shyiko.mysql.binlog.event.TableMapEventData;
import com.github.shyiko.mysql.binlog.event.deserialization.DeleteRowsEventDataDeserializer;
import com.github.shyiko.mysql.binlog.io.ByteArrayInputStream;

import java.io.IOException;
import java.io.Serializable;
import java.util.BitSet;
import java.util.Map;

/**
 * Java类BitSet 自定义系列化规则
 *
 * @author Alay
 * @since 2022-11-14 13:51
 */
public class DeleteRowsDeserializer extends DeleteRowsEventDataDeserializer {


    public DeleteRowsDeserializer(Map<Long, TableMapEventData> tableMapEventByTableId) {
        super(tableMapEventByTableId);
        this.setMayContainExtraInformation(true);
    }

    /**
     * 自定义系列换中 数据库 Bit 字段转为 Boolean 类型
     *
     * @param meta
     * @param inputStream
     * @return
     * @throws IOException
     */
    @Override
    protected Serializable deserializeBit(int meta, ByteArrayInputStream inputStream) throws IOException {
        int bitSetLength = (meta >> 8) * 8 + (meta & 0xFF);
        BitSet bitSet = inputStream.readBitSet(bitSetLength, false);
        int cardinality = bitSet.cardinality();
        return cardinality == 1;
    }
}
