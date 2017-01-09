package com.sxdsf.alliance.parser

import java.nio.ByteBuffer

/**
 * struct ResTable_header
 *{struct ResChunk_header header;
 * // The number of ResTable_package structures.
 * uint32_t packageCount;
 *};
 *
 * ResTableHeader
 * @author 孙博闻
 * @date 2016/12/14 上午10:48
 * @desc 资源索引表header结构
 */
class ResTableHeader implements Resource {

    ResChunkHeader header
    int packageCount

    static final ResTableHeader parse(byte[] bytes) {
        if (!bytes) {
            return null
        }

        ResTableHeader tResTableHeader = new ResTableHeader()
        tResTableHeader.header = ResChunkHeader.parse(Utils.subBytes(bytes, 0))
        tResTableHeader.packageCount = Utils.bytes2Int(Utils.subBytes(bytes, tResTableHeader.header.length(), 4))
        return tResTableHeader
    }

    @Override
    byte[] bytes() {
        return ByteBuffer.allocate(length()).put(header.bytes()).put(Utils.int2Bytes(packageCount)).array()
    }

    @Override
    int length() {
        return header.length() + 4
    }

    @Override
    String toString() {
        String tText = "header->${header.toString()}\r\npackageCount:${packageCount}"
        return tText
    }
}
