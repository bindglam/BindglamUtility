package com.bindglam.utility.manager

import com.bindglam.utility.playerdata.*
import com.bindglam.utility.playerdata.parser.DateParser
import com.bindglam.utility.playerdata.parser.ItemStackParser
import com.bindglam.utility.playerdata.parser.ListParser
import com.bindglam.utility.playerdata.parser.MapParser
import org.bukkit.inventory.ItemStack
import java.time.LocalDateTime

class VariableParserManagerImpl : VariableParserManager {
    private val parsers = HashMap<Class<*>, VariableParser<*>>()

    private val listParser = ListParser()
    private val mapParser = MapParser()

    init {
        register(ItemStack::class.java, ItemStackParser())
        register(LocalDateTime::class.java, DateParser())
    }

    override fun <T> register(clazz: Class<T>, parser: VariableParser<T>) {
        parsers[clazz] = parser
    }

    override fun parseToJSON(`object`: Any?): Any? {
        if(`object` == null)
            return null

        for (clazz in parsers.keys) {
            if(clazz.isInstance(`object`)) {
                return parsers[clazz]!!.parseToJSON(`object`) ?: continue
            }
        }

        return listParser.parseToJSON(`object`) ?: mapParser.parseToJSON(`object`) ?: `object`
    }

    override fun parseFromJSON(json: Any?): Any? {
        if(json == null)
            return null

        for (parser in parsers.values) {
            val result = parser.parseFromJSON(json)

            if(result != null)
                return result
        }

        return listParser.parseFromJSON(json) ?: mapParser.parseFromJSON(json) ?: json
    }
}