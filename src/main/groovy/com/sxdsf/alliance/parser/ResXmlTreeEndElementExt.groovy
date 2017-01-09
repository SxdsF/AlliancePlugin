package com.sxdsf.alliance.parser

import java.nio.ByteBuffer

/**
 *
 * struct ResXMLTree_endElementExt
 *{// String of the full namespace of this element.
 * struct ResStringPool_ref ns;
 * // String name of this node if it is an ELEMENT; the raw
 * // character data if this is a CDATA node.
 * struct ResStringPool_ref name;
 *};
 *
 * ResXmlTreeEndElementExt
 * @author 孙博闻
 * @date 2016/12/15 上午9:37
 * @desc 资源文件结束结点
 */
class ResXmlTreeEndElementExt implements Resource {

    ResStringPoolRef ns
    ResStringPoolRef name

    static final ResXmlTreeEndElementExt parse(byte[] bytes) {
        if (!bytes) {
            return null
        }

        ResXmlTreeEndElementExt tResXmlTreeEndElementExt = new ResXmlTreeEndElementExt()
        tResXmlTreeEndElementExt.ns = ResStringPoolRef.parse(Utils.subBytes(bytes, 0))
        tResXmlTreeEndElementExt.name = ResStringPoolRef.parse(Utils.subBytes(bytes, tResXmlTreeEndElementExt.ns.length()))
        return tResXmlTreeEndElementExt
    }

    @Override
    byte[] bytes() {
        return ByteBuffer.allocate(length()).put(ns.bytes()).put(name.bytes()).array()
    }

    @Override
    int length() {
        return ns.length() + name.length()
    }
}
