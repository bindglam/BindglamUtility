package com.bindglam.utility.utils

import org.bukkit.Bukkit

data class MinecraftVersion(val first: Int, val second: Int, val third: Int): Comparable<MinecraftVersion> {
    companion object {
        val current = MinecraftVersion(Bukkit.getBukkitVersion().substringBefore('-'))

        val V1_21_4 = MinecraftVersion(1, 21, 4)
        val V1_21_8 = MinecraftVersion(1, 21, 8)

        private val comparator = Comparator.comparing { v: MinecraftVersion -> v.first }
            .thenComparing { v: MinecraftVersion -> v.second }
            .thenComparing { v: MinecraftVersion -> v.third }
    }

    constructor(string: String): this(string.split('.'))
    constructor(string: List<String>): this(
        if (string.isNotEmpty()) string[0].toInt() else 0,
        if (string.size > 1) string[1].toInt() else 0,
        if (string.size > 2) string[2].toInt() else 0
    )

    override fun compareTo(other: MinecraftVersion): Int {
        return comparator.compare(this, other)
    }

    override fun toString(): String {
        return if (third == 0) "$first.$second" else "$first.$second.$third"
    }
}