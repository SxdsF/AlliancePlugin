package com.sxdsf.alliance.parser

import java.nio.ByteBuffer

/**
 * struct ResTable_ref{uint32_t ident;};
 *
 * ResTableRef
 * @author 孙博闻
 * @date 2016/12/14 下午11:47
 * @desc 唯一Id
 */
class ResTableRef implements Resource {

    int ident

    static final ResTableRef parse(byte[] bytes) {
        if (!bytes) {
            return null
        }

        ResTableRef tResTableRef = new ResTableRef()
        tResTableRef.ident = Utils.bytes2Int(Utils.subBytes(bytes, 0, 4))
        return tResTableRef
    }

    @Override
    byte[] bytes() {
        return ByteBuffer.allocate(length()).put(Utils.int2Bytes(ident)).array()
    }

    @Override
    int length() {
        return 4
    }
}
