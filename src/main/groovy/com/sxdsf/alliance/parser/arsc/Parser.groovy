package com.sxdsf.alliance.parser.arsc

import com.sxdsf.alliance.parser.ResStringPoolHeader
import com.sxdsf.alliance.parser.ResTableHeader
import com.sxdsf.alliance.parser.ResTablePackageHeader
import com.sxdsf.alliance.parser.Utils

/**
 * Parser
 * @author 孙博闻
 * @date 2016/12/14 上午11:36
 * @desc 解析
 */
def read(File file) {
    file.withInputStream { is ->
        def allBytes = is.getBytes()
        println 'ResTableHeader'
        def bytes = Utils.subBytes(allBytes, 0)
        ResTableHeader tResTableHeader = ResTableHeader.parse(bytes)
        println "res table type:${tResTableHeader.header.type}"
        println "res table size:${tResTableHeader.header.size}"
        println "packageCount:${tResTableHeader.packageCount}"
        println '-----------------------'

        println 'ResStringPoolHeader'
        bytes = Utils.subBytes(allBytes, tResTableHeader.length())
        ResStringPoolHeader tResStringPoolHeader = ResStringPoolHeader.parse(bytes)
        println "res string pool type:${tResStringPoolHeader.header.type}"
        println "res string pool size:${tResStringPoolHeader.header.size}"
        println "stringCount:${tResStringPoolHeader.stringCount}"
        println "styleCount:${tResStringPoolHeader.styleCount}"
        println "flags:${tResStringPoolHeader.flags}"
        println "stringsStart:${tResStringPoolHeader.stringsStart}"
        println "stylesStart:${tResStringPoolHeader.stylesStart}"
        println '-----------------------'

        println 'ResTablePackageHeader'
        bytes = Utils.subBytes(allBytes, tResTableHeader.length() + tResStringPoolHeader.header.size)
        ResTablePackageHeader tResTablePackage = ResTablePackageHeader.parse(bytes)
        println "res table package type:${tResTablePackage.header.type}"
        println "res table package size:${tResTablePackage.header.size}"
        println "id:${tResTablePackage.id}"
        println "nanme:${tResTablePackage.getStringName()}"
        println "typeStrings:${tResTablePackage.typeStrings}"
        println "lastPublicType:${tResTablePackage.lastPublicType}"
        println "keyStrings:${tResTablePackage.keyStrings}"
        println "lastPublicKey:${tResTablePackage.lastPublicKey}"
        println "typeIdOffset:${tResTablePackage.typeIdOffset}"
        println '-----------------------'
    }
}

//read new File('/Users/sunbowen/Documents/workspaces/private/Groovy/AndroidResourceParser/resource/resources-debug/resources.arsc')
//println "--------------修改前--------------"
ResourcesArsc tResourcesArsc = new ResourcesArsc(new File('/Users/sunbowen/Documents/workspaces/private/Groovy/AndroidResourceParser/resource/resources.arsc'))
tResourcesArsc.modifyPackageId(126).write()
//println "--------------修改后--------------"
//new ResourcesArsc(new File('/Users/sunbowen/Documents/workspaces/private/Groovy/AndroidResourceParser/resource/resources.arsc'))
//read new File('/Users/sunbowen/Documents/workspaces/private/Groovy/AndroidResourceParser/resource/resources-debug/resources.arsc')

File tFile = new File('/Users/sunbowen/Documents/workspaces/private/Groovy/AndroidResourceParser/resource/resources.arsc')
ResTable tResTable = ResTable.parse(tFile.getBytes())
println tResTable.toString()

