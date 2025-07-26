package com.bindglam.utility.manager

import com.bindglam.utility.playerdata.*

class VariableParserManagerImpl : VariableParserManager {
    private val parsers = ArrayList<VariableParser<*>>()

    private val listParser = ListParser()
    private val mapParser = MapParser()

    init {
        register(ItemStackParser())
        register(DateParser())
    }

    override fun register(parser: VariableParser<*>) {
        parsers.add(parser)
    }

    override fun parseToJSON(`object`: Any?): Any? {
        if(`object` == null)
            return null

        for (parser in parsers) {
            val result = parser.parseToJSON(`object`) ?: continue

            return result
        }

        return listParser.parseToJSON(`object`) ?: mapParser.parseToJSON(`object`) ?: `object`
    }

    override fun parseFromJSON(json: Any?): Any? {
        if(json == null)
            return null

        for (parser in parsers) {
            val result = parser.parseFromJSON(json)

            if(result != null)
                return result
        }

        return listParser.parseFromJSON(json) ?: mapParser.parseFromJSON(json) ?: json
    }
}