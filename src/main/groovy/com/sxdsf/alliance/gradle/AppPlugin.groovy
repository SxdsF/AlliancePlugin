package com.sxdsf.alliance.gradle

import com.android.build.gradle.internal.pipeline.TransformTask
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

    private static final RELEASE = 'Release'
    private static final DEBUG = 'Debug'
    private static final DEPENDENCY_MODE_NAME = 'excludedCompile'

    @Override
    void apply(Project target) {

        //添加配置字段
        target.extensions.create(AppExtension.CONFIG_NAME, AppExtension)
        //增加一种依赖配置
        target.configurations.create DEPENDENCY_MODE_NAME
        target.dependencies {
            compile target.configurations.getByName(DEPENDENCY_MODE_NAME)
        }

        target.afterEvaluate { Project tProject ->

            int tPackageId = tProject.extensions.getByName(AppExtension.CONFIG_NAME).packageId
            List<String> tExcludeLibs = tProject.extensions.getByName(AppExtension.CONFIG_NAME).excludeLibs

            if (!isValid(tPackageId)) {
                return
            }

            //判断当前是release还是debug
            def tBuildType = isRelease(tProject) ? RELEASE : DEBUG
            ProcessAndroidResources tProcessAndroidResources = (ProcessAndroidResources) tProject.tasks.findByName("process${tBuildType}Resources")
            tProcessAndroidResources.doFirst {
//                File tFile = new File(packageOutputFile.parentFile.parentFile, "exploded-aar${File.separator}GradleDemo")
//                tFile.deleteDir()
            }

            tProcessAndroidResources << {
                //修改packageId
                File tUnZipApFile = PluginHooker.modifyPackageId(tProject, tPackageId,
                        tProcessAndroidResources.packageOutputFile,
                        tProcessAndroidResources.sourceOutputDir,
                        tProcessAndroidResources.textSymbolOutputDir)

                //清除不要的res资源
                PluginHooker.excludeRes(tProject, tUnZipApFile, null)
            }

            //添加重新zip *.ap_文件task
            def tReZipDir = tProcessAndroidResources.packageOutputFile.parentFile.path
            def tArchiveName = "resources-${tBuildType}.ap_"
            Task tReZipAp = tProject.task "${ReZipApTask.TASK_NAME}", type: ReZipApTask, { ReZipApTask reZipApTask ->
                reZipApTask.destinationDir = new File(tReZipDir)
                reZipApTask.archiveName = tArchiveName
                reZipApTask.from {
                    "${tReZipDir}${File.separator}${PluginHooker.AP_UNZIP_FILE_NAME}"
                }

            }
            //处理资源task要以reZipAp task结尾
            tProcessAndroidResources.finalizedBy tReZipAp

            def libraries = []
            def dependencies = tProject.configurations.getByName(DEPENDENCY_MODE_NAME).resolvedConfiguration.firstLevelModuleDependencies
            dependencies.each {
                libraries << new PluginHooker.Library(mModuleGroup: it.moduleGroup, mModuleName: it.moduleName, mModuleVersion: it.moduleVersion)
                it.children.each {
                    libraries << new PluginHooker.Library(mModuleGroup: it.moduleGroup, mModuleName: it.moduleName, mModuleVersion: it.moduleVersion)
                }
            }

            AndroidJavaCompile tCompileJava = (AndroidJavaCompile) tProject.tasks.findByName("compile${tBuildType}JavaWithJavac")
            tCompileJava << {
                //清除不需要的classes
                PluginHooker.excludeClasses(tProject, tCompileJava.destinationDir, tExcludeLibs)


            }

            TransformTask tTransformTask = (TransformTask) tProject.tasks.findByName("transformClassesWithDexFor${tBuildType}")
            tTransformTask.doFirst {
                //重命名不需要的jar包
                PluginHooker.reNameExcludeJars(tProject, new File(tProject.buildDir, "intermediates${File.separator}exploded-aar"), libraries)
            }

            tTransformTask << {
                //将上一步重命名的jar包恢复
                PluginHooker.reverseReNameExcludeJars(tProject, new File(tProject.buildDir, "intermediates${File.separator}exploded-aar"), libraries)
            }
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
