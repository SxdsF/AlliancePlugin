package com.sxdsf.alliance.parser

import java.nio.ByteBuffer

/**
 *
 * struct ResXMLTree_header
 *{struct ResChunk_header header;
 *};
 *
 * ResXmlTreeHeader
 * @author 孙博闻
 * @date 2016/12/14 下午11:59
 * @desc Xml Tree header
 */
class ResXmlTreeHeader implements Resource {

    ResChunkHeader header

    static final ResXmlTreeHeader parse(byte[] bytes) {
        if (!bytes) {
            return null
        }

        return new ResXmlTreeHeader(header: ResChunkHeader.parse(Utils.subBytes(bytes, 0)))
    }

    @Override
    byte[] bytes() {
        return ByteBuffer.allocate(length()).put(header.bytes()).array()
    }

    @Override
    int length() {
        return header.length()
    }
}
