package com.sxdsf.alliance.parser

import java.nio.ByteBuffer

/**
 * struct ResXMLTree_namespaceExt
 *{// The prefix of the namespace.
 * struct ResStringPool_ref prefix;
 * // The URI of the namespace.
 * struct ResStringPool_ref uri;
 *};
 *
 * ResXmlTreeNamespacesExt
 * @author 孙博闻
 * @date 2016/12/15 上午9:16
 * @desc 命名空间
 */
class ResXmlTreeNamespacesExt implements Resource {

    ResStringPoolRef prefix
    ResStringPoolRef uri

    static final ResXmlTreeNamespacesExt parse(byte[] bytes) {
        if (!bytes) {
            return null
        }

        ResXmlTreeNamespacesExt tResXmlTreeNamespacesExt = new ResXmlTreeNamespacesExt()
        tResXmlTreeNamespacesExt.prefix = ResStringPoolRef.parse(Utils.subBytes(bytes, 0))
        tResXmlTreeNamespacesExt.uri = ResStringPoolRef.parse(Utils.subBytes(bytes, tResXmlTreeNamespacesExt.prefix.length()))
        return tResXmlTreeNamespacesExt
    }

    @Override
    byte[] bytes() {
        return ByteBuffer.allocate(length()).put(prefix.bytes()).put(uri.bytes()).array()
    }

    @Override
    int length() {
        return prefix.length() + uri.length()
    }
}
