package com.sxdsf.alliance.parser.axml
/**
 * Parser
 * @author 孙博闻
 * @date 2016/12/15 下午8:17
 * @desc 解析
 */

def read(File file) {
    file.withInputStream { is ->
        def allBytes = is.getBytes()
        AndroidBinaryXml tAndroidBinaryXml = AndroidBinaryXml.parse(allBytes)
        println "bytes size：${allBytes.length}"
        println "file size：${tAndroidBinaryXml.mResXmlTreeHeader.header.size}"
        println "string pool size：${tAndroidBinaryXml.mResStringPool.mResStringPoolHeader.header.size}"
        println "resourceIds size：${tAndroidBinaryXml.mResourceIds.header.size}"
        tAndroidBinaryXml.mResContent.mStartNamespace.each {
            println "namespace  size：${it.bytes().size()}"
        }
        tAndroidBinaryXml.mResContent.mElements.each {
            if (it instanceof StartElement) {
                println "start ${it.mResXmlTreeAttrExt.attributeStart}"
                println "count ${it.mResXmlTreeAttrExt.attributeCount}"
                println "size ${it.mResXmlTreeAttrExt.attributeSize}"
                it.mResXmlTreeAttributes.each {
                    println "data：${it.resourceId}"
                }
            }
        }
    }
}

def modifyXml(File file, int id) {
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
        System.out.println(file.name)
        if (file.name.endsWith('.xml')) {
            ResourceAXml tResourceAXml = new ResourceAXml(file)
            tResourceAXml.modifyPackageId(125).write()
        }
    }
}

//read new File('/Users/sunbowen/Documents/workspaces/private/Groovy/AndroidResourceParser/resource/notification_tile_bg.xml')
//ResourceAXml tResourceAXml = new ResourceAXml(new File('/Users/sunbowen/Documents/workspaces/private/Groovy/AndroidResourceParser/resource/app-debug/AndroidManifest.xml'))
//tResourceAXml.modifyPackageId(126).write()
//read new File('/Users/sunbowen/Documents/workspaces/private/Groovy/AndroidResourceParser/resource/app-debug/AndroidManifest.xml')

modifyXml(new File('/Users/sunbowen/Documents/workspaces/private/Groovy/AndroidResourceParser/resource/resources-debug/res'), 125)

