package com.sxdsf.alliance.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * RemoveLibraryClassesTask
 * @author 孙博闻
 * @date 2016/12/22 下午7:54
 * @desc 移除library中的classes文件的任务
 */
class RemoveLibraryClassesTask extends DefaultTask {

    static final def TASK_NAME = "removeLibraryClasses";

    /**
     * 存放class的文件夹
     */
    File mClassesDir
    /**
     * 要删除的library
     */
    List<Library> mLibraries

    @TaskAction
    void remove() {

        if (mClassesDir == null || !mClassesDir.exists() || !mClassesDir.isDirectory()) {
            return
        }

        if (mLibraries == null || mLibraries.isEmpty()) {
            return
        }

        mLibraries.each {

        }
    }

    class Library {
        /**
         * 企业
         */
        String mGroup
        /**
         * 名称
         */
        String mName
        /**
         * 版本
         */
        String mVersion
    }
}