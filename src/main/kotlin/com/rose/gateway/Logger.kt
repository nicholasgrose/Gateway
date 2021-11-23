package com.rose.gateway

import org.bukkit.Bukkit

object Logger {
    private val bukkitLogger = Bukkit.getLogger()

    fun logInfo(message: String) {
        bukkitLogger.info("[Gateway] $message")
    }
}
