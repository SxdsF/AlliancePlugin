package com.sxdsf.alliance.parser

import java.nio.ByteBuffer

/**
 * struct ResTable_type
 *{ struct ResChunk_header header;
 * enum {* NO_ENTRY = 0xFFFFFFFF
 *};
 * // The type identifier this chunk is holding.  Type IDs start
 * // at 1 (corresponding to the value of the type bits in a
 * // resource identifier).  0 is invalid.
 * uint8_t id;
 * // Must be 0.
 * uint8_t res0;
 * // Must be 0.
 * uint16_t res1;
 * // Number of uint32_t entry indices that follow.
 * uint32_t entryCount;
 * // Offset from header where ResTable_entry data starts.
 * uint32_t entriesStart;
 * // Configuration this collection of entries is designed for.
 * ResTable_config config;
 *};
 *
 * ResTableType
 * @author 孙博闻
 * @date 2016/12/16 上午10:35
 * @desc 资源类型项
 */
class ResTableType implements Resource {

    static final def NO_ENTRY = 0xFFFFFFFF

    ResChunkHeader header

    // The type identifier this chunk is holding.  Type IDs start
    // at 1 (corresponding to the value of the type bits in a
    // resource identifier).  0 is invalid.
    byte id

    // Must be 0.
    byte res0
    // Must be 0.
    short res1

    // Number of uint32_t entry indices that follow.
    int entryCount

    // Offset from header where ResTable_entry data starts.
    int entriesStart

    // Configuration this collection of entries is designed for.
    ResTableConfig config

    static final ResTableType parse(byte[] bytes) {
        if (!bytes) {
            return null
        }

        ResTableType tResTableType = new ResTableType()
        tResTableType.header = ResChunkHeader.parse(Utils.subBytes(bytes, 0))
        tResTableType.id = Utils.subBytes(bytes, tResTableType.header.length(), 1)[0]
        tResTableType.res0 = Utils.subBytes(bytes, tResTableType.header.length() + 1, 1)[0]
        tResTableType.res1 = Utils.bytes2Short(Utils.subBytes(bytes, tResTableType.header.length() + 1 + 1, 2))
        tResTableType.entryCount = Utils.bytes2Int(Utils.subBytes(bytes, tResTableType.header.length() + 1 + 1 + 2, 4))
        tResTableType.entriesStart = Utils.bytes2Int(Utils.subBytes(bytes, tResTableType.header.length() + 1 + 1 + 2 + 4, 4))
        tResTableType.config = ResTableConfig.parse(Utils.subBytes(bytes, tResTableType.header.length() + 1 + 1 + 2 + 4 + 4))
        return tResTableType
    }

    @Override
    byte[] bytes() {
        return ByteBuffer.allocate(length()).
                put(header.bytes()).
                put(id).
                put(res0).
                put(Utils.short2Bytes(res1)).
                put(Utils.int2Bytes(entryCount)).
                put(Utils.int2Bytes(entriesStart)).
                put(config.bytes()).
                array()
    }

    @Override
    int length() {
        return header.headerSize
    }
}
