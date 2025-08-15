package com.bindglam.utility.playerdata.parser

import com.alibaba.fastjson2.JSONObject
import com.bindglam.utility.BindglamUtility
import com.bindglam.utility.playerdata.VariableParser


class MapParser : VariableParser<Map<*, *>> {
    override fun parseToJSON(obj: Any): Any? {
        if(obj !is Map<*, *>)
            return null

        return JSONObject().apply {
            obj.forEach { (key, value) -> put(key.toString(), BindglamUtility.getInstance().variableParserManager.parseToJSON(value)) }
        }
    }

    override fun parseFromJSON(json: Any): Map<*, *>? {
        if(json !is Map<*, *>)
            return null

        val result = hashMapOf<String, Any?>()

        json.forEach { (key, value) -> result[key as String] = BindglamUtility.getInstance().variableParserManager.parseFromJSON(value) }

        return result
    }
}