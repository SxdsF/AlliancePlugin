package com.sxdsf.alliance.parser

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.IntBuffer

/**
 * Utils
 * @author 孙博闻
 * @date 2016/12/14 下午2:06
 * @desc bytes转int，short的工具类，小端编码
 */
class Utils {

    static final byte[] subBytes(byte[] src, int start) {
        return subBytes(src, start, src.length - start)
    }


    static final byte[] subBytes(byte[] src, int start, int len) {
        if (!src) {
            return null
        }
        if (start > src.length) {
            return null
        }
        if ((start + len) > src.length) {
            return null
        }
        if (start < 0) {
            return null
        }
        if (len <= 0) {
            return null
        }
        byte[] resultByte = new byte[len]
        for (int i = 0; i < len; i++) {
            resultByte[i] = src[i + start]
        }
        return resultByte
    }

    static final int bytes2Int(byte[] bytes) {
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt()
    }

    static final int[] bytes2Ints(byte[] bytes) {
        if (!bytes) {
            return new int[0]
        }
        int tLength = bytes.length / 4
        IntBuffer tIntBuffer = IntBuffer.allocate(tLength)
        int tOffset = 0
        while (!(tOffset >= bytes.length)) {
            tIntBuffer.put(bytes2Int(subBytes(bytes, tOffset, 4)))
            tOffset += 4
        }
        return tIntBuffer.array()
    }

    static final short bytes2Short(byte[] bytes) {
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort()
    }

    static final char[] bytes2Chars(byte[] bytes) {
        return ByteBuffer.wrap(bytes).asCharBuffer().array()
    }

    static final byte[] int2Bytes(int res) {
        byte[] targets = new byte[4];
        targets[0] = (byte) (res & 0xff);// 最低位
        targets[1] = (byte) ((res >> 8) & 0xff);// 次低位
        targets[2] = (byte) ((res >> 16) & 0xff);// 次高位
        targets[3] = (byte) (res >>> 24);// 最高位,无符号右移。
        return targets
    }

    static final byte[] ints2Bytes(int[] res) {
        ByteBuffer tByteBuffer = ByteBuffer.allocate(res.length * 4)
        for (int i : res) {
            tByteBuffer.put(int2Bytes(i))
        }
        return tByteBuffer.array()
    }

    static final byte[] short2Bytes(short res) {
        byte[] targets = new byte[2];
        targets[0] = (byte) (res & 0xff);// 最低位
        targets[1] = (byte) (res >>> 8);// 最高位,无符号右移。
        return targets
    }

    static final int replacePackageId(int src, int packageId) {
        if (src >> 24 != 0x7f) {
            return src
        }
        return ((packageId << 24) | (src & 0x00ffffff)) // replace pp
    }
}
