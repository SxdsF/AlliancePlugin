package com.sxdsf.alliance.parser

import java.nio.ByteBuffer

/**
 *
 * struct ResStringPool_span{enum {END = 0xFFFFFFFF};
 *
 * // This is the name of the span -- that is, the name of the XML
 * // tag that defined it.  The special value END (0xFFFFFFFF) indicates
 * // the end of an array of spans.
 * ResStringPool_ref name;
 * // The range of characters in the string that this span applies to.
 * uint32_t firstChar, lastChar;};
 *
 * ResStringPoolSpan
 * @author 孙博闻
 * @date 2016/12/14 下午11:54
 * @desc 文件描述
 */
class ResStringPoolSpan implements Resource {

    static final int END = 0xFFFFFFFF

    ResStringPoolRef name
    int firstChar
    int lastChar

    static final ResStringPoolSpan parse(byte[] bytes) {
        if (!bytes) {
            return null
        }

        ResStringPoolSpan tResStringPoolSpan = new ResStringPoolSpan()
        tResStringPoolSpan.name = ResStringPoolRef.parse(Utils.subBytes(bytes, 0))
        tResStringPoolSpan.firstChar = Utils.bytes2Int(Utils.subBytes(bytes, tResStringPoolSpan.name.length(), 4))
        tResStringPoolSpan.lastChar = Utils.bytes2Int(Utils.subBytes(bytes, tResStringPoolSpan.name.length() + 4, 4))
        return tResStringPoolSpan
    }

    @Override
    byte[] bytes() {
        return ByteBuffer.allocate(length()).
                put(name.bytes()).
                put(Utils.int2Bytes(firstChar)).
                put(Utils.int2Bytes(lastChar)).
                array()
    }

    @Override
    int length() {
        return name.length() + 4 + 4
    }
}
