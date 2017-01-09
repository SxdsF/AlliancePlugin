package com.sxdsf.alliance.parser.arsc

import com.sxdsf.alliance.parser.ResTableEntry
import com.sxdsf.alliance.parser.ResValue
import com.sxdsf.alliance.parser.Utils

import java.nio.ByteBuffer

/**
 * NonBagData
 * @author 孙博闻
 * @date 2016/12/16 下午2:51
 * @desc 非bag类型数据
 */
class NonBagData extends ResTableEntryData {

    ResTableEntry mResTableEntry
    ResValue mResValue

    static final NonBagData parse(byte[] bytes) {
        if (!bytes) {
            return null
        }

        NonBagData tNonBagData = new NonBagData()
        tNonBagData.mResTableEntry = ResTableEntry.parse(Utils.subBytes(bytes, 0))
        tNonBagData.mResValue = ResValue.parse(Utils.subBytes(bytes, tNonBagData.mResTableEntry.length()))
        return tNonBagData
    }

    @Override
    byte[] bytes() {
        return ByteBuffer.allocate(length()).
                put(mResTableEntry.bytes()).
                put(mResValue.bytes()).
                array()
    }

    @Override
    int length() {
        return mResTableEntry.length() + mResValue.length()
    }
}
