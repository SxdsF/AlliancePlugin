package com.sxdsf.alliance.gradle

import com.android.build.gradle.tasks.ProcessAndroidResources
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

/**
 * LibraryPlugin
 * @author 孙博闻
 * @date 2016/12/21 上午11:18
 * @desc Library修改资源的插件
 */
class LibraryPlugin implements Plugin<Project> {

    private static final def RELEASE = 'Release'
    private static final def DEBUG = 'Debug'

    @Override
    void apply(Project target) {

        target.extensions.create(AppExtension.CONFIG_NAME, AppExtension)

        target.afterEvaluate {

            int tPackageId = target.jdbModule.packageId
            println "get packageId:${tPackageId}"
            if (!AppPlugin.isValid(tPackageId)) {
                return
            }

            def tBuildType = isRelease(target) ? RELEASE : DEBUG
            println "library type:${tBuildType}"
            ProcessAndroidResources tProcessAndroidResources = (ProcessAndroidResources) target.tasks.findByName("process${tBuildType}Resources")
            println "library process name:${tProcessAndroidResources.name}"
            //添加资源修改task
            Task tModifyPackageId = target.task ModifyLibraryPackageIdTask.TASK_NAME, type: ModifyLibraryPackageIdTask, {
                mPackageId = target.jdbModule.packageId
                mSourceOutputDir = tProcessAndroidResources.sourceOutputDir
                mSymbolOutputDir = tProcessAndroidResources.textSymbolOutputDir
            }

            //处理资源task要以修改packageId task结尾
            tProcessAndroidResources.finalizedBy tModifyPackageId
        }
    }

    private static boolean isRelease(Project target) {
        boolean tResult = false
        def tTaskNames = target.gradle.startParameter.taskNames
        for (def tTaskName in tTaskNames) {
            if (tTaskName.toLowerCase().contains('release')) {
                println "library type release"
                tResult = true
                break
            }
        }
        return tResult
    }
}
