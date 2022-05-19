package com.rose.gateway.configuration.schema

import com.rose.gateway.GatewayPlugin
import com.rose.gateway.bot.DiscordBot
import com.rose.gateway.configuration.markers.ConfigItem
import com.rose.gateway.configuration.markers.ConfigObject
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BotConfig(
    token: String,
    botChannels: List<String>,
    @ConfigItem
    var extension: ExtensionConfig
) : KoinComponent, ConfigObject {
    val plugin: GatewayPlugin by inject()
    val bot: DiscordBot by inject()

    @ConfigItem(
        """
            The token used by the bot to access discord.
            Accessible at https://discord.com/developers/applications/.
        """
    )
    var token = token
        set(value) {
            field = value
            runBlocking {
                bot.rebuild()
            }
        }

    @ConfigItem("The names of the channels in which the bot should respond to commands and post/accept chat messages.")
    var botChannels = botChannels
        set(value) {
            field = value
            runBlocking { bot.fillBotChannels() }
        }
}
