package com.sxdsf.alliance.parser.arsc

import com.sxdsf.alliance.parser.ResTableTypeSpec
import com.sxdsf.alliance.parser.Resource

import java.nio.ByteBuffer

/**
 * ResTableData
 * @author 孙博闻
 * @date 2016/12/16 下午12:06
 * @desc 数据块，一个数据块包括： spec，entry，map
 */
class ResTableData implements Resource {

    ResTableTypeSpec mResTableTypeSpec
    byte[] mResTableTypeSpecBytes
    List<ResTableTypeData> mResTableTypeData = new ArrayList<>()

    int mLength

    @Override
    byte[] bytes() {
        ByteBuffer tByteBuffer = ByteBuffer.allocate(length()).put(mResTableTypeSpec.bytes())
        if (mResTableTypeSpecBytes != null && mResTableTypeSpecBytes.length > 0) {
            tByteBuffer.put(mResTableTypeSpecBytes)
        }
        if (!mResTableTypeData.isEmpty()) {
            mResTableTypeData.each {
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
