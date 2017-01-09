package com.sxdsf.alliance.parser.axml

import com.sxdsf.alliance.parser.ResXmlTreeEndElementExt
import com.sxdsf.alliance.parser.ResXmlTreeNode
import com.sxdsf.alliance.parser.Utils

import java.nio.ByteBuffer

/**
 * EndElement
 * @author 孙博闻
 * @date 2016/12/15 上午9:33
 * @desc 元素结束
 */
class EndElement extends Element {

    ResXmlTreeEndElementExt mResXmlTreeEndElementExt

    static final EndElement parse(byte[] bytes) {
        if (!bytes) {
            return null
        }

        EndElement tEndElement = new EndElement()
        tEndElement.mResXmlTreeNode = ResXmlTreeNode.parse(Utils.subBytes(bytes, 0))
        tEndElement.mResXmlTreeEndElementExt = ResXmlTreeEndElementExt.parse(Utils.subBytes(bytes, tEndElement.mResXmlTreeNode.length()))
        return tEndElement
    }

    @Override
    byte[] bytes() {
        byte[] tBytes = super.bytes()
        ByteBuffer tByteBuffer = ByteBuffer.allocate(length())
        tByteBuffer.put(tBytes).put(mResXmlTreeEndElementExt.bytes())
        return tByteBuffer.array()
    }

    @Override
    int length() {
        return super.length() + mResXmlTreeEndElementExt.length()
    }
}
