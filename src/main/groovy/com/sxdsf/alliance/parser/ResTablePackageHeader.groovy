package com.sxdsf.alliance.parser

import java.nio.ByteBuffer

/**
 *
 * struct ResTable_package
 *{struct ResChunk_header header;
 * // If this is a base package, its ID.  Package IDs start
 * // at 1 (corresponding to the value of the package bits in a
 * // resource identifier).  0 means this is not a base package.
 * uint32_t id;
 * // Actual name of this package, \0-terminated.
 * char16_t name[128];
 * // Offset to a ResStringPool_header defining the resource
 * // type symbol table.  If zero, this package is inheriting from
 * // another base package (overriding specific values in it).
 * uint32_t typeStrings;
 * // Last index into typeStrings that is for public use by others.
 * uint32_t lastPublicType;
 * // Offset to a ResStringPool_header defining the resource
 * // key symbol table.  If zero, this package is inheriting from
 * // another base package (overriding specific values in it).
 * uint32_t keyStrings;
 * // Last index into keyStrings that is for public use by others.
 * uint32_t lastPublicKey;
 *};
 *
 * ResTablePackageHeader
 * @author 孙博闻
 * @date 2016/12/14 上午11:09
 * @desc package资源的结构
 */
class ResTablePackageHeader implements Resource {

    ResChunkHeader header
    int id
    byte[] name = new byte[256]
    int typeStrings
    int lastPublicType
    int keyStrings
    int lastPublicKey
    int typeIdOffset

    static final ResTablePackageHeader parse(byte[] bytes) {
        if (!bytes) {
            return null
        }

        ResTablePackageHeader tResTablePackageHeader = new ResTablePackageHeader()
        tResTablePackageHeader.header = ResChunkHeader.parse(Utils.subBytes(bytes, 0))
        tResTablePackageHeader.id = Utils.bytes2Int(Utils.subBytes(bytes, tResTablePackageHeader.header.length(), 4))
        tResTablePackageHeader.name = Utils.subBytes(bytes, tResTablePackageHeader.header.length() + 4, 256)
        tResTablePackageHeader.typeStrings = Utils.bytes2Int(Utils.subBytes(bytes, tResTablePackageHeader.header.length() + 4 + 256, 4))
        tResTablePackageHeader.lastPublicType = Utils.bytes2Int(Utils.subBytes(bytes, tResTablePackageHeader.header.length() + 4 + 256 + 4, 4))
        tResTablePackageHeader.keyStrings = Utils.bytes2Int(Utils.subBytes(bytes, tResTablePackageHeader.header.length() + 4 + 256 + 4 + 4, 4))
        tResTablePackageHeader.lastPublicKey = Utils.bytes2Int(Utils.subBytes(bytes, tResTablePackageHeader.header.length() + 4 + 256 + 4 + 4 + 4, 4))
        tResTablePackageHeader.typeIdOffset = Utils.bytes2Int(Utils.subBytes(bytes, tResTablePackageHeader.header.length() + 4 + 256 + 4 + 4 + 4 + 4, 4))
        return tResTablePackageHeader
    }

    String getStringName() {
        return new String(name)
    }

    @Override
    byte[] bytes() {
        return ByteBuffer.allocate(length()).
                put(header.bytes()).
                put(Utils.int2Bytes(id)).
                put(name).
                put(Utils.int2Bytes(typeStrings)).
                put(Utils.int2Bytes(lastPublicType)).
                put(Utils.int2Bytes(keyStrings)).
                put(Utils.int2Bytes(lastPublicKey)).
                put(Utils.int2Bytes(typeIdOffset)).
                array()
    }

    @Override
    int length() {
        return header.length() + 4 + 256 + 4 + 4 + 4 + 4 + 4
    }
}
