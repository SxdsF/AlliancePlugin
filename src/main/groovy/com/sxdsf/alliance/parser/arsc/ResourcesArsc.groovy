package com.sxdsf.alliance.parser.arsc

import com.sxdsf.alliance.parser.Utils

/**
 * ResourcesArsc
 * @author 孙博闻
 * @date 2016/12/14 下午4:50
 * @desc 标识ResourcesArsc文件
 */
class ResourcesArsc {

    private static final int INVALID = -1
    private static final String SUFFIX = '.temp'

    private final File mFile
    private final ResTable mResTable
    private int mPackageId = INVALID

    ResourcesArsc(File file) {
        mFile = file
        mResTable = file && file.exists() ? ResTable.parse(file.getBytes()) : null
    }

    /**
     * 修改packageId，此处要修改的Id为
     * 1、packageHeader中的Id
     * 2、bag数据中的name.ident(此为bag的Id)
     * 3、bag数据中parent.ident(此为bag的parent的Id)
     *
     * @param id 要修改的Id
     */
    ResourcesArsc modifyPackageId(int id) {
        mPackageId = id
        return this
    }

    void write() {
        File tempFile = new File(mFile.getParentFile(), "${mFile.name}${SUFFIX}")
        if (tempFile.exists()) {
            tempFile.delete()
        }
        tempFile.createNewFile()
        write(tempFile)
        mFile.delete()
        tempFile.renameTo(mFile)
    }

    void write(File target) {
        if (!target) {
            return
        }
        if (!target.exists()) {
            return
        }

        if (mPackageId != INVALID) {

            //设置package header中的packageId
            mResTable.mResTablePackage.mResTablePackageHeader.id = mPackageId

            //设置bag，bag parent，non bag的id
            if (!mResTable.mResTablePackage.mResTableData.isEmpty()) {
                mResTable.mResTablePackage.mResTableData.each {
                    if (it.mResTableTypeData.isEmpty()) {
                        return
                    }
                    it.mResTableTypeData.each {
                        if (it.mResTableEntryData.isEmpty()) {
                            return
                        }
                        it.mResTableEntryData.each {
                            if (it instanceof BagData) {
                                it.mResTableMapEntry.parent.ident = Utils.replacePackageId(it.mResTableMapEntry.parent.ident, mPackageId)
                                if (it.mResTableMaps.isEmpty()) {
                                    return
                                }
                                it.mResTableMaps.each {
                                    it.name.ident = Utils.replacePackageId(it.name.ident, mPackageId)
                                    it.value.data = Utils.replacePackageId(it.value.data, mPackageId)
                                }
                            } else if (it instanceof NonBagData) {
                                it.mResValue.data = Utils.replacePackageId(it.mResValue.data, mPackageId)
                            }
                        }
                    }
                }
            }

            //插入dynamic res table
            mResTable.insertDynamicTable(mPackageId)
        }

        target.withOutputStream { os ->
            os.write(mResTable.bytes())
        }
    }
}
