package com.bindglam.utility.playerdata

import org.bukkit.NamespacedKey
import org.bukkit.plugin.Plugin

class VariableHolderImpl(private val plugin: Plugin) : VariableHolder {
    private val variables = HashMap<NamespacedKey, Any?>()

    override fun <T> getVariable(key: NamespacedKey): T? = variables[key] as T?

    @Deprecated("Deprecated in Java")
    override fun <T> getVariable(name: String): T? = variables[NamespacedKey(plugin, name)] as T?

    override fun getString(key: NamespacedKey): String? {
        val `var` = variables[key]

        if(`var` !is String?) return null

        return `var`
    }

    override fun getNumber(key: NamespacedKey): Number? {
        val `var` = variables[key]

        if(`var` !is Number?) return null

        return `var`
    }

    override fun getFloat(key: NamespacedKey): Float = getNumber(key)?.toFloat() ?: 0f
    override fun getDouble(key: NamespacedKey): Double = getNumber(key)?.toDouble() ?: 0.0
    override fun getInt(key: NamespacedKey): Int = getNumber(key)?.toInt() ?: 0
    override fun getLong(key: NamespacedKey): Long = getNumber(key)?.toLong() ?: 0L

    override fun getBoolean(key: NamespacedKey): Boolean {
        val `var` = variables[key]

        if(`var` !is Boolean?) return false

        return `var` == true
    }

    override fun setVariable(key: NamespacedKey, value: Any?) {
        variables[key] = value
    }

    @Deprecated("Deprecated in Java")
    override fun setVariable(name: String, value: Any?) {
        variables[NamespacedKey(plugin, name)] = value
    }

    override fun hasVariable(key: NamespacedKey): Boolean = variables.containsKey(key)

    @Deprecated("Deprecated in Java")
    override fun hasVariable(name: String): Boolean = variables.containsKey(NamespacedKey(plugin, name))

    override fun removeVariable(key: NamespacedKey) {
        variables.remove(key)
    }

    @Deprecated("Deprecated in Java")
    override fun removeVariable(name: String) {
        variables.remove(NamespacedKey(plugin, name))
    }

    override fun getVariables(): Map<NamespacedKey, Any?> = variables.toMap()
}