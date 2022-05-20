package com.rose.gateway.configuration.markers

import com.rose.gateway.bot.DiscordBot
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Transient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class CommonExtensionConfig<T>(
    enabled: Boolean,
    @Transient
    val extensionName: String = "None"
) : KoinComponent, ConfigObject {
    val bot: DiscordBot by inject()

    @ConfigItem("Whether the extension is enabled.")
    var enabled = enabled
        set(value) {
            field = value
            modifyExtensionLoadedStatus(value)
        }

    private fun modifyExtensionLoadedStatus(enabled: Boolean) {
        runBlocking {
            if (enabled) {
                bot.bot?.loadExtension(extensionName)
            } else {
                bot.bot?.unloadExtension(extensionName)
            }
        }
    }
}
