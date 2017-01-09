package com.sxdsf.alliance.parser

import java.nio.ByteBuffer

/**
 * ResStringPool
 * @author 孙博闻
 * @date 2016/12/14 下午5:33
 * @desc 字符串资源池
 */
class ResStringPool implements Resource {

    ResStringPoolHeader mResStringPoolHeader
    int[] mStringsOffsets
    int[] mStylesOffsets
    List<String> mStrings
    List<String> mStyles
    byte[] mStringPool

    static final ResStringPool parse(byte[] bytes) {
        if (!bytes) {
            return null
        }
        ResStringPool tResStringPool = new ResStringPool()
        tResStringPool.mResStringPoolHeader = ResStringPoolHeader.parse(Utils.subBytes(bytes, 0))
        tResStringPool.mStringsOffsets = Utils.bytes2Ints(Utils.subBytes(bytes, tResStringPool.mResStringPoolHeader.header.headerSize,
                tResStringPool.mResStringPoolHeader.stringCount * 4))
        tResStringPool.mStylesOffsets = Utils.bytes2Ints(Utils.subBytes(bytes,
                tResStringPool.mResStringPoolHeader.header.headerSize + tResStringPool.mStringsOffsets.length * 4,
                tResStringPool.mResStringPoolHeader.styleCount * 4))
//        tResStringPool.mStrings = new ArrayList<>(tResStringPool.mResStringPoolHeader.stringCount)
//        tResStringPool.mStyles = new ArrayList<>(tResStringPool.mResStringPoolHeader.styleCount)
//        int tOffset = tResStringPool.mResStringPoolHeader.stringsStart
//        ResStringPoolSpan tResStringPoolSpan = ResStringPoolSpan.parse(Utils.subBytes(bytes, tOffset))
//        for (int i = 1; i < tResStringPool.mResStringPoolHeader.stringCount; i++) {
//
//            tOffset += tResStringPool.mStringsOffsets[i]
//        }
        tResStringPool.mStringPool = Utils.subBytes(bytes, tResStringPool.mResStringPoolHeader.length(), tResStringPool.mResStringPoolHeader.header.size - tResStringPool.mResStringPoolHeader.length())
        return tResStringPool
    }

    @Override
    byte[] bytes() {
        return ByteBuffer.allocate(length()).
                put(mResStringPoolHeader.bytes()).
                put(mStringPool).
                array()
    }

    @Override
    int length() {
        return mResStringPoolHeader.header.size
    }

    @Override
    String toString() {
        String tText = 'GlobalResStringPoolHeader\r\n'
        tText += mResStringPoolHeader.toString() + '\r\n'
        tText += "StringsOffsets:${mStringsOffsets}" + '\r\n'
        tText += "StylesOffsets:${mStylesOffsets}"
        return tText
    }
}
