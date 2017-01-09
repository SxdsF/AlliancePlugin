package com.sxdsf.alliance.parser

import java.nio.ByteBuffer

/**
 *
 * struct ResStringPool_header{struct ResChunk_header header;
 * // Number of strings in this pool (number of uint32_t indices that follow
 * // in the data).
 * uint32_t stringCount;
 * // Number of style span arrays in the pool (number of uint32_t indices
 * // follow the string indices).
 * uint32_t styleCount;
 * // Flags.
 * enum {// If set, the string index is sorted by the string values (based
 * // on strcmp16()).
 * SORTED_FLAG = 1<<0,
 * // String pool is encoded in UTF-8
 * UTF8_FLAG = 1<<8};
 * uint32_t flags;
 * // Index from header of the string data.
 * uint32_t stringsStart;
 * // Index from header of the style data.
 * uint32_t stylesStart;};
 *
 * ResStringPoolHeader
 * @author 孙博闻
 * @date 2016/12/14 上午11:01
 * @desc 字符串资源池的header结构
 */
class ResStringPoolHeader implements Resource {

    /**
     * Flags
     */
    private static final int SORTED_FLAG = 1 << 0
    private static final int UTF8_FLAG = 1 << 8

    ResChunkHeader header
    int stringCount
    int styleCount
    int flags
    int stringsStart
    int stylesStart

    static final ResStringPoolHeader parse(byte[] bytes) {
        if (!bytes) {
            return null
        }

        ResStringPoolHeader tResStringPoolHeader = new ResStringPoolHeader()
        tResStringPoolHeader.header = ResChunkHeader.parse(Utils.subBytes(bytes, 0))
        tResStringPoolHeader.stringCount = Utils.bytes2Int(Utils.subBytes(bytes, tResStringPoolHeader.header.length(), 4))
        tResStringPoolHeader.styleCount = Utils.bytes2Int(Utils.subBytes(bytes, tResStringPoolHeader.header.length() + 4, 4))
        tResStringPoolHeader.flags = Utils.bytes2Int(Utils.subBytes(bytes, tResStringPoolHeader.header.length() + 4 + 4, 4))
        tResStringPoolHeader.stringsStart = Utils.bytes2Int(Utils.subBytes(bytes, tResStringPoolHeader.header.length() + 4 + 4 + 4, 4))
        tResStringPoolHeader.stylesStart = Utils.bytes2Int(Utils.subBytes(bytes, tResStringPoolHeader.header.length() + 4 + 4 + 4 + 4, 4))
        return tResStringPoolHeader
    }

    @Override
    byte[] bytes() {
        return ByteBuffer.allocate(length()).
                put(header.bytes()).
                put(Utils.int2Bytes(stringCount)).
                put(Utils.int2Bytes(styleCount)).
                put(Utils.int2Bytes(flags)).
                put(Utils.int2Bytes(stringsStart)).
                put(Utils.int2Bytes(stylesStart)).
                array()
    }

    @Override
    int length() {
        return header.length() + 4 + 4 + 4 + 4 + 4
    }

    @Override
    String toString() {
        String tText = "header->${header.toString()}\r\n" +
                "stringCount:${stringCount}\r\n" +
                "styleCount${styleCount}\r\n" +
                "flags:${flags == UTF8_FLAG ? 'UTF-8' : 'SORTED'}\r\n" +
                "stringsStart:${stringsStart}\r\n" +
                "stylesStart:${stylesStart}"
        return tText
    }
}
