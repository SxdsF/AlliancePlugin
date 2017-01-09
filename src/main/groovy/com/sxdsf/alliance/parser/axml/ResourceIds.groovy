package com.sxdsf.alliance.parser.axml

import com.sxdsf.alliance.parser.ResChunkHeader
import com.sxdsf.alliance.parser.Resource
import com.sxdsf.alliance.parser.Utils

import java.nio.ByteBuffer

/**
 * ResourceIds
 * @author 孙博闻
 * @date 2016/12/15 上午9:03
 * @desc 系统用的一段id资源
 */
class ResourceIds implements Resource {

    ResChunkHeader header
    byte[] ids

    static final ResourceIds parse(byte[] bytes) {
        if (!bytes) {
            return null
        }

        ResourceIds tResourceIds = new ResourceIds()
        tResourceIds.header = ResChunkHeader.parse(Utils.subBytes(bytes, 0))
        tResourceIds.ids = Utils.subBytes(bytes, tResourceIds.header.length(), tResourceIds.header.size - tResourceIds.header.length())
        return tResourceIds
    }

    @Override
    byte[] bytes() {
        return ByteBuffer.allocate(length()).put(header.bytes()).put(ids).array()
    }

    @Override
    int length() {
        return header.size
    }
}
