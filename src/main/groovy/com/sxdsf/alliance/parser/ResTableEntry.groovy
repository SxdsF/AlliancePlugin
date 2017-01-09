package com.sxdsf.alliance.parser

import java.nio.ByteBuffer

/**
 *
 * struct ResTable_entry{// Number of bytes in this structure.
 uint16_t size;

 enum {// If set, this is a complex entry, holding a set of name/value
 // mappings.  It is followed by an array of ResTable_map structures.
 FLAG_COMPLEX = 0x0001,
 // If set, this resource has been declared public, so libraries
 // are allowed to reference it.
 FLAG_PUBLIC = 0x0002};
 uint16_t flags;

 // Reference into ResTable_package::keyStrings identifying this entry.
 struct ResStringPool_ref key;};
 *
 * ResTableEntry
 * @author 孙博闻
 * @date 2016/12/16 上午11:10
 * @desc 一个资源项的具体信息
 */
class ResTableEntry implements Resource {

    // If set, this is a complex entry, holding a set of name/value
    // mappings.  It is followed by an array of ResTable_map structures.
    static final def FLAG_COMPLEX = 0x0001
    // If set, this resource has been declared public, so libraries
    // are allowed to reference it.
    static final def FLAG_PUBLIC = 0x0002

    // Number of bytes in this structure.
    short size
    short flags

    // Reference into ResTable_package::keyStrings identifying this entry.
    ResStringPoolRef key

    static ResTableEntry parse(byte[] bytes) {
        if (!bytes) {
            return null
        }

        return new ResTableEntry(size: Utils.bytes2Short(Utils.subBytes(bytes, 0, 2)),
                flags: Utils.bytes2Short(Utils.subBytes(bytes, 2, 2)),
                key: ResStringPoolRef.parse(Utils.subBytes(bytes, 2 + 2)))
    }

    @Override
    byte[] bytes() {
        return ByteBuffer.allocate(length()).
                put(Utils.short2Bytes(size)).
                put(Utils.short2Bytes(flags)).
                put(key.bytes()).
                array()
    }

    @Override
    int length() {
        return 2 + 2 + key.length()
    }
}
