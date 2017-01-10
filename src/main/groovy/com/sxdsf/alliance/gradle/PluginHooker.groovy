package com.sxdsf.alliance.gradle

import com.sxdsf.alliance.parser.arsc.ResourcesArsc
import com.sxdsf.alliance.parser.axml.ResourceAXml
import org.gradle.api.Project
import org.gradle.api.file.FileTree

/**
 * com.sxdsf.alliance.gradle.PluginHooker
 * @author 孙博闻
 * @date 2017/1/9 下午3:59
 * @desc 本插件Hook Android gradle task的Hooker
 */
class PluginHooker {

    static final AP_UNZIP_FILE_NAME = 'unzip-ap'
    private static final RESOURCES_ARSC = 'resources.arsc'
    private static final ANDROID_MANIFEST = 'AndroidManifest.xml'
    private static final RES_DIR_PATTERN = 'res/**/*'
    private static final ANDROID_SUPPORT = 'com.android.support'

    /**
     * 修改资源Id的task，一共有5步
     *
     * 0、修改./build/intermediates/res目录下，以ap_结尾的zip包里的resources.arsc
     * 1、修改./build/intermediates/res目录下，以ap_结尾的zip包里的AndroidManifest.xml
     * 2、修改./build/intermediates/res目录下，以ap_结尾的zip包里的res文件夹下的所有xml文件
     * 3、修改./build/generated/source/r目录下，debug、release对应的R.java
     * 4、修改/build/intermediates/symbols目录下，debug、release对应的R.txt
     *
     * @param project gradle project
     * @param packageId 资源Id
     * @param apFile ap文件
     * @param sourceOutputDir R.java文件的dir
     * @param symbolOutputDir R.txt文件的dir
     *
     * @return 返回解压后的Ap文件目录
     */
    static File modifyPackageId(Project project, int packageId, File apFile, File sourceOutputDir, File symbolOutputDir) {

        println ":${project.name}:modifyPackageId"

        FileTree tApFiles = project.zipTree(apFile)
        File tUnzipApDir = new File(apFile.parentFile, AP_UNZIP_FILE_NAME)
        if (tUnzipApDir.exists()) {
            tUnzipApDir.delete()
        }
        project.copy {
            from tApFiles
            into tUnzipApDir

            include ANDROID_MANIFEST
            include RESOURCES_ARSC
            include RES_DIR_PATTERN
        }

        //0、修改./build/intermediates/res目录下，以ap_结尾的zip包里的resources.arsc
        new ResourcesArsc(new File(tUnzipApDir, RESOURCES_ARSC)).modifyPackageId(packageId).write()

        //1、修改./build/intermediates/res目录下，以ap_结尾的zip包里的AndroidManifest.xml
        //2、修改./build/intermediates/res目录下，以ap_结尾的zip包里的res文件夹下的所有xml文件
        modifyXml(tUnzipApDir, packageId)

        //3、修改./build/generated/source/r目录下，debug、release对应的R.java
        modifyRJava(sourceOutputDir, packageId)

        //4、修改/build/intermediates/symbols目录下，debug、release对应的R.txt
        modifyRFile(new File(symbolOutputDir, 'R.txt'), packageId)

        return tUnzipApDir
    }

    /**
     * 清除exclude的res资源
     *
     * @param project 当前的project
     * @param unZipApFile 资源所在的文件夹
     * @param excludeFileNames 需要被清除的文件名的map
     */
    static excludeRes(Project project, File unZipApFile, Map<String, String> excludeFileNames) {

        println "${project.name}:excludeRes"

//        deleteFile(new File(unZipApFile, 'res'), excludeFileNames)
    }

    private static deleteFile(File file, Map<String, String> excludeFileNames) {

        if (!file) {
            return
        }

        if (!file.exists()) {
            return
        }

        if (file.isDirectory()) {
            file.eachFile {
                deleteFile(it, excludeFileNames)
            }
        } else {
            if (excludeFileNames.get(file.name)) {
                file.deleteOnExit()
            }
        }
    }

//    /**
//     * 重新zip ap file
//     *
//     * @param project 当前的project
//     * @param unZipApFile 被解压缩后的文件夹
//     * @param buildType 当前的构建模式，是debug还是release
//     */
//    static reZipApFile(Project project, File unZipApFile, String buildType) {
//
//        println ":${project.name}:reZipApFile"
//
////        ZipCompressor tZipCompressor = new DefaultZipCompressor(false, ZipOutputStream.DEFLATED)
//        ZipOutputStream tZos
//        BufferedOutputStream tBos
//        try {
//            File tTargetFile = new File(unZipApFile.parentFile, "resources-${buildType}.ap_")
//            tTargetFile.deleteOnExit()
//            tZos = new ZipOutputStream(tTargetFile)
//            tBos = new BufferedOutputStream(tZos)
//            zip(tZos, unZipApFile, unZipApFile.getName(), tBos)
//        } catch (Exception e) {
//            println e.getMessage()
//        } finally {
//            if (tBos) {
//                try {
//                    tBos.close()
//                } catch (Exception e) {
//                    println e.getMessage()
//                }
//            }
//            if (tZos) {
//                try {
//                    tZos.close()
//                } catch (Exception e) {
//                    println e.getMessage()
//                }
//            }
//        }
//    }
//
//    private static zip(ZipOutputStream zos, File file, String basePath, BufferedOutputStream bos) throws Exception {
//        if (file.isDirectory()) {
//            File[] tFiles = file.listFiles()
//            if (tFiles.length == 0) {
//                zos.putNextEntry(new ZipEntry(basePath + "/"))
//            }
//            for (int i = 0; i < tFiles.length; i++) {
//                zip(zos, tFiles[i], basePath + "/" + tFiles[i].getName(), bos)
//            }
//        } else {
//            zos.putNextEntry(new ZipEntry(basePath))
//            FileInputStream tFis = new FileInputStream(file)
//            BufferedInputStream tBis = new BufferedInputStream(tFis)
//            int b
//            while ((b = tBis.read()) != -1) {
//                bos.write(b)
//            }
//            tBis.close()
//            tFis.close()
//        }
//    }

