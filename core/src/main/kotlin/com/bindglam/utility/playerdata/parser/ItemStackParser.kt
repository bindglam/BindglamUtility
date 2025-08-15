package com.bindglam.utility.playerdata.parser

import com.alibaba.fastjson2.JSONObject
import com.bindglam.utility.playerdata.VariableParser
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.collections.get

class ItemStackParser : VariableParser<ItemStack> {
    override fun parseToJSON(obj: Any): Any? {
        if(obj !is ItemStack)
            return null

        return JSONObject().apply {
            put("__type__", "itemstack")
            put("data", Base64.getEncoder().encodeToString(obj.serializeAsBytes()))
        }
    }

    override fun parseFromJSON(json: Any): ItemStack? {
        if(json !is Map<*, *>)
            return null
        if(json["__type__"] != "itemstack")
            return null

        return ItemStack.deserializeBytes(Base64.getDecoder().decode(json["data"] as String))
    }
}