package com.rose.gateway.config.markers

import com.rose.gateway.discord.bot.DiscordBotController
import com.rose.gateway.shared.concurrency.PluginCoroutineScope
import kotlinx.coroutines.launch
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
    private val bot: DiscordBotController by inject()
    private val pluginsScope: PluginCoroutineScope by inject()

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
        pluginsScope.launch {
            if (enabled) {
                bot.discordBot.kordexBot?.loadExtension(extensionName)
            } else {
                bot.discordBot.kordexBot?.unloadExtension(extensionName)
            }
        }
    }
}
