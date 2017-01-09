package com.sxdsf.alliance.gradle

import com.android.build.gradle.tasks.ProcessAndroidResources
import com.android.build.gradle.tasks.factory.AndroidJavaCompile
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

/**
 * AppPlugin
 * @author 孙博闻
 * @date 2016/12/12 下午6:26
 * @desc Apk修改资源的插件
 */
class AppPlugin implements Plugin<Project> {

    private static final def RELEASE = 'Release'
    private static final def DEBUG = 'Debug'
    private static final def DEPENDENCY_MODE_NAME = 'excludedCompile'

    @Override
    void apply(Project target) {

        //添加配置字段
        target.extensions.create(AppExtension.CONFIG_NAME, AppExtension)
        //增加一种依赖配置
        target.configurations.create DEPENDENCY_MODE_NAME
        target.dependencies {
            compile target.configurations.getByName(DEPENDENCY_MODE_NAME)
        }

        target.afterEvaluate {

            def libraries = []
            def dependencies = target.configurations.getByName(DEPENDENCY_MODE_NAME).resolvedConfiguration.firstLevelModuleDependencies
            dependencies.each {
                println "dependencies module group:${it.moduleGroup}"
                println "dependencies module name:${it.moduleName}"
                println "dependencies module version:${it.moduleVersion}"
                println "dependencies name:${it.name}"
                println "dependencies configuration:${it.configuration}"
                println "dependencies module id:${it.module.id}"
                it.children.each {
                    println "children:${it.name}"
                }
            }

            def tPackageId = target.extensions.getByName(AppExtension.CONFIG_NAME).packageId
            def tExcludeLibs = target.extensions.getByName(AppExtension.CONFIG_NAME).excludeLibs
            tExcludeLibs.each {
                println it
            }
            if (!isValid(tPackageId)) {
                return
            }

            //判断当前是release还是debug
            def tBuildType = isRelease(target) ? RELEASE : DEBUG
            ProcessAndroidResources tProcessAndroidResources = (ProcessAndroidResources) target.tasks.findByName("process${tBuildType}Resources")
            tProcessAndroidResources.doFirst {
                File tFile = new File(packageOutputFile.parentFile.parentFile, "exploded-aar${File.separator}GradleDemo")
//                tFile.deleteDir()
            }

            //添加资源修改task
            Task tModifyPackageId = target.task ModifyAppPackageIdTask.TASK_NAME, type: ModifyAppPackageIdTask,
                    { ModifyAppPackageIdTask tTask ->
                        tTask.mPackageId = tPackageId
                        tTask.mApFile = tProcessAndroidResources.packageOutputFile
                        tTask.mSourceOutputDir = tProcessAndroidResources.sourceOutputDir
                        tTask.mSymbolOutputDir = tProcessAndroidResources.textSymbolOutputDir
                    }

            //处理资源task要以修改packageId task结尾
            tProcessAndroidResources.finalizedBy tModifyPackageId

            def tReZipDir = tProcessAndroidResources.packageOutputFile.parentFile.path
            def tArchiveName = "resources-${tBuildType}.ap_"
            //添加重新zip *.ap_文件task
            Task tReZipAp = target.task "${ReZipApTask.TASK_NAME}", type: ReZipApTask, {
                destinationDir = new File(tReZipDir)
                archiveName = tArchiveName
                from {
                    "${tReZipDir}${File.separator}${ModifyAppPackageIdTask.AP_UNZIP_FILE_NAME}"
                }

            }

            //修改packageId task要以reZipAp task结尾
            tModifyPackageId.finalizedBy tReZipAp

            AndroidJavaCompile tCompileJava = (AndroidJavaCompile) target.tasks.findByName("compile${tBuildType}JavaWithJavac")
            println "compile destination:${tCompileJava.destinationDir}"

//
//            TransformTask tTransformTask = (TransformTask) target.tasks.findByName("transformClassesWithDexFor${tBuildType}")
//
//            AndroidJarTask tAndroidJarTask = (AndroidJarTask) target.tasks.findByName("jar${tBuildType}Classes")
//            println "archive path:${tAndroidJarTask.archivePath}"
//            println "archive destination:${tAndroidJarTask.destinationDir}"

            Task tRemoveLibraryClasses = target.task "${RemoveLibraryClassesTask.TASK_NAME}", type: RemoveLibraryClassesTask

            tCompileJava.finalizedBy tRemoveLibraryClasses
//
//            target.tasks.findByName("package${tBuildType}").doFirst {
//                File tFile = new File('/Users/sunbowen/Documents/workspaces/private/Gradle/GradleDemo/app/build/intermediates/pre-dexed/debug')
//                tFile.eachFile {
//                    println "before package:${it.name}"
//                }
//            }

//            target.tasks.each {
//                println "task name:${it.name} task class:${it.class}"
//                println ""
//            }
        }
    }

    private static boolean isRelease(Project target) {
        boolean tResult = false
        def tTaskNames = target.gradle.startParameter.taskNames
        for (def tTaskName in tTaskNames) {
            if (tTaskName.startsWith('assemble')) {
                if (tTaskName.contains('Release')) {
                    tResult = true
                    break
                }
            }
        }
        return tResult
    }

    /**
     * @param packageId packageId
     * @return 返回packageId是否合法，只有在0x01————0x7f之间的才合法，其中0x01，0x02，0x7f为保留值
     */
    public static boolean isValid(def packageId) {
        boolean tResult = false
        if (packageId > 2 && packageId < 127) {
            tResult = true
        }
        return tResult
    }
}
