package com.rose.gateway.config.markers

import com.rose.gateway.discord.bot.DiscordBot
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Transient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Represents a config object for a Discord extension
 *
 * @property extensionName The name of the extension
 * @constructor Creates a common extension config for the given extension
 *
 * @param enabled Whether the extension will start enabled
 */
open class CommonExtensionConfig(
    enabled: Boolean,
    @Transient val extensionName: String = "None",
) : KoinComponent, ConfigObject {
    private val bot: DiscordBot by inject()

    /**
     * Whether the extension is enabled
     */
    @ConfigItem("Whether the extension is enabled.")
    var enabled = enabled
        set(value) {
            field = value
            modifyExtensionLoadedStatus(value)
        }

    /**
     * Modifies whether an extension is enabled in the bot or not
     *
     * @param enabled The extensions new status
     */
    private fun modifyExtensionLoadedStatus(enabled: Boolean) {
        runBlocking {
            if (enabled) {
                bot.kordexBot?.loadExtension(extensionName)
            } else {
                bot.kordexBot?.unloadExtension(extensionName)
            }
        }
    }
}
