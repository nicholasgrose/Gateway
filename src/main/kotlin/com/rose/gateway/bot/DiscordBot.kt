package com.rose.gateway.bot

import com.kotlindiscord.kord.extensions.DISCORD_GREEN
import com.kotlindiscord.kord.extensions.ExtensibleBot
import com.kotlindiscord.kord.extensions.checks.isNotbot
import com.rose.gateway.GatewayPlugin
import com.rose.gateway.Logger
import com.rose.gateway.bot.checks.DefaultCheck
import com.rose.gateway.bot.client.ClientInfo
import com.rose.gateway.bot.presence.DynamicPresence
import com.rose.gateway.configuration.Configurator
import com.rose.gateway.configuration.PluginSpec
import dev.kord.core.Kord
import dev.kord.core.entity.Guild
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.exception.KordInitializationException
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.datetime.Clock
import net.kyori.adventure.text.format.TextColor
import org.koin.core.context.stopKoin

class DiscordBot {
    companion object {
        fun getBotChannels(): Set<TextChannel> {
            return GatewayPlugin.plugin.discordBot.botChannels
        }

        fun getBotGuilds(): Set<Guild> {
            return GatewayPlugin.plugin.discordBot.botGuilds
        }

        fun getKordClient(): Kord? {
            return GatewayPlugin.plugin.discordBot.kordClient
        }

        fun getBot(): ExtensibleBot? {
            return GatewayPlugin.plugin.discordBot.bot
        }

        fun getMemberQueryMax(): Int {
            return GatewayPlugin.plugin.discordBot.memberQueryMax
        }

        fun getDiscordColor(): TextColor {
            return GatewayPlugin.plugin.discordBot.discordColor
        }

        fun getMentionColor(): TextColor {
            return GatewayPlugin.plugin.discordBot.mentionColor
        }
    }

    private val botChannels = mutableSetOf<TextChannel>()
    private val botGuilds = mutableSetOf<Guild>()
    private var job: Job? = null

    val memberQueryMax by lazy { Configurator.config[PluginSpec.BotSpec.memberQueryMax] }
    val discordColor by lazy { TextColor.fromHexString(Configurator.config[PluginSpec.MinecraftSpec.discordUserColor])!! }
    val mentionColor by lazy { TextColor.fromHexString(Configurator.config[PluginSpec.MinecraftSpec.discordMentionColor])!! }
    var botStatus = BotStatus.NOT_STARTED

    private val bot = try {
        runBlocking { createBot(Configurator.config[PluginSpec.botToken]) }
    } catch (e: KordInitializationException) {
        botStatus = BotStatus.STOPPED because e.localizedMessage
        null
    }

    private suspend fun createBot(token: String): ExtensibleBot {
        return ExtensibleBot(token) {
            hooks {
                kordShutdownHook = false
            }
            presence {
                since = Clock.System.now()
                playing(DynamicPresence.presenceForPlayerCount())
            }
            messageCommands {
                defaultPrefix = Configurator.config[PluginSpec.BotSpec.commandPrefix]
                invokeOnMention = true
                check(DefaultCheck.defaultCheck, isNotbot)
            }
            slashCommands {
                enabled = false
            }
            extensions {
                DiscordBotConstants.BOT_EXTENSIONS.filter { Configurator.extensionEnabled(it) }.forEach { add(it) }

                help {
                    paginatorTimeout = Configurator.config[PluginSpec.BotSpec.commandTimeout].toLong()
                    deletePaginatorOnTimeout = true
                    deleteInvocationOnPaginatorTimeout = true
                    colour { DISCORD_GREEN }
                }
            }
        }
    }

    private val kordClient = bot?.getKoin()?.get<Kord>()

    suspend fun start() {
        if (bot == null) {
            return
        }

        botStatus = BotStatus.STARTING

        fillBotChannels()

        job = CoroutineScope(Dispatchers.Default).launch {
            try {
                botStatus = BotStatus.RUNNING
                bot.start()
            } catch (error: KordInitializationException) {
                val message = "An error occurred while running bot: ${error.message}"
                Logger.log(message)
                botStatus = BotStatus.STOPPED because message
            }
        }

        Logger.log("Bot ready!")
    }

    private suspend fun fillBotChannels() {
        val validBotChannels = Configurator.config[PluginSpec.BotSpec.botChannels]
        kordClient!!.guilds.collect { guild: Guild ->
            guild.channels.collect { channel ->
                if (
                    ClientInfo.hasChannelPermissions(channel, DiscordBotConstants.REQUIRED_PERMISSIONS)
                    && channel is TextChannel
                    && channel.name in validBotChannels
                ) {
                    botChannels.add(channel)
                    botGuilds.add(guild)
                }
            }
        }
    }

    suspend fun stop() {
        botStatus = BotStatus.STOPPING

        if (kordClient != null) {
            kordClient.shutdown()
            kordClient.coroutineContext.job.join()
        }

        job?.join()
        stopKoin()

        botStatus = BotStatus.STOPPED
    }
}
