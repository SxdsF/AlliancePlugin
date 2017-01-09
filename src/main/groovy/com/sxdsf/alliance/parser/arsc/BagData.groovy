package com.sxdsf.alliance.parser.arsc

import com.sxdsf.alliance.parser.ResTableMap
import com.sxdsf.alliance.parser.ResTableMapEntry
import com.sxdsf.alliance.parser.Utils

import java.nio.ByteBuffer

/**
 * BagData
 * @author 孙博闻
 * @date 2016/12/16 下午2:50
 * @desc bag类型数据
 */
class BagData extends ResTableEntryData {

    ResTableMapEntry mResTableMapEntry
    List<ResTableMap> mResTableMaps

    private int mLength

    static final BagData parse(byte[] bytes) {
        if (!bytes) {
            return null
        }

        BagData tBagData = new BagData()
        tBagData.mResTableMapEntry = ResTableMapEntry.parse(Utils.subBytes(bytes, 0))
        tBagData.mResTableMaps = new ArrayList<>(tBagData.mResTableMapEntry.count)
        int tOffset = tBagData.mResTableMapEntry.length()
        def count = tBagData.mResTableMapEntry.count & 0x00ffff // fix aapt v22 bug?
        for (int i = 0; i < count; i++) {
            tBagData.mResTableMaps.add(ResTableMap.parse(Utils.subBytes(bytes, tOffset)))
            tOffset += tBagData.mResTableMaps.get(tBagData.mResTableMaps.size() - 1).length()
        }
        tBagData.mLength = tOffset
        return tBagData
    }

    @Override
    byte[] bytes() {
        ByteBuffer tByteBuffer = ByteBuffer.allocate(length())
        tByteBuffer.put(mResTableMapEntry.bytes())
        if (!mResTableMaps.isEmpty()) {
            mResTableMaps.each {
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
