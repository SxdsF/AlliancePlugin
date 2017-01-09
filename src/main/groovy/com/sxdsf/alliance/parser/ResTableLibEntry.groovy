package com.sxdsf.alliance.parser

import java.nio.ByteBuffer

/**
 *
 * struct ResTable_lib_entry{// The package-id this shared library was assigned at build time.
 // We use a uint32 to keep the structure aligned on a uint32 boundary.
 uint32_t packageId;

 // The package name of the shared library. \0 terminated.
 uint16_t packageName[128];};
 *
 * ResTableLibEntry
 * @author 孙博闻
 * @date 2016/12/27 上午10:43
 * @desc 描述一个shared lib
 */
class ResTableLibEntry implements Resource {

    // The package-id this shared library was assigned at build time.
    // We use a uint32 to keep the structure aligned on a uint32 boundary.
    int packageId

    // The package name of the shared library. \0 terminated.
    byte[] packageName = new byte[256]

    static final ResTableLibEntry parse(byte[] bytes) {
        if (!bytes) {
            return null
        }

        ResTableLibEntry tResTableLibEntry = new ResTableLibEntry()
        tResTableLibEntry.packageId = Utils.bytes2Int(Utils.subBytes(bytes, 0, 4))
        tResTableLibEntry.packageName = Utils.subBytes(bytes, 4, 256)
        return tResTableLibEntry
    }

    @Override
    byte[] bytes() {
        return ByteBuffer.allocate(length()).put(Utils.ints2Bytes(packageId)).put(packageName).array()
    }

    @Override
    int length() {
        return 4 + 256
    }
}
