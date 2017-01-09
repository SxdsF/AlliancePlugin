package com.sxdsf.alliance.parser.axml

import com.sxdsf.alliance.parser.Utils

/**
 * ResourceAXml
 * @author 孙博闻
 * @date 2016/12/15 上午11:49
 * @desc 标识一个android二进制xml资源
 */
class ResourceAXml {

    private static final int INVALID = -1
    private static final String SUFFIX = '.temp'

    private final File mFile
    private final AndroidBinaryXml mAndroidBinaryXml
    private int mPackageId = INVALID

    ResourceAXml(File file) {
        mFile = file
        mAndroidBinaryXml = file && file.exists() ? AndroidBinaryXml.parse(file.getBytes()) : null
    }

    /**
     * 修改packageId，资源文件修改的Id为每一个element数据中，resourceId值为0x7F开头的
     *
     * @param id 要修改的id
     */
    ResourceAXml modifyPackageId(int id) {
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
            if (!mAndroidBinaryXml.mResContent.mElements.isEmpty()) {
                mAndroidBinaryXml.mResContent.mElements.each {
                    if (it instanceof StartElement) {
                        it.mResXmlTreeAttributes.each {
                            it.resourceId = Utils.replacePackageId(it.resourceId, mPackageId)
                        }
                    }
                }
            }
        }

        target.withOutputStream { os ->
            os.write(mAndroidBinaryXml.bytes())
        }
    }
}
