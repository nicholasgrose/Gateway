package com.rose.gateway.configuration.specs

import com.rose.gateway.GatewayPlugin
import com.uchuhimo.konf.ConfigSpec
import kotlinx.coroutines.runBlocking

abstract class CommonExtensionSpec(private val extensionName: String) : ConfigSpec(), ResponsiveSpec {
    val enabled by optional(true, description = "Whether the ${super.prefix} extension is enabled.")

    fun modifyExtensionLoadedStatus(enabled: Boolean, plugin: GatewayPlugin) {
        runBlocking {
            if (enabled) {
                plugin.discordBot.bot?.loadExtension(extensionName)
            } else {
                plugin.discordBot.bot?.unloadExtension(extensionName)
            }
        }
    }
}
