package com.rose.gateway

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object Logger : KoinComponent {
    private val plugin: GatewayPlugin by inject()
    private val pluginLogger = plugin.logger

    fun info(message: String) {
        pluginLogger.info(message)
    }

    fun warning(message: String) {
        pluginLogger.warning(message)
    }
}
