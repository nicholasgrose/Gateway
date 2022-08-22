package com.rose.gateway.discord.bot

import com.rose.gateway.config.PluginConfig
import com.rose.gateway.config.extensions.botChannels
import com.rose.gateway.discord.bot.client.ClientInfo
import dev.kord.core.entity.Guild
import dev.kord.core.entity.channel.TextChannel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BotContext : KoinComponent {
    private val bot: DiscordBot by inject()
    private val config: PluginConfig by inject()

    val botChannels = mutableSetOf<TextChannel>()
    val botGuilds = mutableSetOf<Guild>()

    suspend fun fillBotChannels() {
        val validBotChannels = config.botChannels()

        botChannels.clear()
        botGuilds.clear()

        bot.kordClient()?.guilds?.collect { guild ->
            guild.channels.collect { channel ->
                if (
                    ClientInfo.hasChannelPermissions(channel, DiscordBotConstants.REQUIRED_PERMISSIONS) &&
                    channel is TextChannel &&
                    channel.name in validBotChannels
                ) {
                    botChannels.add(channel)
                    botGuilds.add(guild)
                }
            }
        }
    }
}
