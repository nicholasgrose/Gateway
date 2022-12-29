package com.rose.gateway.minecraft.logging

import com.rose.gateway.GatewayPlugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.logging.FileHandler
import java.util.logging.Logger
import java.util.logging.SimpleFormatter
import kotlin.reflect.jvm.jvmName

object FileLogger : KoinComponent, Logger("File Logger", null) {
    val logger: Logger = Logger.getLogger(
        FileLogger::class.simpleName
    )
    val plugin: GatewayPlugin by inject()
    val handler: FileHandler
    init {
        handler = FileHandler(plugin.dataFolder.path.plus("/gateway_log.txt"), true)
        logger.addHandler(handler)
        val formatter = LogFormatter()
        handler.formatter = formatter
        logger.useParentHandlers = false
    }

}
