package com.rose.gateway

import com.rose.gateway.minecraft.server.Scheduler
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object Logger : KoinComponent {
    private val plugin: GatewayPlugin by inject()
    private val pluginLogger = plugin.logger

    fun info(message: String) {
        Scheduler.runTask { pluginLogger.info(message) }
    }

    fun warning(message: String) {
        Scheduler.runTask { pluginLogger.warning(message) }
    }
}
