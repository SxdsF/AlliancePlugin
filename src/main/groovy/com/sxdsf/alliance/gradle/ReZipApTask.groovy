package com.sxdsf.alliance.gradle

import org.gradle.api.tasks.bundling.Zip

/**
 * ReZipApTask
 * @author 孙博闻
 * @date 2016/12/13 上午9:50
 * @desc 用于把修改过后的*.ap_文件重新打包
 */
class ReZipApTask extends Zip {

    static final def TASK_NAME = 'reZipAp'
}
