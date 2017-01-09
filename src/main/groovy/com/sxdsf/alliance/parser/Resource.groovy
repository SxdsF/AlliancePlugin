package com.sxdsf.alliance.parser

/**
 * Resource
 * @author 孙博闻
 * @date 2016/12/14 下午5:59
 * @desc 资源接口
 */
interface Resource {

    /**
     * @return 数据结构资源转成的byte数组
     */
    byte[] bytes()

    /**
     * @return 返回这个结构的长度
     */
    int length()
}