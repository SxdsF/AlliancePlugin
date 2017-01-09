package com.sxdsf.alliance.parser

import java.nio.ByteBuffer

/**
 *
 * struct ResStringPool_ref{// Index into the string pool table (uint32_t-offset from the indices
 * // immediately after ResStringPool_header) at which to find the location
 * // of the string data in the pool.
 * uint32_t index;};
 *
 * ResStringPoolRef
 * @author 孙博闻
 * @date 2016/12/14 下午11:51
 * @desc string在StringPool中的引用
 */
class ResStringPoolRef implements Resource {

    int index

    static final ResStringPoolRef parse(byte[] bytes) {
        if (!bytes) {
            return null
        }

        return new ResStringPoolRef(index: Utils.bytes2Int(Utils.subBytes(bytes, 0, 4)))
    }

    @Override
    byte[] bytes() {
        return ByteBuffer.allocate(length()).put(Utils.int2Bytes(index)).array()
    }

    @Override
    int length() {
        return 4
    }
}
