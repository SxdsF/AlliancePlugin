package com.sxdsf.alliance.parser

import java.nio.ByteBuffer

/**
 *
 * * struct ResXMLTree_attribute  {// Namespace of this attribute.
 * struct ResStringPool_ref ns;
 * // Name of this attribute.
 * struct ResStringPool_ref name;
 * // The original raw string value of this attribute.
 * struct ResStringPool_ref rawValue;
 * // Processesd typed value of this attribute.
 * struct Res_value typedValue;  };
 *
 * ResXmlTreeAttribute
 * @author 孙博闻
 * @date 2016/12/15 上午9:24
 * @desc xml资源属性的结构
 */
class ResXmlTreeAttribute implements Resource {

    // Namespace of this attribute.
    ResStringPoolRef ns
    // Name of this attribute.
    ResStringPoolRef name
    // The original raw string value of this attribute.
    ResStringPoolRef rawValue
    // Processesd typed value of this attribute.
    ResStringPoolRef typedValue

    int resourceId

    static final ResXmlTreeAttribute parse(byte[] bytes) {
        if (!bytes) {
            return null
        }

        ResXmlTreeAttribute tResXmlTreeAttribute = new ResXmlTreeAttribute()
        tResXmlTreeAttribute.ns = ResStringPoolRef.parse(Utils.subBytes(bytes, 0))
        tResXmlTreeAttribute.name = ResStringPoolRef.parse(Utils.subBytes(bytes, tResXmlTreeAttribute.ns.length()))
        tResXmlTreeAttribute.rawValue = ResStringPoolRef.parse(Utils.subBytes(bytes, tResXmlTreeAttribute.ns.length()
                + tResXmlTreeAttribute.name.length()))
        tResXmlTreeAttribute.typedValue = ResStringPoolRef.parse(Utils.subBytes(bytes, tResXmlTreeAttribute.ns.length()
                + tResXmlTreeAttribute.name.length() + tResXmlTreeAttribute.rawValue.length()))
        tResXmlTreeAttribute.resourceId = Utils.bytes2Int(Utils.subBytes(bytes, tResXmlTreeAttribute.ns.length()
                + tResXmlTreeAttribute.name.length() + tResXmlTreeAttribute.rawValue.length() + tResXmlTreeAttribute.typedValue.length(), 4))
        return tResXmlTreeAttribute
    }

    @Override
    byte[] bytes() {
        return ByteBuffer.allocate(length()).
                put(ns.bytes()).
                put(name.bytes()).
                put(rawValue.bytes()).
                put(typedValue.bytes()).
                put(Utils.int2Bytes(resourceId)).
                array()
    }

    @Override
    int length() {
        return ns.length() + name.length() + rawValue.length() + typedValue.length() + 4
    }
}
