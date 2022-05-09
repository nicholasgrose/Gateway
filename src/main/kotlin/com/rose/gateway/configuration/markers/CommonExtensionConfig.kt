package com.rose.gateway.configuration.markers

import com.rose.gateway.GatewayPlugin
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class CommonExtensionConfig<T>(private val extensionName: String) : KoinComponent, ConfigObject {
    val plugin by inject<GatewayPlugin>()

    fun modifyExtensionLoadedStatus(enabled: Boolean) {
        runBlocking {
            if (enabled) {
                plugin.discordBot.bot?.loadExtension(extensionName)
            } else {
                plugin.discordBot.bot?.unloadExtension(extensionName)
            }
        }
    }
}
