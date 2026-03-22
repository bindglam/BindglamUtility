package com.bindglam.utility.utils

import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

object InteractionGuard {
    private const val GUI_CLICK_COOLDOWN_MS = 120L

    private val lastGuiClickTimes = ConcurrentHashMap<UUID, Long>()

    @JvmStatic
    fun isGuiClickOnCooldown(playerUuid: UUID): Boolean {
        val now = System.currentTimeMillis()
        val lastClickTime = lastGuiClickTimes[playerUuid]

        if (lastClickTime != null && now - lastClickTime < GUI_CLICK_COOLDOWN_MS) {
            return true
        }

        lastGuiClickTimes[playerUuid] = now
        return false
    }

    @JvmStatic
    fun clearGuiClickCooldown(playerUuid: UUID) {
        lastGuiClickTimes.remove(playerUuid)
    }
}
