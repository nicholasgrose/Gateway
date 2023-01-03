package com.rose.gateway.minecraft.logging

import com.rose.gateway.GatewayPlugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.logging.FileHandler
import java.util.logging.Logger

/**
 * Helper object that provides logging to file
 */
object FileLogger : KoinComponent {
    /**
     * this is the logger as a var
     */
    val logger: Logger = Logger.getLogger(
        FileLogger::class.simpleName
    )
    private val plugin: GatewayPlugin by inject()
    private val handler: FileHandler

    init {
        handler = FileHandler(plugin.dataFolder.path.plus("/gateway_log.txt"), true)
        logger.addHandler(handler)
        val formatter = LogFormatter()
        handler.formatter = formatter
        logger.useParentHandlers = false
    }
}
