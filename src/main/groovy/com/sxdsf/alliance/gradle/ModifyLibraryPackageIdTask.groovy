package com.sxdsf.alliance.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * ModifyLibraryPackageIdTask
 * @author 孙博闻
 * @date 2016/12/21 下午3:01
 * @desc 修改library包的packageId
 */
class ModifyLibraryPackageIdTask extends DefaultTask {

    static final def TASK_NAME = 'modifyPackageId'

    /**
     * 当前设置的packageId
     */
    int mPackageId
    /**
     * 存放R.java的dir
     */
    File mSourceOutputDir
    /**
     * R.txt文件的dir
     */
    String mSymbolOutputDir


    @TaskAction
    void modify() {

        println "wanted package id:${mPackageId}"

        //0、修改./build/generated/source/r目录下，debug、release对应的R.java
        modifyRJava(mSourceOutputDir, mPackageId)

        //1、修改/build/intermediates/bundles目录下，debug、release对应的R.txt
        modifyRFile(new File(mSymbolOutputDir, 'R.txt'), mPackageId)
    }

    static def modifyRJava(File file, int id) {
        if (!file) {
            return
        }

        if (!file.exists()) {
            return
        }

        if (file.isDirectory()) {
            file.eachFile {
                modifyRJava(it, id)
            }
        } else {
            if (file.name.contains('R') && file.name.endsWith('.java')) {
                modifyRFile(file, id)
            }
        }
    }

    static def modifyRFile(File file, int id) {
        if (!file) {
            return
        }

        if (!file.exists()) {
            return
        }

        File tempFile = new File(file.getParentFile(), '.temp.cfg')
        if (tempFile.exists()) {
            tempFile.delete()
        }
        tempFile.createNewFile()

        def tStringId = "0x${Integer.toHexString(id)}"
        tempFile.withOutputStream { os ->
            file.withInputStream { is ->
                def list = is.readLines()
                list.each { line ->
                    if (line.contains('0x7f')) {
                        def array = line.split('0x7f')
                        if (array.length > 1) {
                            line = array[0] + tStringId
                            array.eachWithIndex { def entry, int i ->
                                if (i > 0) {
                                    if (i < array.length - 1) {
                                        line += (entry + tStringId)
                                    } else {
                                        line += entry
                                    }
                                }
                            }
                        }
                    }
                    os.write((line + '\r\n').getBytes())
                }
            }
        }

        file.delete()
        tempFile.renameTo(file)
    }
}
