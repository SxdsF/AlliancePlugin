package com.sxdsf.alliance.parser

import java.nio.ByteBuffer

/**
 *
 * struct ResTable_map_entry : public ResTable_entry{// Resource identifier of the parent mapping, or 0 if there is none.
 // This is always treated as a TYPE_DYNAMIC_REFERENCE.
 ResTable_ref parent;
 // Number of name/value pairs that follow for FLAG_COMPLEX.
 uint32_t count;};
 *
 * ResTableMapEntry
 * @author 孙博闻
 * @date 2016/12/16 上午11:17
 * @desc map entry
 */
class ResTableMapEntry extends ResTableEntry {

    // Resource identifier of the parent mapping, or 0 if there is none.
    // This is always treated as a TYPE_DYNAMIC_REFERENCE.
    ResTableRef parent
    // Number of name/value pairs that follow for FLAG_COMPLEX.
    int count

    static final ResTableMapEntry parse(byte[] bytes) {
        if (!bytes) {
            return null
        }

        ResTableMapEntry tResTableMapEntry = new ResTableMapEntry()
        tResTableMapEntry.size = Utils.bytes2Short(Utils.subBytes(bytes, 0, 2))
        tResTableMapEntry.flags = Utils.bytes2Short(Utils.subBytes(bytes, 2, 2))
        tResTableMapEntry.key = ResStringPoolRef.parse(Utils.subBytes(bytes, 2 + 2))
        tResTableMapEntry.parent = ResTableRef.parse(Utils.subBytes(bytes, 2 + 2 + tResTableMapEntry.key.length()))
        tResTableMapEntry.count = Utils.bytes2Int(Utils.subBytes(bytes, 2 + 2 + tResTableMapEntry.key.length() + tResTableMapEntry.parent.length(), 4))
        return tResTableMapEntry
    }

    @Override
    byte[] bytes() {
        return ByteBuffer.allocate(length()).
                put(Utils.short2Bytes(size)).
                put(Utils.short2Bytes(flags)).
                put(key.bytes()).
                put(parent.bytes()).
                put(Utils.int2Bytes(count)).
                array()
    }

    @Override
    int length() {
        return 2 + 2 + key.length() + parent.length() + 4
    }
}
