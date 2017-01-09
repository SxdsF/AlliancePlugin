package com.sxdsf.alliance.gradle

import com.sxdsf.alliance.parser.arsc.ResourcesArsc
import com.sxdsf.alliance.parser.axml.ResourceAXml
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.TaskAction

/**
 * ModifyAppPackageIdTask
 * @author 孙博闻
 * @date 2016/12/12 下午6:48
 * @desc 修改资源Id的task，一共有6步
 *
 * 0、修改./build/intermediates/res目录下，以ap_结尾的zip包里的resources.arsc
 * 1、修改./build/intermediates/res目录下，以ap_结尾的zip包里的AndroidManifest.xml
 * 2、修改./build/intermediates/res目录下，以ap_结尾的zip包里的res文件夹下的所有xml文件
 * 3、修改./build/generated/source/r目录下，debug、release对应的R.java
 * 4、修改/build/intermediates/symbols目录下，debug、release对应的R.txt
 */
class ModifyAppPackageIdTask extends DefaultTask {

    static final def TASK_NAME = 'modifyPackageId'
    static final def AP_UNZIP_FILE_NAME = 'unzip-ap'

    private static final def RESOURCES_ARSC = 'resources.arsc'
    private static final def LINE_SEPARATOR = File.separator

    /**
     * 当前设置的packageId
     */
    int mPackageId
    /**
     * ap_结尾的文件
     */
    File mApFile
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
        //声明ap_文件
        File tApFile = mApFile
        FileTree tApFiles = project.zipTree(tApFile)
        File tUnzipApDir = new File(tApFile.parentFile, AP_UNZIP_FILE_NAME)
        if (tUnzipApDir.exists()) {
            tUnzipApDir.delete()
        }
        project.copy {
            from tApFiles
            into tUnzipApDir

            include 'AndroidManifest.xml'
            include 'resources.arsc'
            include 'res/**/*'
        }

        println "wanted package id:${mPackageId}"

        //0、修改./build/intermediates/res目录下，以ap_结尾的zip包里的resources.arsc
        new ResourcesArsc(new File(tUnzipApDir, RESOURCES_ARSC)).modifyPackageId(mPackageId).write()

        //1、修改./build/intermediates/res目录下，以ap_结尾的zip包里的AndroidManifest.xml
        //2、修改./build/intermediates/res目录下，以ap_结尾的zip包里的res文件夹下的所有xml文件
        modifyXml(tUnzipApDir, mPackageId)

        //3、修改./build/generated/source/r目录下，debug、release对应的R.java
        modifyRJava(mSourceOutputDir, mPackageId)

        //4、修改/build/intermediates/symbols目录下，debug、release对应的R.txt
        modifyRFile(new File(mSymbolOutputDir, 'R.txt'), mPackageId)
    }


    static def modifyXml(File file, int id) {
        if (!file) {
            return
        }

        if (!file.exists()) {
            return
        }

        if (file.isDirectory()) {
            file.eachFile {
                modifyXml(it, id)
            }
        } else {
            if (file.name.endsWith('.xml')) {
                new ResourceAXml(file).modifyPackageId(id).write()
            }
        }
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

    static String packageForDirPath(String packageName) {
        if (!packageName) {
            return null
        }
        def tList = packageName.split('\\.')
        if (!(tList.length > 0)) {
            return null
        }
        String tPath = tList[0]
        tList.eachWithIndex { String entry, int i ->
            if (i == 0) {
                return
            }
            tPath += LINE_SEPARATOR + entry
        }
        return tPath
    }
}
