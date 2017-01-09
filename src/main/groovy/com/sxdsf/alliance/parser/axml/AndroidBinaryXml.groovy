package com.sxdsf.alliance.parser.axml

import com.sxdsf.alliance.parser.ResXmlTreeHeader
import com.sxdsf.alliance.parser.ResStringPool
import com.sxdsf.alliance.parser.Resource
import com.sxdsf.alliance.parser.Utils

import java.nio.ByteBuffer

/**
 * AndroidBinaryXml
 * @author 孙博闻
 * @date 2016/12/15 上午7:29
 * @desc Android二进制xml资源结构
 */
class AndroidBinaryXml implements Resource {

    ResXmlTreeHeader mResXmlTreeHeader
    ResStringPool mResStringPool
    ResourceIds mResourceIds
    ResContent mResContent

    static final AndroidBinaryXml parse(byte[] bytes) {
        if (!bytes) {
            return null
        }
        AndroidBinaryXml tAndroidBinaryXml = new AndroidBinaryXml()
        tAndroidBinaryXml.mResXmlTreeHeader = ResXmlTreeHeader.parse(Utils.subBytes(bytes, 0))
        tAndroidBinaryXml.mResStringPool = ResStringPool.parse(Utils.subBytes(bytes, tAndroidBinaryXml.mResXmlTreeHeader.length()))
        tAndroidBinaryXml.mResourceIds = ResourceIds.parse(Utils.subBytes(bytes, tAndroidBinaryXml.mResXmlTreeHeader.length()
                + tAndroidBinaryXml.mResStringPool.length()))
        tAndroidBinaryXml.mResContent = ResContent.parse(Utils.subBytes(bytes, tAndroidBinaryXml.mResXmlTreeHeader.length()
                + tAndroidBinaryXml.mResStringPool.length()
                + tAndroidBinaryXml.mResourceIds.length()))
        return tAndroidBinaryXml
    }

    @Override
    byte[] bytes() {
        return ByteBuffer.allocate(length()).
                put(mResXmlTreeHeader.bytes()).
                put(mResStringPool.bytes()).
                put(mResourceIds.bytes()).
                put(mResContent.bytes()).
                array()
    }

    @Override
    int length() {
        return mResXmlTreeHeader.header.size
    }
}