    /**
     * 清除不需要的class文件
     *
     * @param project 当前工程
     * @param classesDir 放class的文件夹
     * @param packages 不需要的包名称
     */
    static excludeClasses(Project project, File classesDir, List<String> packages) {

        println ":${project.name}:excludeClasses"

        if (!packages || packages.isEmpty()) {
            return
        }

        packages.each {
            File tFile = new File(classesDir, packageForDirPath(it))
            tFile.deleteDir()
        }
    }

    /**
     * 重命名不需要的jar包
     * @param project 当前工程
     * @param jarsDir 存放jar包的文件夹
     * @param libraries 要排除的库
     * @return
     */
    static reNameExcludeJars(Project project, File jarsDir, List<Library> libraries) {

        println ":${project.name}:reNameExcludeJars"

        libraries.each {
            if (ANDROID_SUPPORT == it.mModuleGroup) {
                reNameJar(new File(jarsDir, it.mModuleGroup))
            } else {
                reNameJar(new File(jarsDir, "${it.mModuleGroup}${File.separator}${it.mModuleName}${File.separator}${it.mModuleVersion}"))
            }
        }

    }

    private static reNameJar(File file) {
        if (!file) {
            return
        }

        if (!file.exists()) {
            return
        }

//        if (file.isDirectory()) {
//            reNameJar(file)
//        } else {
//            if (file.name.endsWith('.jar')) {
//                file.renameTo(new File(file.parentFile, "${file.name}.tmp"))
//            }
//        }

        traverseFile file, { File temp ->
            if (temp.name.endsWith('.jar')) {
                temp.renameTo(new File(temp.parentFile, "${temp.name}.tmp"))
            }
        }
    }

    /**
     * 重置重命名后的jar包
     * @param project 当前工程
     * @param jarsDir 存放jar包的文件夹
     * @param libraries 要排除的库
     * @return
     */
    static reverseReNameExcludeJars(Project project, File jarsDir, List<Library> libraries) {

        println ":${project.name}:reverseReNameExcludeJars"

        libraries.each {
            if (ANDROID_SUPPORT == it.mModuleGroup) {
                revereReNameJar(new File(jarsDir, it.mModuleGroup))
            } else {
                revereReNameJar(new File(jarsDir, "${it.mModuleGroup}${File.separator}${it.mModuleName}${File.separator}${it.mModuleVersion}"))
            }
        }

    }

    private static revereReNameJar(File file) {
        if (!file) {
            return
        }

        if (!file.exists()) {
            return
        }

//        if (file.isDirectory()) {
//            reNameJar(file)
//        } else {
//            if (file.name.endsWith('.jar.tmp')) {
//                file.renameTo(new File(file.parentFile, "${file.name.substring(0, file.length() - 4)}"))
//            }
//        }

        traverseFile file, { File temp ->
            if (temp.name.endsWith('.jar.tmp')) {
                temp.renameTo(new File(temp.parentFile, "${temp.name.substring(0, temp.name.length() - 4)}"))
            }
        }
    }


    private static modifyXml(File file, int id) {
        if (!file) {
            return
        }

        if (!file.exists()) {
            return
        }

//        if (file.isDirectory()) {
//            file.eachFile {
//                modifyXml(it, id)
//            }
//        } else {
//            if (file.name.endsWith('.xml')) {
//                new ResourceAXml(file).modifyPackageId(id).write()
//            }
//        }

        traverseFile file, { File temp ->
            if (temp.name.endsWith('.xml')) {
                new ResourceAXml(temp).modifyPackageId(id).write()
            }
        }
    }

    private static modifyRJava(File file, int id) {
        if (!file) {
            return
        }

        if (!file.exists()) {
            return
        }

//        if (file.isDirectory()) {
//            file.eachFile {
//                modifyRJava(it, id)
//            }
//        } else {
//            if (file.name.contains('R') && file.name.endsWith('.java')) {
//                modifyRFile(file, id)
//            }
//        }

        traverseFile file, { File temp ->
            if (temp.name.contains('R') && temp.name.endsWith('.java')) {
                modifyRFile(temp, id)
            }
        }
    }

    private static modifyRFile(File file, int id) {
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
                            array.eachWithIndex { entry, int i ->
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

    private static String packageForDirPath(String packageName) {
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
            tPath += File.separator + entry
        }
        return tPath
    }

    private static traverseFile(File file, Closure closure) {
        if (file.exists()) {
            LinkedList<File> tDirectories = new LinkedList<File>()
            File[] files = file.listFiles()
            for (File temp : files) {
                if (temp.isDirectory()) {
                    tDirectories.add(temp)
                } else {
                    closure.call(temp)
                }
            }
            while (!tDirectories.isEmpty()) {
                File tFirstFile = tDirectories.removeFirst()
                files = tFirstFile.listFiles()
                for (File temp : files) {
                    if (temp.isDirectory()) {
                        tDirectories.add(temp)
                    } else {
                        closure.call(temp)
                    }
                }
            }
        }
    }

    static class Library {

        String mModuleGroup

        String mModuleName

        String mModuleVersion
    }
}
