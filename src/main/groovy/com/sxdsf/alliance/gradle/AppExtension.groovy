package com.sxdsf.alliance.gradle

/**
 * AppExtension
 * @author 孙博闻
 * @date 2016/12/12 下午7:16
 * @desc 此插件的配置项
 */
class AppExtension {

    static final def CONFIG_NAME = 'jdbModule'

    int packageId

    List<String> excludeLibs

}
