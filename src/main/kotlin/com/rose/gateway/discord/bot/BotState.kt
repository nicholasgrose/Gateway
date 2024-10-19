package com.rose.gateway.discord.bot

import com.rose.gateway.config.PluginConfig
import com.rose.gateway.config.access.botChannels
import com.rose.gateway.discord.bot.client.ClientInfo
import com.rose.gateway.shared.concurrency.PluginCoroutineScope
import dev.kord.core.entity.Guild
import dev.kord.core.entity.channel.TextChannel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * The Discord context in which the bot operates
 *
 * @constructor Creates an empty bot context
 */
class BotState : KoinComponent {
    private val bot: DiscordBotController by inject()
    private val config: PluginConfig by inject()
    private val pluginScope: PluginCoroutineScope by inject()

    /**
     * The bot's status
     */
    var status = BotStatus.NOT_STARTED

    /**
     * The job running the bot
     */
    var botJob: Job? = null

    /**
     * The text channels the bot operates in
     */
    val botChannels = mutableSetOf<TextChannel>()

    /**
     * The guilds the bot operates in
     */
    val botGuilds = mutableSetOf<Guild>()

    init {
        pluginScope.launch {
            fillBotChannels()
        }
    }

    /**
     * Fills the valid bot channel set and the valid bot guild set
     */
    suspend fun fillBotChannels() {
        val validBotChannels = config.botChannels()

        botChannels.clear()
        botGuilds.clear()

        bot.discordBot.kordClient()?.guilds?.collect { guild ->
            guild.channels.collect { channel ->
                if (ClientInfo.hasChannelPermissions(
                        channel,
                        DiscordBotConstants.REQUIRED_PERMISSIONS,
                    ) &&
                    channel is TextChannel &&
                    channel.name in validBotChannels
                ) {
                    botChannels.add(channel)
                    // The guilds are only added if they have valid channels for message sending.
                    botGuilds.add(guild)
                }
            }
        }
    }
}
