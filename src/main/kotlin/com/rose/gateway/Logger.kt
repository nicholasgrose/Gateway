package com.rose.gateway

import org.bukkit.Bukkit
import java.util.logging.Level

object Logger {
    private val bukkitLogger = Bukkit.getLogger()

    fun logInfo(message: String) {
        bukkitLogger.log(Level.INFO, gatewayPrefixedMessage(message))
    }

    private fun gatewayPrefixedMessage(message: String): String {
        return "[Gateway] $message"
    }

    fun logDebug(message: String) {
        bukkitLogger.log(Level.FINEST, gatewayPrefixedMessage(message))
    }
}
