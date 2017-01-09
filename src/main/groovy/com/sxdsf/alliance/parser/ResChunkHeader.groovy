package com.sxdsf.alliance.parser

import java.nio.ByteBuffer

/**
 * struct ResChunk_header{// Type identifier for this chunk.  The meaning of this value depends
 * // on the containing chunk.
 * uint16_t type;
 * // Size of the chunk header (in bytes).  Adding this value to
 * // the address of the chunk allows you to find its associated data
 * // (if any).
 * uint16_t headerSize;
 * // Total size of this chunk (in bytes).  This is the chunkSize plus
 * // the size of any data associated with the chunk.  Adding this value
 * // to the chunk allows you to completely skip its contents (including
 * // any child chunks).  If this value is the same as chunkSize, there is
 * // no data associated with the chunk.
 * uint32_t size;};
 *
 * ResChunkHeader
 * @author 孙博闻
 * @date 2016/12/14 上午10:41
 * @desc 每一个Chunk的Header结构
 */
class ResChunkHeader implements Resource {

    short type
    short headerSize
    int size

    static final ResChunkHeader parse(byte[] bytes) {
        if (!bytes) {
            return null
        }

        //每一个属性要小端编码，例如：
        //type 有2个字节，解出来的是0200，最终的值应该是0002 = 2
        return new ResChunkHeader(type: Utils.bytes2Short(Utils.subBytes(bytes, 0, 2)),
                headerSize: Utils.bytes2Short(Utils.subBytes(bytes, 2, 2)),
                size: Utils.bytes2Int(Utils.subBytes(bytes, 4, 4)))
    }

    @Override
    byte[] bytes() {
        return ByteBuffer.allocate(length()).
                put(Utils.short2Bytes(type)).
                put(Utils.short2Bytes(headerSize)).
                put(Utils.int2Bytes(size)).
                array()
    }

    @Override
    int length() {
        return 2 + 2 + 4
    }

    @Override
    String toString() {
        String tText = "type:${type} headerSize:${headerSize} size:${size}"
        return tText
    }
}
