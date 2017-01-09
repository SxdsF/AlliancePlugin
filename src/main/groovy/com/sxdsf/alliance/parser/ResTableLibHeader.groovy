package com.sxdsf.alliance.parser

import java.nio.ByteBuffer

/**
 * struct ResTable_lib_header{struct ResChunk_header header;

 // The number of shared libraries linked in this resource table.
 uint32_t count;};
 *
 * ResTableLibHeader
 * @author 孙博闻
 * @date 2016/12/27 上午10:37
 * @desc 标识此arsc引用的lib
 */
class ResTableLibHeader implements Resource {

    ResChunkHeader header

    // The number of shared libraries linked in this resource table.
    int count

    static final ResTableLibHeader parse(byte[] bytes) {
        if (!bytes) {
            return null
        }

        ResTableLibHeader tResTableLibHeader = new ResTableLibHeader()
        tResTableLibHeader.header = ResChunkHeader.parse(Utils.subBytes(bytes, 0))
        tResTableLibHeader.count = Utils.bytes2Int(Utils.subBytes(bytes, tResTableLibHeader.header.length(), 4))
        return tResTableLibHeader
    }

    @Override
    byte[] bytes() {
        return ByteBuffer.allocate(length()).put(header.bytes()).put(Utils.ints2Bytes(count)).array()
    }

    @Override
    int length() {
        return header.length() + 4
    }
}
