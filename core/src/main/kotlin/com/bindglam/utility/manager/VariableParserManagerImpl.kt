package com.bindglam.utility.manager

import com.bindglam.utility.playerdata.VariableParser

class VariableParserManagerImpl : VariableParserManager {
    private val parsers = HashMap<Class<*>, VariableParser<*>>()

    override fun register(parser: VariableParser<*>) {
        parsers[parser.typeClass] = parser
    }

    override fun parseToJSON(`object`: Any?): Any? {
        if(`object` == null) return null
        if(!parsers.containsKey(`object`.javaClass)) return null

        return parsers[`object`.javaClass]!!.parseToJSON(`object`)
    }

    override fun parseFromJSON(json: Any?): Any? {
        if(json == null) return null

        for (parser in parsers.values) {
            val result = parser.parseFromJSON(json)

            if(result != null)
                return result
        }
        return null
    }
}