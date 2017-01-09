package com.sxdsf.alliance.parser

/**
 *
 * enum {RES_NULL_TYPE         = 0x0000,
 * RES_STRING_POOL_TYPE        = 0x0001,
 * RES_TABLE_TYPE              = 0x0002,
 * RES_XML_TYPE                = 0x0003,
 * // Chunk types in RES_XML_TYPE
 * RES_XML_FIRST_CHUNK_TYPE    = 0x0100,
 * RES_XML_START_NAMESPACE_TYPE= 0x0100,
 * RES_XML_END_NAMESPACE_TYPE  = 0x0101,
 * RES_XML_START_ELEMENT_TYPE  = 0x0102,
 * RES_XML_END_ELEMENT_TYPE    = 0x0103,
 * RES_XML_CDATA_TYPE          = 0x0104,
 * RES_XML_LAST_CHUNK_TYPE     = 0x017f,
 * // This contains a uint32_t array mapping strings in the string
 * // pool back to resource identifiers.  It is optional.
 * RES_XML_RESOURCE_MAP_TYPE   = 0x0180,
 * // Chunk types in RES_TABLE_TYPE
 * RES_TABLE_PACKAGE_TYPE      = 0x0200,
 * RES_TABLE_TYPE_TYPE         = 0x0201,
 * RES_TABLE_TYPE_SPEC_TYPE    = 0x0202};
 *
 * ResourceType
 * @author 孙博闻
 * @date 2016/12/14 下午11:43
 * @desc 资源的类型
 */
class ResourceType {

    static final def RES_NULL_TYPE = 0x0000
    static final def RES_STRING_POOL_TYPE = 0x0001
    static final def RES_TABLE_TYPE = 0x0002
    static final def RES_XML_TYPE = 0x0003

    // Chunk types in RES_XML_TYPE
    static final def RES_XML_FIRST_CHUNK_TYPE = 0x0100
    static final def RES_XML_START_NAMESPACE_TYPE = 0x0100
    static final def RES_XML_END_NAMESPACE_TYPE = 0x0101
    static final def RES_XML_START_ELEMENT_TYPE = 0x0102
    static final def RES_XML_END_ELEMENT_TYPE = 0x0103
    static final def RES_XML_CDATA_TYPE = 0x0104
    static final def RES_XML_LAST_CHUNK_TYPE = 0x017f

    // This contains a uint32_t array mapping strings in the string
    // pool back to resource identifiers.  It is optional.
    static final def RES_XML_RESOURCE_MAP_TYPE = 0x0180

    // Chunk types in RES_TABLE_TYPE
    static final def RES_TABLE_PACKAGE_TYPE = 0x0200
    static final def RES_TABLE_TYPE_TYPE = 0x0201
    static final def RES_TABLE_TYPE_SPEC_TYPE = 0x0202
    static final def RES_TABLE_LIBRARY_TYPE = 0x0203
}
