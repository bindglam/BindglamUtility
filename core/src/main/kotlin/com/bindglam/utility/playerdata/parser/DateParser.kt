package com.bindglam.utility.playerdata.parser

import com.alibaba.fastjson2.JSONObject
import com.bindglam.utility.playerdata.VariableParser
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.get


class DateParser : VariableParser<LocalDateTime> {
    companion object {
        private val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    }

    override fun parseToJSON(obj: Any): Any? {
        if(obj !is LocalDateTime)
            return null

        return JSONObject().apply {
            put("__type__", "date")
            put("data", obj.format(DATE_TIME_FORMATTER))
        }
    }

    override fun parseFromJSON(json: Any): LocalDateTime? {
        if(json !is Map<*, *>)
            return null
        if(json["__type__"] != "date")
            return null

        return LocalDateTime.parse(json["data"] as String, DATE_TIME_FORMATTER)
    }
}