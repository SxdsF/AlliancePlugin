package com.sxdsf.alliance.parser.arsc

import com.rrh.jdb.parser.*
import com.sxdsf.alliance.parser.ResChunkHeader
import com.sxdsf.alliance.parser.ResStringPoolHeader
import com.sxdsf.alliance.parser.ResTableLibEntry
import com.sxdsf.alliance.parser.ResTableLibHeader
import com.sxdsf.alliance.parser.ResTablePackageHeader
import com.sxdsf.alliance.parser.ResTableTypeSpec
import com.sxdsf.alliance.parser.Resource
import com.sxdsf.alliance.parser.ResourceType
import com.sxdsf.alliance.parser.Utils

import java.nio.ByteBuffer

/**
 * ResTablePackage
 * @author 孙博闻
 * @date 2016/12/14 下午5:36
 * @desc 各个package资源包
 */
class ResTablePackage implements Resource {

    ResTablePackageHeader mResTablePackageHeader
    byte[] mTypeAndKeyStringsBytes
    ResTableLibHeader mResTableLibHeader
    ResTableLibEntry mResTableLibEntry
    List<ResTableData> mResTableData

    static final ResTablePackage parse(byte[] bytes) {
        if (!bytes) {
            return null
        }
        ResTablePackage tResTablePackage = new ResTablePackage()
        tResTablePackage.mResTablePackageHeader = ResTablePackageHeader.parse(Utils.subBytes(bytes, 0))
        int tOffset = tResTablePackage.mResTablePackageHeader.keyStrings
        ResStringPoolHeader tKey = ResStringPoolHeader.parse(Utils.subBytes(bytes, tOffset))
        tOffset += tKey.header.size
        tResTablePackage.mTypeAndKeyStringsBytes = Utils.subBytes(bytes, tResTablePackage.mResTablePackageHeader.length()
                , tOffset - tResTablePackage.mResTablePackageHeader.length())

        //判断一下是否有shared lib
        ResChunkHeader tChunkHeader = ResChunkHeader.parse(Utils.subBytes(bytes, tResTablePackage.mResTablePackageHeader.length()
                + tResTablePackage.mTypeAndKeyStringsBytes.length))
        if (tChunkHeader.type == ResourceType.RES_TABLE_LIBRARY_TYPE.shortValue()) {
            tResTablePackage.mResTableLibHeader = ResTableLibHeader.parse(Utils.subBytes(bytes, tResTablePackage.mResTablePackageHeader.length()
                    + tResTablePackage.mTypeAndKeyStringsBytes.length))
            tResTablePackage.mResTableLibEntry = ResTableLibEntry.parse(Utils.subBytes(bytes, tResTablePackage.mResTablePackageHeader.length()
                    + tResTablePackage.mTypeAndKeyStringsBytes.length + tResTablePackage.mResTableLibHeader.length()))
        }
        tOffset = tResTablePackage.mResTablePackageHeader.length() + tResTablePackage.mTypeAndKeyStringsBytes.length
        if (tResTablePackage.mResTableLibHeader != null && tResTablePackage.mResTableLibEntry != null) {
            tOffset += tResTablePackage.mResTableLibHeader.length() + tResTablePackage.mResTableLibEntry.length()
        }
        tResTablePackage.mResTableData = parseResTableData(Utils.subBytes(bytes, tOffset))
        return tResTablePackage
    }

    static final List<ResTableData> parseResTableData(byte[] bytes) {
        if (!bytes) {
            return null
        }
        List<ResTableData> tResTableDataList = new ArrayList<>()
        int tOffset = 0;
        ResTableData tResTableData = null
        while (!(tOffset >= bytes.length)) {
            ResChunkHeader tResChunkHeader = ResChunkHeader.parse(Utils.subBytes(bytes, tOffset))
            switch (tResChunkHeader.type) {
                case ResourceType.RES_TABLE_TYPE_SPEC_TYPE:
                    tResTableData = new ResTableData()
                    tResTableDataList.add(tResTableData)
                    tResTableData.mResTableTypeSpec = ResTableTypeSpec.parse(Utils.subBytes(bytes, tOffset))
                    tResTableData.mResTableTypeSpecBytes = Utils.subBytes(bytes, tOffset + tResTableData.mResTableTypeSpec.length(),
                            tResTableData.mResTableTypeSpec.entryCount * 4)
                    tOffset += tResTableData.mResTableTypeSpec.header.size
                    tResTableData.mLength += tResTableData.mResTableTypeSpec.header.size
                    break;
                case ResourceType.RES_TABLE_TYPE_TYPE:
                    tResTableData.mResTableTypeData.add(ResTableTypeData.parse(Utils.subBytes(bytes, tOffset)))
                    tOffset += tResTableData.mResTableTypeData.get(tResTableData.mResTableTypeData.size() - 1).length()
                    tResTableData.mLength += tResTableData.mResTableTypeData.get(tResTableData.mResTableTypeData.size() - 1).length()
                    break;
            }
        }
        return tResTableDataList
    }

    @Override
    byte[] bytes() {
        ByteBuffer tByteBuffer = ByteBuffer.allocate(length())
        tByteBuffer.put(mResTablePackageHeader.bytes())
        tByteBuffer.put(mTypeAndKeyStringsBytes)
        if (mResTableLibHeader != null && mResTableLibEntry != null) {
            tByteBuffer.put(mResTableLibHeader.bytes())
            tByteBuffer.put(mResTableLibEntry.bytes())
        }
        if (!mResTableData.isEmpty()) {
            mResTableData.each {
                tByteBuffer.put(it.bytes())
            }
        }
        return tByteBuffer.array()
    }

    @Override
    int length() {
        return mResTablePackageHeader.header.size
    }
}
