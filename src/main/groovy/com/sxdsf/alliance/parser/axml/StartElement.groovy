package com.sxdsf.alliance.parser.axml

import com.sxdsf.alliance.parser.ResXmlTreeAttrExt
import com.sxdsf.alliance.parser.ResXmlTreeAttribute
import com.sxdsf.alliance.parser.ResXmlTreeNode
import com.sxdsf.alliance.parser.Utils

import java.nio.ByteBuffer

/**
 * StartElement
 * @author 孙博闻
 * @date 2016/12/15 上午9:33
 * @desc 元素开始
 */
class StartElement extends Element {

    ResXmlTreeAttrExt mResXmlTreeAttrExt
    byte[] mGapBetweenAttrFromHead
    List<ResXmlTreeAttribute> mResXmlTreeAttributes

    static final StartElement parse(byte[] bytes) {
        if (!bytes) {
            return null
        }

        StartElement tStartElement = new StartElement()
        tStartElement.mResXmlTreeNode = ResXmlTreeNode.parse(Utils.subBytes(bytes, 0))
        tStartElement.mResXmlTreeAttrExt = ResXmlTreeAttrExt.parse(Utils.subBytes(bytes, tStartElement.mResXmlTreeNode.length()))
        tStartElement.mGapBetweenAttrFromHead = Utils.subBytes(bytes, tStartElement.mResXmlTreeNode.length()
                + tStartElement.mResXmlTreeAttrExt.length(), tStartElement.mResXmlTreeAttrExt.attributeStart
                - (tStartElement.mResXmlTreeNode.length() + tStartElement.mResXmlTreeAttrExt.length()))
        int tOffset = tStartElement.mResXmlTreeNode.length() + tStartElement.mResXmlTreeAttrExt.length()
        if (tStartElement.mGapBetweenAttrFromHead != null && tStartElement.mGapBetweenAttrFromHead.length > 0) {
            tOffset += tStartElement.mGapBetweenAttrFromHead.length
        }
        tStartElement.mResXmlTreeAttributes = parseAttributes(Utils.subBytes(bytes, tOffset), tStartElement.mResXmlTreeAttrExt.attributeCount)

        return tStartElement
    }

    static final List<ResXmlTreeAttribute> parseAttributes(byte[] bytes, int count) {
        List<ResXmlTreeAttribute> tResXmlTreeAttributes = new ArrayList<>(count)
        if (bytes) {
            int tOffset = 0
            for (int i = 0; i < count; i++) {
                tResXmlTreeAttributes.add(ResXmlTreeAttribute.parse(Utils.subBytes(bytes, tOffset)))
                tOffset += tResXmlTreeAttributes.get(tResXmlTreeAttributes.size() - 1).length()
            }
        }
        return tResXmlTreeAttributes
    }

    @Override
    byte[] bytes() {
        ByteBuffer tByteBuffer = ByteBuffer.allocate(length())
        tByteBuffer.put(super.bytes()).put(mResXmlTreeAttrExt.bytes())
        if (mGapBetweenAttrFromHead != null && mGapBetweenAttrFromHead.length > 0) {
            tByteBuffer.put(mGapBetweenAttrFromHead)
        }
        if (!mResXmlTreeAttributes.isEmpty()) {
            mResXmlTreeAttributes.each {
                tByteBuffer.put(it.bytes())
            }
        }
        return tByteBuffer.array()
    }

    @Override
    int length() {
        int length = super.length() + mResXmlTreeAttrExt.length()
        if (mGapBetweenAttrFromHead != null && mGapBetweenAttrFromHead.length > 0) {
            length += mGapBetweenAttrFromHead.length
        }
        return length + mResXmlTreeAttrExt.attributeSize * mResXmlTreeAttrExt.attributeCount
    }
}
