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
        FileLogger::class.jvmName
    )
    val plugin: GatewayPlugin by inject()
    val handler: FileHandler
    init {
        handler = FileHandler(plugin.dataFolder.path.plus("/Gateway_Log"))
        logger.addHandler(handler)
        val formatter = SimpleFormatter()
        handler.formatter = formatter
    }
}
