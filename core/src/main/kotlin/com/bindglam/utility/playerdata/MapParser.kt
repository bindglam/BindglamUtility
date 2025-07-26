package com.bindglam.utility.playerdata

import com.alibaba.fastjson2.JSONObject
import com.bindglam.utility.BindglamUtility


class MapParser : VariableParser<Map<*, *>> {
    override fun parseToJSON(obj: Any): Any? {
        if(obj !is Map<*, *>)
            return null

        return JSONObject().apply {
            obj.forEach { (key, value) -> put(key.toString(), BindglamUtility.getInstance().variableParserManager.parseToJSON(value)) }
        }
    }

    override fun parseFromJSON(json: Any): Map<*, *>? {
        if(json !is JSONObject)
            return null

        val result = hashMapOf<String, Any?>()

        json.forEach { (key, value) -> result[key] = BindglamUtility.getInstance().variableParserManager.parseFromJSON(value) }

        return result
    }
}