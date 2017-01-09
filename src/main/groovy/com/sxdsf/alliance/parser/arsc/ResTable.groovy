package com.sxdsf.alliance.parser.arsc

import com.rrh.jdb.parser.*
import com.sxdsf.alliance.parser.ResChunkHeader
import com.sxdsf.alliance.parser.ResStringPool
import com.sxdsf.alliance.parser.ResTableHeader
import com.sxdsf.alliance.parser.ResTableLibEntry
import com.sxdsf.alliance.parser.ResTableLibHeader
import com.sxdsf.alliance.parser.Resource
import com.sxdsf.alliance.parser.ResourceType
import com.sxdsf.alliance.parser.Utils

import java.nio.ByteBuffer

/**
 * ResTable
 * @author 孙博闻
 * @date 2016/12/14 下午5:29
 * @desc resources.arsc解析出来就是一个ResourceTable对象
 * 一个resources.arsc文件的结构就是——>资源索引表头部+字符串资源池+N个Package数据块
 */
class ResTable implements Resource {

    /**
     * 资源索引头部
     */
    ResTableHeader mResTableHeader
    /**
     * 全局字符串资源池
     */
    ResStringPool mResStringPool
    /**
     * package数据块
     */
    ResTablePackage mResTablePackage

    static final ResTable parse(byte[] bytes) {
        if (!bytes) {
            return null
        }

        ResTable tResTable = new ResTable()
        tResTable.mResTableHeader = ResTableHeader.parse(Utils.subBytes(bytes, 0))
        tResTable.mResStringPool = ResStringPool.parse(Utils.subBytes(bytes, tResTable.mResTableHeader.length()))
        tResTable.mResTablePackage = ResTablePackage.parse(Utils.subBytes(bytes, tResTable.mResTableHeader.length()
                + tResTable.mResStringPool.length()))
        return tResTable
    }

    void insertDynamicTable(int packageId) {
        //先插入数据
        ResChunkHeader tResChunkHeader = new ResChunkHeader()
        tResChunkHeader.type = ResourceType.RES_TABLE_LIBRARY_TYPE
        tResChunkHeader.headerSize = 12
        tResChunkHeader.size = 272
        mResTablePackage.mResTableLibHeader = new ResTableLibHeader()
        mResTablePackage.mResTableLibHeader.header = tResChunkHeader
        mResTablePackage.mResTableLibHeader.count = 1
        mResTablePackage.mResTableLibEntry = new ResTableLibEntry()
        mResTablePackage.mResTableLibEntry.packageId = packageId
        mResTablePackage.mResTableLibEntry.packageName = mResTablePackage.mResTablePackageHeader.name

        //修改长度，res table header中的size和res table package header中的size
        mResTableHeader.header.size += 272
        mResTablePackage.mResTablePackageHeader.header.size += 272
    }

    @Override
    byte[] bytes() {
        return ByteBuffer.allocate(length()).
                put(mResTableHeader.bytes()).
                put(mResStringPool.bytes()).
                put(mResTablePackage.bytes()).
                array()
    }

    @Override
    int length() {
        return mResTableHeader.header.size
    }

    @Override
    String toString() {
        String tText = '##################################################' + '\r\n' + '\r\n'
        tText += 'ResTableHeader' + '\r\n' + '++++++++++++++++++++++++++++++++++++++++++++++++++' + '\r\n'
        tText += mResTableHeader.toString() + '\r\n' + '\r\n'
        tText += 'GlobalResStringPool' + '\r\n' + '++++++++++++++++++++++++++++++++++++++++++++++++++' + '\r\n'
        tText += mResStringPool.toString() + '\r\n' + '\r\n'
        tText += 'ResTablePackage' + '\r\n' + '++++++++++++++++++++++++++++++++++++++++++++++++++' + '\r\n'
        tText += mResTablePackage.toString() + '\r\n' + '\r\n'
        tText += '##################################################'
        return tText
    }
}
