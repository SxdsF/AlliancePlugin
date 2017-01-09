package com.sxdsf.alliance.parser

import java.nio.ByteBuffer

/**
 *
 * struct ResTable_typeSpec
 *{struct ResChunk_header header;
 * // The type identifier this chunk is holding.  Type IDs start
 * // at 1 (corresponding to the value of the type bits in a
 * // resource identifier).  0 is invalid.
 * uint8_t id;
 * // Must be 0.
 * uint8_t res0;
 * // Must be 0.
 * uint16_t res1;
 * // Number of uint32_t entry configuration masks that follow.
 * uint32_t entryCount;
 * enum {* // Additional flag indicating an entry is public.
 * SPEC_PUBLIC = 0x40000000
 *};
 *};
 *
 * ResTableTypeSpec
 * @author 孙博闻
 * @date 2016/12/16 上午10:24
 * @desc 类型规范
 */
class ResTableTypeSpec implements Resource {

    static final def SPEC_PUBLIC = 0x40000000

    ResChunkHeader header

    // The type identifier this chunk is holding.  Type IDs start
    // at 1 (corresponding to the value of the type bits in a
    // resource identifier).  0 is invalid.
    byte id

    // Must be 0.
    byte res0
    // Must be 0.
    short res1

    // Number of uint32_t entry configuration masks that follow.
    int entryCount

    static final ResTableTypeSpec parse(byte[] bytes) {
        if (!bytes) {
            return null
        }

        ResTableTypeSpec tResTableTypeSpec = new ResTableTypeSpec()
        tResTableTypeSpec.header = ResChunkHeader.parse(Utils.subBytes(bytes, 0))
        tResTableTypeSpec.id = Utils.subBytes(bytes, tResTableTypeSpec.header.length(), 1)[0]
        tResTableTypeSpec.res0 = Utils.subBytes(bytes, tResTableTypeSpec.header.length() + 1, 1)[0]
        tResTableTypeSpec.res1 = Utils.bytes2Short(Utils.subBytes(bytes, tResTableTypeSpec.header.length() + 1 + 1, 2))
        tResTableTypeSpec.entryCount = Utils.bytes2Int(Utils.subBytes(bytes, tResTableTypeSpec.header.length() + 1 + 1 + 2, 4))
        return tResTableTypeSpec
    }

    @Override
    byte[] bytes() {
        return ByteBuffer.allocate(length()).
                put(header.bytes()).
                put(id).
                put(res0).
                put(Utils.short2Bytes(res1)).
                put(Utils.int2Bytes(entryCount)).
                array()
    }

    @Override
    int length() {
        return header.length() + 1 + 1 + 2 + 4
    }
}
