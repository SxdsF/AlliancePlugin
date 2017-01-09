package com.sxdsf.alliance.parser.axml

import com.sxdsf.alliance.parser.ResXmlTreeNamespacesExt
import com.sxdsf.alliance.parser.ResXmlTreeNode
import com.sxdsf.alliance.parser.Resource
import com.sxdsf.alliance.parser.Utils

import java.nio.ByteBuffer

/**
 * Namespace
 * @author 孙博闻
 * @date 2016/12/15 上午9:29
 * @desc 命名空间结点的结构
 */
class Namespace implements Resource {

    ResXmlTreeNode mResXmlTreeNode
    ResXmlTreeNamespacesExt mResXmlTreeNamespacesExt

    static final Namespace parse(byte[] bytes) {
        if (!bytes) {
            return null
        }

        Namespace tNamespace = new Namespace()
        tNamespace.mResXmlTreeNode = ResXmlTreeNode.parse(Utils.subBytes(bytes, 0))
        tNamespace.mResXmlTreeNamespacesExt = ResXmlTreeNamespacesExt.parse(Utils.subBytes(bytes, tNamespace.mResXmlTreeNode.length()))
        return tNamespace
    }

    @Override
    byte[] bytes() {
        return ByteBuffer.allocate(length()).put(mResXmlTreeNode.bytes()).put(mResXmlTreeNamespacesExt.bytes()).array()
    }

    @Override
    int length() {
        return mResXmlTreeNode.length() + mResXmlTreeNamespacesExt.length()
    }
}
