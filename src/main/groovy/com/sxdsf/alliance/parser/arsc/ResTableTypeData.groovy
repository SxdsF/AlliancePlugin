package com.sxdsf.alliance.parser.arsc

import com.sxdsf.alliance.parser.ResTableEntry
import com.sxdsf.alliance.parser.ResTableType
import com.sxdsf.alliance.parser.Resource
import com.sxdsf.alliance.parser.Utils

import java.nio.ByteBuffer

/**
 * ResTableTypeData
 * @author 孙博闻
 * @date 2016/12/16 下午2:20
 * @desc 文件描述
 */
class ResTableTypeData implements Resource {

    ResTableType mResTableType
    int[] mResTableTypeOffsets
    List<ResTableEntryData> mResTableEntryData = new ArrayList<>()

    static final ResTableTypeData parse(byte[] bytes) {
        if (!bytes) {
            return null
        }

        ResTableTypeData tResTableTypeData = new ResTableTypeData()
        tResTableTypeData.mResTableType = ResTableType.parse(Utils.subBytes(bytes, 0))
        tResTableTypeData.mResTableTypeOffsets = Utils.bytes2Ints(Utils.subBytes(bytes, tResTableTypeData.mResTableType.length()
                , tResTableTypeData.mResTableType.entryCount * 4))
        int tOffset = tResTableTypeData.mResTableType.entriesStart
        for (int i = 0; i < tResTableTypeData.mResTableType.entryCount; i++) {
            if (tResTableTypeData.mResTableTypeOffsets[i] == -1) {
                continue
            }
            ResTableEntry tResTableEntry = ResTableEntry.parse(Utils.subBytes(bytes, tOffset))
            switch (tResTableEntry.flags) {
            //1是bag类型
                case ResTableEntry.FLAG_COMPLEX:
                    BagData tBagData = BagData.parse(Utils.subBytes(bytes, tOffset))
                    tResTableTypeData.mResTableEntryData.add(tBagData)
                    tOffset += tBagData.length()
                    break;
                default:
                    NonBagData tNonBagData = NonBagData.parse(Utils.subBytes(bytes, tOffset))
                    tResTableTypeData.mResTableEntryData.add(tNonBagData)
                    tOffset += tNonBagData.length()
                    break;
            }
        }
        return tResTableTypeData
    }

    @Override
    byte[] bytes() {
        ByteBuffer tByteBuffer = ByteBuffer.allocate(length())
        tByteBuffer.put(mResTableType.bytes()).put(Utils.ints2Bytes(mResTableTypeOffsets))
        if (!mResTableEntryData.isEmpty()) {
            mResTableEntryData.each {
                tByteBuffer.put(it.bytes())
            }
        }
        return tByteBuffer.array()
    }

    @Override
    int length() {
        return mResTableType.header.size
    }
}
