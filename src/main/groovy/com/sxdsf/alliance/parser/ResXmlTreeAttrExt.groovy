package com.sxdsf.alliance.parser

import java.nio.ByteBuffer

/**
 *
 * struct ResXMLTree_attrExt  {// String of the full namespace of this element.
 * struct ResStringPool_ref ns;
 * // String name of this node if it is an ELEMENT; the raw
 * // character data if this is a CDATA node.
 * struct ResStringPool_ref name;
 * // Byte offset from the start of this structure where the attributes start.
 * uint16_t attributeStart;
 * // Size of the ResXMLTree_attribute structures that follow.
 * uint16_t attributeSize;
 * // Number of attributes associated with an ELEMENT.  These are
 * // available as an array of ResXMLTree_attribute structures
 * // immediately following this node.
 * uint16_t attributeCount;
 * // Index (1-based) of the "id" attribute. 0 if none.
 * uint16_t idIndex;
 * // Index (1-based) of the "class" attribute. 0 if none.
 * uint16_t classIndex;
 * // Index (1-based) of the "style" attribute. 0 if none.
 * uint16_t styleIndex;  };
 *
 * ResXmlTreeAttrExt
 * @author 孙博闻
 * @date 2016/12/15 上午9:22
 * @desc 文件描述
 */
class ResXmlTreeAttrExt implements Resource {

    // String of the full namespace of this element.
    ResStringPoolRef ns

    // String name of this node if it is an ELEMENT; the raw
    // character data if this is a CDATA node.
    ResStringPoolRef name

    // Byte offset from the start of this structure where the attributes start.
    short attributeStart

    // Size of the ResXMLTree_attribute structures that follow.
    short attributeSize

    // Number of attributes associated with an ELEMENT.  These are
    // available as an array of ResXMLTree_attribute structures
    // immediately following this node.
    short attributeCount

    // Index (1-based) of the "id" attribute. 0 if none.
    short idIndex

    // Index (1-based) of the "class" attribute. 0 if none.
    short classIndex

    // Index (1-based) of the "style" attribute. 0 if none.
    short styleIndex

    static final ResXmlTreeAttrExt parse(byte[] bytes) {
        if (!bytes) {
            return null
        }

        ResXmlTreeAttrExt tResXmlTreeAttrExt = new ResXmlTreeAttrExt()
        tResXmlTreeAttrExt.ns = ResStringPoolRef.parse(Utils.subBytes(bytes, 0))
        tResXmlTreeAttrExt.name = ResStringPoolRef.parse(Utils.subBytes(bytes, tResXmlTreeAttrExt.ns.length()))
        tResXmlTreeAttrExt.attributeStart = Utils.bytes2Short(Utils.subBytes(bytes, tResXmlTreeAttrExt.ns.length() + tResXmlTreeAttrExt.name.length(), 2))
        tResXmlTreeAttrExt.attributeSize = Utils.bytes2Short(Utils.subBytes(bytes, tResXmlTreeAttrExt.ns.length() + tResXmlTreeAttrExt.name.length() + 2, 2))
        tResXmlTreeAttrExt.attributeCount = Utils.bytes2Short(Utils.subBytes(bytes, tResXmlTreeAttrExt.ns.length() + tResXmlTreeAttrExt.name.length() + 2 + 2, 2))
        tResXmlTreeAttrExt.idIndex = Utils.bytes2Short(Utils.subBytes(bytes, tResXmlTreeAttrExt.ns.length() + tResXmlTreeAttrExt.name.length() + 2 + 2 + 2, 2))
        tResXmlTreeAttrExt.classIndex = Utils.bytes2Short(Utils.subBytes(bytes, tResXmlTreeAttrExt.ns.length() + tResXmlTreeAttrExt.name.length() + 2 + 2 + 2 + 2, 2))
        tResXmlTreeAttrExt.styleIndex = Utils.bytes2Short(Utils.subBytes(bytes, tResXmlTreeAttrExt.ns.length() + tResXmlTreeAttrExt.name.length() + 2 + 2 + 2 + 2 + 2, 2))
        return tResXmlTreeAttrExt
    }

    @Override
    byte[] bytes() {
        return ByteBuffer.allocate(length()).
                put(ns.bytes()).
                put(name.bytes()).
                put(Utils.short2Bytes(attributeStart)).
                put(Utils.short2Bytes(attributeSize)).
                put(Utils.short2Bytes(attributeCount)).
                put(Utils.short2Bytes(idIndex)).
                put(Utils.short2Bytes(classIndex)).
                put(Utils.short2Bytes(styleIndex)).
                array()
    }

    @Override
    int length() {
        return ns.length() + name.length() + 2 + 2 + 2 + 2 + 2 + 2
    }
}
