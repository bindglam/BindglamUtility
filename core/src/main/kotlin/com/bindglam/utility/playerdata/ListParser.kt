package com.bindglam.utility.playerdata

import com.alibaba.fastjson2.JSONArray
import com.bindglam.utility.BindglamUtility


class ListParser : VariableParser<List<*>> {
    override fun parseToJSON(obj: Any): Any? {
        if(obj !is List<*>)
            return null

        return JSONArray().apply {
            obj.forEach { value -> add(BindglamUtility.getInstance().variableParserManager.parseToJSON(value)) }
        }
    }

    override fun parseFromJSON(json: Any): List<*>? {
        if(json !is List<*>)
            return null

        val result = arrayListOf<Any?>()

        json.forEach { value -> result.add(BindglamUtility.getInstance().variableParserManager.parseFromJSON(value)) }

        return result
    }
}