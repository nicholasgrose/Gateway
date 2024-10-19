package com.rose.gateway.minecraft.logging

import com.rose.gateway.GatewayPlugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.logging.FileHandler

/**
 * Helper object that provides logging functionality throughout Gateway
 */
object Logger : KoinComponent {
    private val plugin: GatewayPlugin by inject()
    private val pluginLogger = plugin.logger
    private val handler: FileHandler = FileHandler(plugin.dataFolder.path.plus("/gateway_log.txt"), true)

    init {
        handler.formatter = LogFormatter()
        pluginLogger.addHandler(handler)
    }

    /**
     * Logs a message with level "INFO"
     *
     * @param message The message to log
     */
    fun info(message: String) {
        pluginLogger.info(message)
    }

    /**
     * Logs a message with level "WARNING"
     *
     * @param message The message to log
     */
    fun warning(message: String) {
        pluginLogger.warning(message)
    }

    /**
     * Logs a message with level "ERROR"
     *
     * @param message The message to log
     */
    fun error(message: String) {
        pluginLogger.severe(message)
    }
}
