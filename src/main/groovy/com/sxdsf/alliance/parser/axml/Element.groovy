package com.sxdsf.alliance.parser.axml

import com.sxdsf.alliance.parser.Resource
import com.sxdsf.alliance.parser.ResXmlTreeNode

import java.nio.ByteBuffer

/**
 * Element
 * @author 孙博闻
 * @date 2016/12/15 上午9:30
 * @desc 元素
 */
abstract class Element implements Resource {

    ResXmlTreeNode mResXmlTreeNode

    @Override
    byte[] bytes() {
        return ByteBuffer.wrap(mResXmlTreeNode.bytes()).array()
    }

    @Override
    int length() {
        return mResXmlTreeNode.length()
    }
}
