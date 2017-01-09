package com.sxdsf.alliance.parser

import java.nio.ByteBuffer

/**
 *
 * struct ResXMLTree_node
 *{struct ResChunk_header header;
 * // Line number in original source file at which this element appeared.
 * uint32_t lineNumber;
 * // Optional XML comment that was associated with this element; -1 if none.
 * struct ResStringPool_ref comment;
 *};
 *
 * ResXmlTreeNode
 * @author 孙博闻
 * @date 2016/12/14 下午11:35
 * @desc xml树结点
 */
class ResXmlTreeNode implements Resource {

    ResChunkHeader header
    int lineNumber
    ResStringPoolRef comment

    static final ResXmlTreeNode parse(byte[] bytes) {
        if (!bytes) {
            return null
        }

        ResXmlTreeNode tResXmlTreeNode = new ResXmlTreeNode()
        tResXmlTreeNode.header = ResChunkHeader.parse(Utils.subBytes(bytes, 0))
        tResXmlTreeNode.lineNumber = Utils.bytes2Int(Utils.subBytes(bytes, tResXmlTreeNode.header.length(), 4))
        tResXmlTreeNode.comment = ResStringPoolRef.parse(Utils.subBytes(bytes, tResXmlTreeNode.header.length() + 4))
        return tResXmlTreeNode
    }

    @Override
    byte[] bytes() {
        return ByteBuffer.allocate(length()).
                put(header.bytes()).
                put(Utils.int2Bytes(lineNumber)).
                put(comment.bytes()).
                array()
    }

    @Override
    int length() {
        return header.length() + 4 + comment.length()
    }
}
