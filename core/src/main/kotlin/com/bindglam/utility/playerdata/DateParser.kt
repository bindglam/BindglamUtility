package com.bindglam.utility.playerdata

import com.alibaba.fastjson2.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


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
        if(json !is JSONObject)
            return null
        if(json.getString("__type__") != "date")
            return null

        return LocalDateTime.parse(json.getString("data"), DATE_TIME_FORMATTER)
    }
}