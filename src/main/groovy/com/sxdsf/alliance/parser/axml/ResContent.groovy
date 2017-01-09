package com.sxdsf.alliance.parser.axml

import com.sxdsf.alliance.parser.ResChunkHeader
import com.sxdsf.alliance.parser.Resource
import com.sxdsf.alliance.parser.ResourceType
import com.sxdsf.alliance.parser.Utils

import java.nio.ByteBuffer

/**
 * ResContent
 * @author 孙博闻
 * @date 2016/12/16 上午12:02
 * @desc 资源内容
 */
class ResContent implements Resource {

    List<Namespace> mStartNamespace = new ArrayList<>()
    List<Element> mElements = new ArrayList<>()
    List<Namespace> mEndNamespace = new ArrayList<>()

    private int mLength

    static final ResContent parse(byte[] bytes) {
        if (!bytes) {
            return null
        }

        ResContent tResContent = new ResContent()
        tResContent.mLength = bytes.length
        int tOffset = 0
        while (!(tOffset >= bytes.length)) {
            ResChunkHeader tResChunkHeader = ResChunkHeader.parse(Utils.subBytes(bytes, tOffset))
            switch (tResChunkHeader.type) {
                case ResourceType.RES_XML_START_NAMESPACE_TYPE:
                    tResContent.mStartNamespace.add(Namespace.parse(Utils.subBytes(bytes, tOffset, tResChunkHeader.size)))
                    break;
                case ResourceType.RES_XML_START_ELEMENT_TYPE:
                    tResContent.mElements.add(StartElement.parse(Utils.subBytes(bytes, tOffset, tResChunkHeader.size)))
                    break;
                case ResourceType.RES_XML_END_ELEMENT_TYPE:
                    tResContent.mElements.add(EndElement.parse(Utils.subBytes(bytes, tOffset, tResChunkHeader.size)))
                    break;
                case ResourceType.RES_XML_END_NAMESPACE_TYPE:
                    tResContent.mEndNamespace.add(Namespace.parse(Utils.subBytes(bytes, tOffset, tResChunkHeader.size)))
                    break;
            }
            tOffset += tResChunkHeader.size
        }
        return tResContent
    }

    @Override
    byte[] bytes() {
        ByteBuffer tByteBuffer = ByteBuffer.allocate(length())
        if (!mStartNamespace.isEmpty()) {
            mStartNamespace.each {
                tByteBuffer.put(it.bytes())
            }
        }
        if (!mElements.isEmpty()) {
            mElements.each {
                tByteBuffer.put(it.bytes())
            }
        }
        if (!mEndNamespace.isEmpty()) {
            mEndNamespace.each {
                tByteBuffer.put(it.bytes())
            }
        }
        return tByteBuffer.array()
    }

    @Override
    int length() {
        return mLength
    }
}
